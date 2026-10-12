import React, { useState } from 'react';
import { 
  CheckCircle2, 
  Circle, 
  FolderGit2, 
  BookOpen, 
  Edit3, 
  Calendar, 
  ChevronDown, 
  ChevronUp, 
  ExternalLink,
  Save,
  X
} from 'lucide-react';

export default function MilestoneCard({ milestone, onToggle, onUpdate }) {
  const [expanded, setExpanded] = useState(true);
  const [editing, setEditing] = useState(false);
  const [editForm, setEditForm] = useState({
    title: milestone.title,
    description: milestone.description,
    focusSkill: milestone.focusSkill,
    portfolioProject: milestone.portfolioProject,
    learningOutcome: milestone.learningOutcome,
    resourceLinks: milestone.resourceLinks,
  });

  const handleSave = async (e) => {
    e.preventDefault();
    await onUpdate(milestone.id, editForm);
    setEditing(false);
  };

  const parseLinks = (linksStr) => {
    if (!linksStr) return [];
    return linksStr.split(';').map(part => part.trim()).filter(Boolean);
  };

  return (
    <div 
      className={`rounded-2xl border transition-all duration-300 ${
        milestone.completed 
          ? 'bg-emerald-50/40 border-emerald-200/80 shadow-sm' 
          : 'bg-white border-slate-200 shadow-sm hover:border-indigo-300'
      }`}
    >
      {/* Header bar */}
      <div className="p-5 flex items-start justify-between gap-4">
        <div className="flex items-start gap-4">
          <button
            onClick={() => onToggle(milestone.id)}
            title={milestone.completed ? 'Mark Incomplete' : 'Mark Completed'}
            className="mt-0.5 text-slate-400 hover:scale-110 active:scale-95 transition-transform"
          >
            {milestone.completed ? (
              <CheckCircle2 className="w-6 h-6 text-emerald-600 fill-emerald-100" />
            ) : (
              <Circle className="w-6 h-6 text-slate-400 hover:text-indigo-600" />
            )}
          </button>

          <div>
            <div className="flex flex-wrap items-center gap-2 mb-1.5">
              <span className="inline-flex items-center gap-1 text-xs font-bold px-2.5 py-0.5 rounded-full bg-indigo-100 text-indigo-800">
                <Calendar className="w-3 h-3" />
                Week {milestone.weekNumber}
              </span>

              {milestone.focusSkill && (
                <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-slate-100 text-slate-700">
                  {milestone.focusSkill}
                </span>
              )}

              {milestone.completed && (
                <span className="text-xs font-bold px-2.5 py-0.5 rounded-full bg-emerald-100 text-emerald-800">
                  Completed
                </span>
              )}
            </div>

            <h4 className={`text-base sm:text-lg font-bold ${milestone.completed ? 'text-emerald-950 line-through decoration-emerald-500/50' : 'text-slate-900'}`}>
              {milestone.title}
            </h4>

            <p className="text-sm text-slate-600 mt-1 leading-relaxed">
              {milestone.description}
            </p>
          </div>
        </div>

        {/* Action Controls */}
        <div className="flex items-center gap-1 shrink-0">
          <button
            onClick={() => setEditing(!editing)}
            className="p-2 text-slate-400 hover:text-indigo-600 hover:bg-slate-100 rounded-lg transition-colors"
            title="Edit Milestone Details"
          >
            <Edit3 className="w-4 h-4" />
          </button>
          <button
            onClick={() => setExpanded(!expanded)}
            className="p-2 text-slate-400 hover:text-slate-700 hover:bg-slate-100 rounded-lg transition-colors"
          >
            {expanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
          </button>
        </div>
      </div>

      {/* Edit Form */}
      {editing && (
        <form onSubmit={handleSave} className="p-5 border-t border-slate-200 bg-slate-50/80 rounded-b-2xl space-y-4">
          <div className="flex items-center justify-between pb-2 border-b border-slate-200">
            <h5 className="font-bold text-sm text-slate-800">Edit Milestone Plan</h5>
            <button
              type="button"
              onClick={() => setEditing(false)}
              className="text-slate-400 hover:text-slate-600"
            >
              <X className="w-4 h-4" />
            </button>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">Title</label>
            <input
              type="text"
              value={editForm.title}
              onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
              className="w-full text-sm px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
              required
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">Description</label>
            <textarea
              rows="2"
              value={editForm.description}
              onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
              className="w-full text-sm px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            />
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Focus Skill</label>
              <input
                type="text"
                value={editForm.focusSkill}
                onChange={(e) => setEditForm({ ...editForm, focusSkill: e.target.value })}
                className="w-full text-sm px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Portfolio Deliverable</label>
              <input
                type="text"
                value={editForm.portfolioProject}
                onChange={(e) => setEditForm({ ...editForm, portfolioProject: e.target.value })}
                className="w-full text-sm px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">Curated Resources (Semicolon separated)</label>
            <input
              type="text"
              value={editForm.resourceLinks}
              onChange={(e) => setEditForm({ ...editForm, resourceLinks: e.target.value })}
              className="w-full text-sm px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            />
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button
              type="button"
              onClick={() => setEditing(false)}
              className="px-3 py-1.5 text-xs font-medium text-slate-600 hover:bg-slate-200 rounded-lg"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex items-center gap-1.5 px-4 py-1.5 text-xs font-semibold bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
            >
              <Save className="w-3.5 h-3.5" />
              Save Changes
            </button>
          </div>
        </form>
      )}

      {/* Expanded Content: Portfolio deliverables & resources */}
      {expanded && !editing && (
        <div className="px-5 pb-5 pt-1 space-y-4 border-t border-slate-100">
          {/* Portfolio project deliverable */}
          {milestone.portfolioProject && (
            <div className="p-3.5 rounded-xl bg-violet-50/70 border border-violet-100 flex items-start gap-3">
              <div className="p-1.5 rounded-lg bg-violet-600 text-white shrink-0 mt-0.5">
                <FolderGit2 className="w-4 h-4" />
              </div>
              <div>
                <span className="block text-xs font-bold uppercase tracking-wider text-violet-800">
                  Portfolio Project Deliverable
                </span>
                <p className="text-sm font-medium text-violet-950 mt-0.5">
                  {milestone.portfolioProject}
                </p>
              </div>
            </div>
          )}

          {/* Curated Resources */}
          {milestone.resourceLinks && (
            <div className="space-y-2">
              <span className="flex items-center gap-1.5 text-xs font-bold uppercase tracking-wider text-slate-600">
                <BookOpen className="w-3.5 h-3.5 text-indigo-600" />
                Approved Curated Learning Materials
              </span>
              <div className="grid grid-cols-1 gap-2">
                {parseLinks(milestone.resourceLinks).map((link, idx) => (
                  <div 
                    key={idx}
                    className="flex items-center justify-between p-2.5 bg-slate-50 rounded-lg border border-slate-200/70 text-xs font-medium text-slate-700 hover:bg-slate-100 transition-colors"
                  >
                    <span className="truncate pr-2">{link}</span>
                    <a
                      href={link.includes('http') ? link.substring(link.indexOf('http')) : '#'}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-indigo-600 hover:text-indigo-800 shrink-0 flex items-center gap-1 font-semibold"
                    >
                      Open <ExternalLink className="w-3 h-3" />
                    </a>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
