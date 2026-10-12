import React, { useState, useEffect } from 'react';
import { api } from '../api/client';
import { 
  ShieldCheck, 
  Users, 
  BookOpen, 
  BarChart2, 
  Trash2, 
  Plus, 
  ExternalLink, 
  CheckCircle2, 
  Trophy 
} from 'lucide-react';

export default function AdminConsolePage() {
  const [stats, setStats] = useState(null);
  const [users, setUsers] = useState([]);
  const [resources, setResources] = useState([]);
  const [activeSubTab, setActiveSubTab] = useState('overview'); // overview, resources, users
  const [loading, setLoading] = useState(true);

  // New Resource Form
  const [newResource, setNewResource] = useState({
    title: '',
    type: 'COURSE',
    url: '',
    provider: '',
    durationMinutes: 180,
    difficulty: 'INTERMEDIATE',
    skillName: 'Spring Boot'
  });

  useEffect(() => {
    loadAllAdminData();
  }, []);

  const loadAllAdminData = async () => {
    setLoading(true);
    try {
      const [s, u, r] = await Promise.all([
        api.getAdminStats().catch(() => null),
        api.getAdminUsers().catch(() => []),
        api.getAdminResources().catch(() => [])
      ]);
      setStats(s);
      setUsers(u);
      setResources(r);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateResource = async (e) => {
    e.preventDefault();
    try {
      await api.createAdminResource(newResource);
      setNewResource({
        title: '',
        type: 'COURSE',
        url: '',
        provider: '',
        durationMinutes: 180,
        difficulty: 'INTERMEDIATE',
        skillName: 'Spring Boot'
      });
      const updated = await api.getAdminResources();
      setResources(updated);
      alert('Resource added to curated catalog!');
    } catch (err) {
      alert('Failed to add resource: ' + err.message);
    }
  };

  const handleDeleteResource = async (id) => {
    if (!confirm('Are you sure you want to remove this resource from the curated catalog?')) return;
    try {
      await api.deleteAdminResource(id);
      setResources(resources.filter(r => r.id !== id));
    } catch (err) {
      alert('Failed to delete resource: ' + err.message);
    }
  };

  if (loading) {
    return <div className="p-12 text-center text-slate-500">Loading administrator console...</div>;
  }

  return (
    <div className="max-w-6xl mx-auto space-y-6">
      {/* Header */}
      <div className="bg-white rounded-2xl p-6 sm:p-8 border border-slate-200 shadow-sm flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2 text-indigo-600 font-bold text-xs uppercase tracking-wider mb-1">
            <ShieldCheck className="w-4 h-4" />
            Staff Administration & Program Oversight
          </div>
          <h2 className="text-2xl font-black text-slate-900">Administrator Console</h2>
          <p className="text-sm text-slate-500 mt-1">
            Manage users, role curricula, approved learning resources, and monitor cohort completion analytics.
          </p>
        </div>

        {/* Sub Navigation */}
        <div className="flex items-center gap-1 bg-slate-100 p-1.5 rounded-xl self-start sm:self-auto">
          <button
            onClick={() => setActiveSubTab('overview')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
              activeSubTab === 'overview'
                ? 'bg-white text-indigo-700 shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Metrics & Reports
          </button>
          <button
            onClick={() => setActiveSubTab('resources')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
              activeSubTab === 'resources'
                ? 'bg-white text-indigo-700 shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Curated Resources
          </button>
          <button
            onClick={() => setActiveSubTab('users')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
              activeSubTab === 'users'
                ? 'bg-white text-indigo-700 shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Students ({users.length})
          </button>
        </div>
      </div>

      {/* Overview / Reports Tab */}
      {activeSubTab === 'overview' && stats && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
              <span className="text-xs font-semibold text-slate-500 uppercase">Enrolled Students</span>
              <div className="text-2xl sm:text-3xl font-black text-slate-900 mt-1">
                {stats.totalStudents}
              </div>
              <span className="text-[11px] text-slate-400">Total active accounts</span>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
              <span className="text-xs font-semibold text-slate-500 uppercase">Generated Roadmaps</span>
              <div className="text-2xl sm:text-3xl font-black text-indigo-600 mt-1">
                {stats.totalRoadmaps}
              </div>
              <span className="text-[11px] text-indigo-600 font-medium">Personalized plans</span>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
              <span className="text-xs font-semibold text-slate-500 uppercase">Milestones Completed</span>
              <div className="text-2xl sm:text-3xl font-black text-emerald-600 mt-1">
                {stats.completedMilestones} / {stats.totalMilestones}
              </div>
              <span className="text-[11px] text-emerald-700 font-medium">Verified deliverables</span>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
              <span className="text-xs font-semibold text-slate-500 uppercase">Completion Rate</span>
              <div className="text-2xl sm:text-3xl font-black text-violet-600 mt-1">
                {stats.overallCompletionRate}%
              </div>
              <span className="text-[11px] text-violet-700 font-medium">Success metric</span>
            </div>
          </div>

          {/* Students Per Role Distribution */}
          <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
            <h3 className="text-base font-bold text-slate-900 mb-4">
              Student Enrollment by Target Career Role
            </h3>
            {stats.studentsPerRole && Object.keys(stats.studentsPerRole).length > 0 ? (
              <div className="space-y-3">
                {Object.entries(stats.studentsPerRole).map(([roleTitle, count]) => (
                  <div key={roleTitle} className="p-3.5 rounded-xl bg-slate-50 border border-slate-100 flex items-center justify-between">
                    <span className="font-semibold text-sm text-slate-800">{roleTitle}</span>
                    <span className="px-3 py-1 bg-indigo-50 text-indigo-700 font-bold text-xs rounded-full">
                      {count} {count === 1 ? 'Student' : 'Students'}
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-sm text-slate-500 italic">No role distribution data recorded yet.</p>
            )}
          </div>
        </div>
      )}

      {/* Resources Management Tab */}
      {activeSubTab === 'resources' && (
        <div className="space-y-6">
          {/* Add Resource Card */}
          <div className="bg-white rounded-2xl p-6 border border-slate-200 shadow-sm">
            <h3 className="text-base font-bold text-slate-900 mb-4 flex items-center gap-2">
              <Plus className="w-4 h-4 text-indigo-600" />
              Add Approved Curated Resource
            </h3>
            <form onSubmit={handleCreateResource} className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-700 mb-1">Title</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Master Spring Data JPA Repositories"
                  value={newResource.title}
                  onChange={(e) => setNewResource({ ...newResource, title: e.target.value })}
                  className="w-full text-xs px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Associated Skill</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Spring Boot"
                  value={newResource.skillName}
                  onChange={(e) => setNewResource({ ...newResource, skillName: e.target.value })}
                  className="w-full text-xs px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Resource URL</label>
                <input
                  type="url"
                  required
                  placeholder="https://..."
                  value={newResource.url}
                  onChange={(e) => setNewResource({ ...newResource, url: e.target.value })}
                  className="w-full text-xs px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Provider / Platform</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Spring.io, Coursera"
                  value={newResource.provider}
                  onChange={(e) => setNewResource({ ...newResource, provider: e.target.value })}
                  className="w-full text-xs px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div className="flex items-end">
                <button
                  type="submit"
                  className="w-full py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-xs rounded-lg shadow-sm"
                >
                  Save Resource
                </button>
              </div>
            </form>
          </div>

          {/* Resources List */}
          <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm">
            <div className="p-4 bg-slate-50 border-b border-slate-200 font-bold text-xs uppercase tracking-wider text-slate-600">
              Curated Materials Catalog ({resources.length})
            </div>
            <div className="divide-y divide-slate-100">
              {resources.map((res) => (
                <div key={res.id} className="p-4 flex items-center justify-between gap-4 hover:bg-slate-50/50">
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="font-bold text-sm text-slate-900">{res.title}</span>
                      <span className="text-[10px] font-semibold px-2 py-0.5 rounded bg-indigo-50 text-indigo-700">
                        {res.skillName}
                      </span>
                    </div>
                    <div className="text-xs text-slate-500 mt-0.5">
                      {res.provider} • {res.type} • {res.difficulty}
                    </div>
                  </div>

                  <div className="flex items-center gap-3 shrink-0">
                    <a
                      href={res.url}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-xs text-indigo-600 hover:underline flex items-center gap-1 font-semibold"
                    >
                      Visit <ExternalLink className="w-3 h-3" />
                    </a>
                    <button
                      onClick={() => handleDeleteResource(res.id)}
                      className="p-1.5 text-slate-400 hover:text-rose-600 rounded-lg hover:bg-rose-50"
                      title="Delete Resource"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* Users Tab */}
      {activeSubTab === 'users' && (
        <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm">
          <div className="p-4 bg-slate-50 border-b border-slate-200 font-bold text-xs uppercase tracking-wider text-slate-600">
            Registered Student & Staff Accounts
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50/80 text-slate-500 uppercase font-semibold border-b border-slate-100">
                <tr>
                  <th className="p-4">User</th>
                  <th className="p-4">Email</th>
                  <th className="p-4">Role</th>
                  <th className="p-4">Created Date</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {users.map((u) => (
                  <tr key={u.id} className="hover:bg-slate-50/50">
                    <td className="p-4 font-bold text-slate-900">
                      {u.fullName || u.username}
                      <span className="block text-[11px] font-normal text-slate-400">@{u.username}</span>
                    </td>
                    <td className="p-4 text-slate-600">{u.email}</td>
                    <td className="p-4">
                      <span className={`px-2 py-0.5 rounded-full font-bold text-[10px] ${
                        u.role === 'ROLE_ADMIN' ? 'bg-amber-100 text-amber-800' : 'bg-indigo-100 text-indigo-800'
                      }`}>
                        {u.role}
                      </span>
                    </td>
                    <td className="p-4 text-slate-500">
                      {u.createdAt ? new Date(u.createdAt).toLocaleDateString() : 'Active'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
