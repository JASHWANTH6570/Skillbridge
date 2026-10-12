import React, { useState, useEffect } from 'react';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import SkillGapRadar from '../components/SkillGapRadar';
import { 
  BarChart3, 
  Sparkles, 
  ArrowRight, 
  AlertCircle, 
  CheckCircle2, 
  Lightbulb, 
  TrendingUp 
} from 'lucide-react';

export default function SkillGapAnalysisPage({ onGenerateRoadmap }) {
  const { user } = useAuth();
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);

  const roleId = user?.targetRoleId || 1;

  useEffect(() => {
    async function loadAnalysis() {
      try {
        const data = await api.getGapAnalysis(roleId);
        setAnalysis(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    loadAnalysis();
  }, [roleId]);

  const handleGenerate = async () => {
    setGenerating(true);
    try {
      await api.generateRoadmap(roleId);
      if (onGenerateRoadmap) {
        onGenerateRoadmap();
      }
    } catch (err) {
      alert('Failed to generate roadmap: ' + err.message);
    } finally {
      setGenerating(false);
    }
  };

  if (loading) {
    return <div className="p-12 text-center text-slate-500">Computing gap analysis across role profile...</div>;
  }

  if (!analysis) {
    return <div className="p-12 text-center text-slate-500">No gap analysis available.</div>;
  }

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      {/* Header Banner */}
      <div className="bg-white rounded-2xl p-6 sm:p-8 border border-slate-200 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
        <div>
          <div className="flex items-center gap-2 text-indigo-600 font-bold text-xs uppercase tracking-wider mb-1">
            <BarChart3 className="w-4 h-4" />
            Gap Analysis Engine
          </div>
          <h2 className="text-2xl font-black text-slate-900">
            {analysis.roleTitle} Skill Profile Comparison
          </h2>
          <p className="text-sm text-slate-500 mt-1 max-w-xl">
            Department: {analysis.roleDepartment} • Target Level: {analysis.experienceLevel}
          </p>
        </div>

        <button
          onClick={handleGenerate}
          disabled={generating}
          className="flex items-center gap-2 px-6 py-3.5 rounded-xl bg-gradient-to-r from-indigo-600 to-violet-600 hover:from-indigo-700 hover:to-violet-700 text-white font-bold text-sm shadow-lg shadow-indigo-500/25 shrink-0 transition-transform active:scale-95"
        >
          <Sparkles className="w-4 h-4" />
          <span>{generating ? 'Synthesizing Milestones...' : 'Build Custom Roadmap'}</span>
          <ArrowRight className="w-4 h-4" />
        </button>
      </div>

      {/* KPI Stats Grid */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase">Career Readiness</span>
          <div className="text-2xl sm:text-3xl font-black text-indigo-600 mt-1">
            {analysis.readinessScore}%
          </div>
          <span className="text-[11px] text-slate-400">Weighted against role</span>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase">Total Competencies</span>
          <div className="text-2xl sm:text-3xl font-black text-slate-900 mt-1">
            {analysis.totalRequiredSkills}
          </div>
          <span className="text-[11px] text-slate-400">Curated by staff</span>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase">Skills Met</span>
          <div className="text-2xl sm:text-3xl font-black text-emerald-600 mt-1">
            {analysis.skillsMetCount}
          </div>
          <span className="text-[11px] text-emerald-700 font-medium">Ready for interview</span>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase">Critical Gaps</span>
          <div className="text-2xl sm:text-3xl font-black text-rose-600 mt-1">
            {analysis.criticalGapsCount}
          </div>
          <span className="text-[11px] text-rose-700 font-medium">High priority focus</span>
        </div>
      </div>

      {/* Recommendations Banner */}
      {analysis.topPriorityRecommendations && analysis.topPriorityRecommendations.length > 0 && (
        <div className="bg-amber-50/70 border border-amber-200/80 rounded-2xl p-6">
          <div className="flex items-center gap-2 text-amber-800 font-bold text-sm mb-3">
            <Lightbulb className="w-5 h-5 text-amber-600" />
            AI Identified High-Impact Priorities
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            {analysis.topPriorityRecommendations.map((rec, idx) => (
              <div key={idx} className="bg-white/90 p-3.5 rounded-xl border border-amber-200/50 text-xs text-slate-700 font-medium leading-relaxed">
                {rec}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Visual Proficiency Comparison Radar / Bar Component */}
      <SkillGapRadar skillGaps={analysis.skillGaps} readinessScore={analysis.readinessScore} />
    </div>
  );
}
