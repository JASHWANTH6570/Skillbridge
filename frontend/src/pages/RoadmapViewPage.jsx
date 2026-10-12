import React, { useState, useEffect } from 'react';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import WeeklyPlanTimeline from '../components/WeeklyPlanTimeline';
import { RefreshCw, MapPin } from 'lucide-react';

export default function RoadmapViewPage({ onSwitchToAssessment }) {
  const { user } = useAuth();
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadRoadmap();
  }, []);

  const loadRoadmap = async () => {
    setLoading(true);
    try {
      const data = await api.getActiveRoadmap();
      if (data && data.id) {
        setRoadmap(data);
      } else {
        setRoadmap(null);
      }
    } catch (err) {
      console.error(err);
      setRoadmap(null);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleMilestone = async (milestoneId) => {
    try {
      const updated = await api.toggleMilestone(milestoneId);
      setRoadmap(updated);
    } catch (err) {
      alert('Error updating milestone: ' + err.message);
    }
  };

  const handleUpdateMilestone = async (milestoneId, editData) => {
    try {
      const updatedMilestone = await api.updateMilestone(milestoneId, editData);
      setRoadmap((prev) => {
        if (!prev) return prev;
        const newMilestones = prev.milestones.map((m) =>
          m.id === milestoneId ? { ...m, ...updatedMilestone } : m
        );
        return { ...prev, milestones: newMilestones };
      });
    } catch (err) {
      alert('Error saving milestone edit: ' + err.message);
    }
  };

  const handleRegenerate = async () => {
    try {
      const roleId = user?.targetRoleId || 1;
      const newRoadmap = await api.generateRoadmap(roleId);
      setRoadmap(newRoadmap);
    } catch (err) {
      alert('Error generating roadmap: ' + err.message);
    }
  };

  if (loading) {
    return <div className="p-12 text-center text-slate-500">Loading personalized learning roadmap...</div>;
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <MapPin className="w-5 h-5 text-indigo-600" />
          <h2 className="text-xl font-bold text-slate-900">Your Structured Journey</h2>
        </div>

        {roadmap && (
          <button
            onClick={handleRegenerate}
            className="flex items-center gap-1.5 px-3.5 py-2 text-xs font-semibold text-indigo-700 bg-indigo-50 hover:bg-indigo-100 rounded-xl transition-colors"
          >
            <RefreshCw className="w-3.5 h-3.5" />
            <span>Recalculate Roadmap</span>
          </button>
        )}
      </div>

      <WeeklyPlanTimeline
        roadmap={roadmap}
        onToggleMilestone={handleToggleMilestone}
        onUpdateMilestone={handleUpdateMilestone}
        onRegenerate={handleRegenerate}
      />
    </div>
  );
}
