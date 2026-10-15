import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import TargetRoleSelectorPage from './pages/TargetRoleSelectorPage';
import SkillAssessmentPage from './pages/SkillAssessmentPage';
import SkillGapAnalysisPage from './pages/SkillGapAnalysisPage';
import RoadmapViewPage from './pages/RoadmapViewPage';
import AiCareerAssistantPage from './pages/AiCareerAssistantPage';

export default function App() {
  const { user, loading } = useAuth();
  const [authView, setAuthView] = useState('login'); // 'login' or 'register'
  const [activeTab, setActiveTab] = useState('dashboard');

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">
        <div className="flex flex-col items-center gap-3">
          <div className="w-8 h-8 border-4 border-indigo-500 border-t-transparent rounded-full animate-spin" />
          <span className="text-sm font-semibold text-indigo-200">Loading SkillBridge...</span>
        </div>
      </div>
    );
  }

  if (!user) {
    if (authView === 'register') {
      return <RegisterPage onSwitchToLogin={() => setAuthView('login')} />;
    }
    return <LoginPage onSwitchToRegister={() => setAuthView('register')} />;
  }

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {activeTab === 'dashboard' && (
          <DashboardPage setActiveTab={setActiveTab} />
        )}

        {activeTab === 'target-role' && (
          <TargetRoleSelectorPage
            onRoleSelected={() => setActiveTab('assessment')}
          />
        )}

        {activeTab === 'assessment' && (
          <SkillAssessmentPage
            onAssessmentComplete={() => setActiveTab('gap-analysis')}
          />
        )}

        {activeTab === 'gap-analysis' && (
          <SkillGapAnalysisPage
            onGenerateRoadmap={() => setActiveTab('roadmap')}
          />
        )}

        {activeTab === 'roadmap' && (
          <RoadmapViewPage
            onSwitchToAssessment={() => setActiveTab('assessment')}
          />
        )}

        {activeTab === 'ai-assistant' && (
          <AiCareerAssistantPage />
        )}
      </main>

      <footer className="bg-white border-t border-slate-200 py-6 mt-12 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-2">
          <span>SkillBridge © 2026. AI-Powered Student Skill & Career Roadmap Platform.</span>
          <span className="text-slate-400">Spring Boot 3 • Spring Security & JWT • React & Vite • RAG</span>
        </div>
      </footer>
    </div>
  );
}
