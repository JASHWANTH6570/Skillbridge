import React from 'react';
import { CheckCircle2, AlertCircle, AlertTriangle } from 'lucide-react';

export default function SkillGapRadar({ skillGaps, readinessScore }) {
  if (!skillGaps || skillGaps.length === 0) {
    return (
      <div className="p-8 text-center text-slate-500 bg-white rounded-2xl border border-slate-200">
        No skill gap data available yet. Please complete a skill assessment.
      </div>
    );
  }

  return (
    <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-6 border-b border-slate-100">
        <div>
          <h3 className="text-lg font-bold text-slate-900">Skill Proficiency Comparison</h3>
          <p className="text-sm text-slate-500">
            Comparing your self-assessed levels against target role profile benchmarks (Scale: 1-5)
          </p>
        </div>

        <div className="flex items-center gap-3 bg-indigo-50/80 px-4 py-2.5 rounded-xl border border-indigo-100">
          <div className="text-right">
            <div className="text-xs font-semibold text-indigo-700 uppercase tracking-wider">
              Role Readiness
            </div>
            <div className="text-2xl font-extrabold text-indigo-950">
              {readinessScore}%
            </div>
          </div>
          <div className="w-12 h-12 rounded-full border-4 border-indigo-500 border-t-transparent animate-spin-slow flex items-center justify-center font-bold text-xs text-indigo-700">
            ★
          </div>
        </div>
      </div>

      <div className="mt-6 space-y-5">
        {skillGaps.map((item) => {
          const currentPct = (item.currentProficiency / 5) * 100;
          const reqPct = (item.requiredProficiency / 5) * 100;

          let badgeColor = 'bg-emerald-50 text-emerald-700 border-emerald-200';
          let StatusIcon = CheckCircle2;
          let label = 'Target Met';

          if (item.status === 'CRITICAL_GAP') {
            badgeColor = 'bg-rose-50 text-rose-700 border-rose-200';
            StatusIcon = AlertCircle;
            label = `-${item.gap} Level Gap (Critical)`;
          } else if (item.status === 'MODERATE_GAP') {
            badgeColor = 'bg-amber-50 text-amber-700 border-amber-200';
            StatusIcon = AlertTriangle;
            label = `-${item.gap} Level Gap`;
          }

          return (
            <div key={item.skillId} className="p-4 rounded-xl bg-slate-50 border border-slate-100 hover:border-indigo-200 transition-colors">
              <div className="flex flex-wrap items-center justify-between gap-2 mb-2">
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-slate-800 text-sm sm:text-base">
                    {item.skillName}
                  </span>
                  <span className="text-xs px-2 py-0.5 rounded-md bg-slate-200/60 text-slate-600 font-medium">
                    {item.category}
                  </span>
                  {item.importance === 'REQUIRED' && (
                    <span className="text-[10px] font-bold px-1.5 py-0.5 rounded bg-indigo-100 text-indigo-800">
                      CORE
                    </span>
                  )}
                </div>

                <div className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold border ${badgeColor}`}>
                  <StatusIcon className="w-3.5 h-3.5" />
                  <span>{label}</span>
                </div>
              </div>

              {/* Comparative Progress Bar */}
              <div className="relative pt-2 pb-1">
                {/* Background Track */}
                <div className="h-3 w-full bg-slate-200 rounded-full overflow-hidden relative">
                  {/* Target benchmark shadow marker */}
                  <div
                    className="absolute top-0 bottom-0 bg-indigo-200/80 rounded-full transition-all duration-500"
                    style={{ width: `${reqPct}%` }}
                    title={`Required Benchmark: ${item.requiredProficiency}/5`}
                  />
                  {/* Current student level fill */}
                  <div
                    className={`h-full rounded-full transition-all duration-700 ${
                      item.gap === 0 ? 'bg-emerald-500' : item.gap === 1 ? 'bg-amber-500' : 'bg-rose-500'
                    }`}
                    style={{ width: `${currentPct}%` }}
                    title={`Current Proficiency: ${item.currentProficiency}/5`}
                  />
                </div>

                <div className="flex justify-between text-xs text-slate-500 mt-2 font-medium">
                  <span>Current: <strong>{item.currentProficiency} / 5</strong></span>
                  <span className="text-indigo-700">Target Required: <strong>{item.requiredProficiency} / 5</strong></span>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
