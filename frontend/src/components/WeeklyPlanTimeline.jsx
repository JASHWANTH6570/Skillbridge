import React from 'react';
import MilestoneCard from './MilestoneCard';
import { Trophy, CheckCircle, Clock, Sparkles } from 'lucide-react';

export default function WeeklyPlanTimeline({ roadmap, onToggleMilestone, onUpdateMilestone, onRegenerate }) {
  if (!roadmap || !roadmap.milestones || roadmap.milestones.length === 0) {
    return (
      <div className="bg-white rounded-2xl p-12 text-center border border-slate-200">
        <Sparkles className="w-12 h-12 text-indigo-500 mx-auto mb-3" />
        <h3 className="text-lg font-bold text-slate-800">No Learning Roadmap Active</h3>
        <p className="text-sm text-slate-500 max-w-md mx-auto mt-1 mb-6">
          Perform a skill assessment and select your target role to generate your personalized step-by-step roadmap.
        </p>
        <button
          onClick={onRegenerate}
          className="px-5 py-2.5 bg-indigo-600 text-white rounded-xl font-semibold hover:bg-indigo-700 shadow-md shadow-indigo-500/20"
        >
          Generate Personalized Roadmap
        </button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Roadmap Summary Banner */}
      <div className="bg-gradient-to-br from-indigo-900 via-indigo-800 to-slate-900 rounded-2xl p-6 sm:p-8 text-white shadow-xl">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div>
            <span className="text-xs uppercase font-bold tracking-widest text-indigo-300">
              Active Learning Journey • {roadmap.roleTitle}
            </span>
            <h2 className="text-2xl sm:text-3xl font-extrabold mt-1">
              {roadmap.title}
            </h2>
            <p className="text-indigo-200 text-sm mt-2 max-w-2xl leading-relaxed">
              {roadmap.summary}
            </p>
          </div>

          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 sm:p-5 border border-white/10 flex items-center gap-5 shrink-0">
            <div>
              <div className="text-xs text-indigo-200 font-medium">Roadmap Progress</div>
              <div className="text-3xl font-black text-white mt-0.5">
                {roadmap.progressPercentage}%
              </div>
              <div className="text-xs text-indigo-300 mt-1">
                {roadmap.completedMilestones} of {roadmap.totalMilestones} completed
              </div>
            </div>

            <div className="w-16 h-16 rounded-full border-4 border-indigo-400/40 border-t-white flex items-center justify-center font-bold text-sm">
              {roadmap.progressPercentage === 100 ? (
                <Trophy className="w-7 h-7 text-amber-300 animate-bounce" />
              ) : (
                <Clock className="w-6 h-6 text-indigo-200" />
              )}
            </div>
          </div>
        </div>

        {/* Linear Progress Bar */}
        <div className="mt-6 pt-6 border-t border-indigo-700/50">
          <div className="h-2.5 w-full bg-indigo-950/70 rounded-full overflow-hidden p-0.5">
            <div
              className="h-full bg-gradient-to-r from-indigo-400 to-emerald-400 rounded-full transition-all duration-700 shadow-sm"
              style={{ width: `${roadmap.progressPercentage}%` }}
            />
          </div>
        </div>
      </div>

      {/* Milestones List */}
      <div className="space-y-4">
        <div className="flex items-center justify-between px-1">
          <h3 className="text-lg font-bold text-slate-900 flex items-center gap-2">
            Weekly Milestones & Hands-On Deliverables
          </h3>
          <span className="text-xs font-semibold text-slate-500">
            Click checkboxes to track growth
          </span>
        </div>

        {roadmap.milestones.map((milestone) => (
          <MilestoneCard
            key={milestone.id}
            milestone={milestone}
            onToggle={onToggleMilestone}
            onUpdate={onUpdateMilestone}
          />
        ))}
      </div>
    </div>
  );
}
