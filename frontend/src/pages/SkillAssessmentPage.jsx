import React, { useState, useEffect } from 'react';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { CheckSquare, Save, ArrowRight, Star } from 'lucide-react';

export default function SkillAssessmentPage({ onAssessmentComplete }) {
  const { user } = useAuth();
  const [skills, setSkills] = useState([]);
  const [ratings, setRatings] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const proficiencyLabels = {
    1: '1 - Novice (Theoretical knowledge only)',
    2: '2 - Advanced Beginner (Built toy examples)',
    3: '3 - Competent (Can build real features)',
    4: '4 - Proficient (Production debugging & best practices)',
    5: '5 - Expert (Architecture, optimization & mentoring)'
  };

  useEffect(() => {
    async function loadData() {
      try {
        const allSkills = await api.getSkills();
        setSkills(allSkills);

        const currentSkills = await api.getMySkills().catch(() => []);
        const ratingMap = {};
        allSkills.forEach(s => {
          ratingMap[s.id] = 1;
        });
        currentSkills.forEach(cs => {
          ratingMap[cs.skill.id] = cs.proficiencyLevel;
        });
        setRatings(ratingMap);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  const handleRatingChange = (skillId, level) => {
    setRatings(prev => ({
      ...prev,
      [skillId]: level,
    }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const payload = Object.entries(ratings).map(([skillId, proficiencyLevel]) => ({
        skillId: Number(skillId),
        proficiencyLevel,
      }));
      await api.saveAssessment(user?.targetRoleId || 1, payload);
      if (onAssessmentComplete) {
        onAssessmentComplete();
      }
    } catch (err) {
      alert('Failed to save assessment: ' + err.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div className="p-12 text-center text-slate-500">Loading skill assessment rubric...</div>;
  }

  // Group by category
  const categories = {};
  skills.forEach(s => {
    const cat = s.category || 'General';
    if (!categories[cat]) categories[cat] = [];
    categories[cat].push(s);
  });

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="bg-white rounded-2xl p-6 sm:p-8 border border-slate-200 shadow-sm">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-6 border-b border-slate-100">
          <div>
            <div className="flex items-center gap-2 text-indigo-600 font-bold text-xs uppercase tracking-wider mb-1">
              <CheckSquare className="w-4 h-4" />
              Self-Assessment Rubric
            </div>
            <h2 className="text-2xl font-black text-slate-900">Record Your Current Skill Proficiencies</h2>
            <p className="text-sm text-slate-500 mt-1">
              Be honest in your ratings. SkillBridge uses these levels to generate your precise gap breakdown and weekly study plan.
            </p>
          </div>

          <button
            onClick={handleSave}
            disabled={saving}
            className="flex items-center gap-2 px-5 py-3 rounded-xl bg-indigo-600 text-white font-bold text-sm hover:bg-indigo-700 shadow-md shadow-indigo-500/20 shrink-0 self-start sm:self-auto"
          >
            <Save className="w-4 h-4" />
            <span>{saving ? 'Evaluating Gaps...' : 'Save & Analyze Gaps'}</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </div>

        <form onSubmit={handleSave} className="mt-8 space-y-8">
          {Object.entries(categories).map(([category, items]) => (
            <div key={category} className="space-y-4">
              <h3 className="text-sm font-bold uppercase tracking-wider text-slate-500 border-b border-slate-100 pb-2">
                {category} Competencies
              </h3>

              <div className="grid grid-cols-1 gap-4">
                {items.map((skill) => {
                  const currentLevel = ratings[skill.id] || 1;
                  return (
                    <div
                      key={skill.id}
                      className="p-4 rounded-xl bg-slate-50 border border-slate-200/80 hover:border-indigo-200 transition-colors"
                    >
                      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-2">
                        <div>
                          <h4 className="font-bold text-slate-900 text-sm sm:text-base">
                            {skill.name}
                          </h4>
                          <p className="text-xs text-slate-500 mt-0.5">
                            {skill.description}
                          </p>
                        </div>

                        {/* 1 to 5 Star / Numeric Selector */}
                        <div className="flex items-center gap-1.5 shrink-0 bg-white px-3 py-1.5 rounded-xl border border-slate-200">
                          {[1, 2, 3, 4, 5].map((lvl) => (
                            <button
                              key={lvl}
                              type="button"
                              onClick={() => handleRatingChange(skill.id, lvl)}
                              title={proficiencyLabels[lvl]}
                              className={`w-7 h-7 rounded-lg text-xs font-bold transition-all ${
                                currentLevel >= lvl
                                  ? 'bg-indigo-600 text-white shadow-xs'
                                  : 'bg-slate-100 text-slate-500 hover:bg-slate-200'
                              }`}
                            >
                              {lvl}
                            </button>
                          ))}
                        </div>
                      </div>

                      <div className="text-right text-[11px] font-medium text-slate-500">
                        {proficiencyLabels[currentLevel]}
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          ))}

          <div className="pt-4 flex justify-end">
            <button
              type="submit"
              disabled={saving}
              className="flex items-center gap-2 px-6 py-3 rounded-xl bg-indigo-600 text-white font-bold text-sm hover:bg-indigo-700 shadow-md shadow-indigo-500/20"
            >
              <Save className="w-4 h-4" />
              <span>{saving ? 'Evaluating Gaps...' : 'Save & Analyze Gaps'}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
