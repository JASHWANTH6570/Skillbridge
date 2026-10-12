import React, { useState, useEffect } from 'react';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { 
  Compass, 
  MapPin, 
  CheckSquare, 
  Sparkles, 
  ArrowRight, 
  TrendingUp, 
  FolderGit2, 
  CheckCircle2, 
  Calendar 
} from 'lucide-react';

export default function DashboardPage({ setActiveTab }) {
  const { user } = useAuth();
  const [role, setRole] = useState(null);
  const [roadmap, setRoadmap] = useState(null);
  const [gapAnalysis, setGapAnalysis] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadDashboard() {
      try {
        const roleId = user?.targetRoleId || 1;
        const [roleData, roadmapData, gapData] = await Promise.all([
          api.getRole(roleId).catch(() => null),
          api.getActiveRoadmap().catch(() => null),
          api.getGapAnalysis(roleId).catch(() => null),
        ]);

        setRole(roleData);
        if (roadmapData && roadmapData.id) {
          setRoadmap(roadmapData);
        }
        setGapAnalysis(gapData);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    loadDashboard();
  }, [user]);

  if (loading) {
    return <div className="p-12 text-center text-slate-500">Loading student learning dashboard...</div>;
  }

  const nextMilestone = roadmap?.milestones?.find(m => !m.completed);

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      {/* Welcome Banner */}
      <div className="bg-gradient-to-r from-indigo-900 via-indigo-800 to-slate-900 rounded-3xl p-6 sm:p-8 text-white shadow-xl">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div>
            <span className="text-xs font-bold uppercase tracking-wider text-indigo-300">
              Student Career Workspace
            </span>
            <h1 className="text-2xl sm:text-3xl font-black mt-1">
              Welcome back, {user?.fullName || user?.username}!
            </h1>
            <p className="text-indigo-200 text-sm mt-2 max-w-xl leading-relaxed">
              Targeting <span className="text-white font-bold underline decoration-indigo-400 underline-offset-4">{role?.title || 'Full Stack Developer'}</span>. Follow your structured weekly milestones to bridge your skill gaps and build practical portfolio projects.
            </p>
          </div>

          <div className="flex gap-3">
            <button
              onClick={() => setActiveTab('roadmap')}
              className="px-5 py-2.5 bg-white text-indigo-900 font-bold text-xs sm:text-sm rounded-xl hover:bg-indigo-50 shadow-md shadow-black/10 transition-transform active:scale-95"
            >
              Resume Learning
            </button>
            <button
              onClick={() => setActiveTab('ai-assistant')}
              className="px-4 py-2.5 bg-indigo-700/60 hover:bg-indigo-700 text-white font-semibold text-xs sm:text-sm rounded-xl border border-indigo-400/30 flex items-center gap-1.5 transition-colors"
            >
              <Sparkles className="w-4 h-4" />
              <span>Ask AI</span>
            </button>
          </div>
        </div>
      </div>

      {/* Primary KPI Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        {/* Target Role Card */}
        <div 
          onClick={() => setActiveTab('target-role')}
          className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm hover:border-indigo-300 transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">Target Goal</span>
            <div className="p-2 rounded-xl bg-indigo-50 text-indigo-600 group-hover:scale-110 transition-transform">
              <Compass className="w-5 h-5" />
            </div>
          </div>
          <div className="font-extrabold text-slate-900 text-base line-clamp-1">
            {role?.title || 'Full Stack Developer'}
          </div>
          <div className="text-xs text-indigo-600 font-semibold mt-1">
            {role?.department || 'Engineering'} • {role?.experienceLevel || 'Entry-Mid'}
          </div>
          <div className="mt-4 pt-3 border-t border-slate-100 flex items-center justify-between text-xs font-semibold text-slate-500">
            <span>Change Target Role</span>
            <ArrowRight className="w-3.5 h-3.5 text-indigo-600" />
          </div>
        </div>

        {/* Readiness Score */}
        <div 
          onClick={() => setActiveTab('gap-analysis')}
          className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm hover:border-indigo-300 transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">Role Readiness</span>
            <div className="p-2 rounded-xl bg-emerald-50 text-emerald-600 group-hover:scale-110 transition-transform">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <div className="text-3xl font-black text-slate-900">
            {gapAnalysis ? `${gapAnalysis.readinessScore}%` : 'N/A'}
          </div>
          <div className="text-xs text-slate-500 mt-1">
            {gapAnalysis ? `${gapAnalysis.skillsMetCount} of ${gapAnalysis.totalRequiredSkills} competencies met` : 'Complete assessment to compute'}
          </div>
          <div className="mt-4 pt-3 border-t border-slate-100 flex items-center justify-between text-xs font-semibold text-slate-500">
            <span>Review Gap Analysis</span>
            <ArrowRight className="w-3.5 h-3.5 text-indigo-600" />
          </div>
        </div>

        {/* Roadmap Progress */}
        <div 
          onClick={() => setActiveTab('roadmap')}
          className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm hover:border-indigo-300 transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">Milestone Progress</span>
            <div className="p-2 rounded-xl bg-violet-50 text-violet-600 group-hover:scale-110 transition-transform">
              <MapPin className="w-5 h-5" />
            </div>
          </div>
          <div className="text-3xl font-black text-slate-900">
            {roadmap ? `${roadmap.progressPercentage}%` : '0%'}
          </div>
          <div className="text-xs text-slate-500 mt-1">
            {roadmap ? `${roadmap.completedMilestones} of ${roadmap.totalMilestones} milestones completed` : 'No active roadmap generated yet'}
          </div>
          <div className="mt-4 pt-3 border-t border-slate-100 flex items-center justify-between text-xs font-semibold text-slate-500">
            <span>Open Learning Roadmap</span>
            <ArrowRight className="w-3.5 h-3.5 text-indigo-600" />
          </div>
        </div>
      </div>

      {/* Up Next: Next Pending Milestone */}
      {nextMilestone ? (
        <div className="bg-white rounded-2xl p-6 border border-indigo-100 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-indigo-600 animate-pulse" />
              <h3 className="font-bold text-slate-900 text-sm sm:text-base">
                Current Learning Focus: Week {nextMilestone.weekNumber}
              </h3>
            </div>
            <button
              onClick={() => setActiveTab('roadmap')}
              className="text-xs font-bold text-indigo-600 hover:text-indigo-800 flex items-center gap-1"
            >
              Go to Milestone <ArrowRight className="w-3 h-3" />
            </button>
          </div>

          <div className="p-4 rounded-xl bg-slate-50 border border-slate-200/80">
            <h4 className="font-bold text-slate-900 text-base">
              {nextMilestone.title}
            </h4>
            <p className="text-xs text-slate-600 mt-1 leading-relaxed">
              {nextMilestone.description}
            </p>

            {nextMilestone.portfolioProject && (
              <div className="mt-3 p-3 rounded-lg bg-indigo-50/70 border border-indigo-100 flex items-center gap-2 text-xs font-medium text-indigo-950">
                <FolderGit2 className="w-4 h-4 text-indigo-600 shrink-0" />
                <span><strong>Deliverable:</strong> {nextMilestone.portfolioProject}</span>
              </div>
            )}
          </div>
        </div>
      ) : (
        <div className="bg-white rounded-2xl p-6 border border-slate-200 text-center">
          <p className="text-sm text-slate-500 mb-3">
            Ready to structure your learning path? Generate a personalized roadmap tailored to your gaps.
          </p>
          <button
            onClick={() => setActiveTab('assessment')}
            className="px-4 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold hover:bg-indigo-700"
          >
            Start Skill Assessment
          </button>
        </div>
      )}
    </div>
  );
}
