import React from 'react';
import { useAuth } from '../context/AuthContext';
import AiAssistantModal from '../components/AiAssistantModal';
import { Sparkles, HelpCircle } from 'lucide-react';

export default function AiCareerAssistantPage() {
  const { user } = useAuth();

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-xl font-bold text-slate-900 flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-indigo-600" />
            AI Career Assistant (RAG)
          </h2>
          <p className="text-xs text-slate-500 mt-0.5">
            Trained on verified curriculum, Spring Boot architectures, React frameworks, and engineering benchmarks
          </p>
        </div>
      </div>

      <AiAssistantModal
        targetRoleId={user?.targetRoleId || 1}
        roleTitle="Full Stack Developer (Java & React)"
      />
    </div>
  );
}
