import React, { useState, useEffect } from 'react';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { Compass, CheckCircle, ArrowRight, Briefcase, Award } from 'lucide-react';

export default function TargetRoleSelectorPage({ onRoleSelected }) {
  const { user, updateUserTargetRole } = useAuth();
  const [roles, setRoles] = useState([]);
  const [selectedId, setSelectedId] = useState(user?.targetRoleId || null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadRoles() {
      try {
        const data = await api.getRoles();
        setRoles(data);
        if (!selectedId && data.length > 0) {
          setSelectedId(data[0].id);
        }
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    loadRoles();
  }, []);

  const handleConfirmRole = (roleId) => {
    setSelectedId(roleId);
    updateUserTargetRole(roleId);
    if (onRoleSelected) {
      onRoleSelected(roleId);
    }
  };

  if (loading) {
    return <div className="p-12 text-center text-slate-500">Loading defined career roles...</div>;
  }

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="bg-white rounded-2xl p-8 border border-slate-200 shadow-sm text-center max-w-3xl mx-auto">
        <div className="w-12 h-12 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center mx-auto mb-3 shadow-inner">
          <Compass className="w-6 h-6" />
        </div>
        <h2 className="text-2xl font-black text-slate-900">Define Your Target Career Goal</h2>
        <p className="text-slate-600 text-sm mt-2 max-w-xl mx-auto leading-relaxed">
          SkillBridge analyzes industry expectations for your chosen role to benchmark your current skills and build your personalized learning roadmap.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {roles.map((role) => {
          const isSelected = selectedId === role.id;
          return (
            <div
              key={role.id}
              onClick={() => setSelectedId(role.id)}
              className={`rounded-2xl p-6 border-2 transition-all cursor-pointer relative flex flex-col justify-between ${
                isSelected
                  ? 'bg-indigo-50/50 border-indigo-600 shadow-lg shadow-indigo-500/10 scale-[1.02]'
                  : 'bg-white border-slate-200 hover:border-slate-300 shadow-sm'
              }`}
            >
              <div>
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-bold uppercase tracking-wider px-2.5 py-1 rounded-full bg-slate-100 text-slate-600 flex items-center gap-1">
                    <Briefcase className="w-3 h-3" />
                    {role.department}
                  </span>
                  {isSelected && (
                    <CheckCircle className="w-5 h-5 text-indigo-600 fill-indigo-100" />
                  )}
                </div>

                <h3 className="text-lg font-bold text-slate-900 leading-snug">
                  {role.title}
                </h3>

                <p className="text-xs text-indigo-700 font-semibold mt-1 flex items-center gap-1">
                  <Award className="w-3.5 h-3.5" />
                  {role.experienceLevel}
                </p>

                <p className="text-xs text-slate-600 mt-3 leading-relaxed">
                  {role.description}
                </p>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-500">
                  {role.skillRequirements?.length || 5} Required Skills
                </span>
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    handleConfirmRole(role.id);
                  }}
                  className={`px-3.5 py-1.5 rounded-lg text-xs font-bold transition-all flex items-center gap-1 ${
                    isSelected
                      ? 'bg-indigo-600 text-white hover:bg-indigo-700'
                      : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                  }`}
                >
                  <span>{isSelected ? 'Selected' : 'Choose Role'}</span>
                  <ArrowRight className="w-3 h-3" />
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
