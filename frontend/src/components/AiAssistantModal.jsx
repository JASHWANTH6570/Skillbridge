import React, { useState } from 'react';
import { api } from '../api/client';
import { 
  Sparkles, 
  Send, 
  Bot, 
  User as UserIcon, 
  BookMarked, 
  CheckCircle, 
  ExternalLink, 
  Info,
  Loader2 
} from 'lucide-react';

export default function AiAssistantModal({ targetRoleId, roleTitle }) {
  const [messages, setMessages] = useState([
    {
      sender: 'assistant',
      text: `Hello! I am your SkillBridge AI Career Assistant. I use Retrieval-Augmented Generation (RAG) over our staff-approved curriculum to provide grounded career guidance for ${roleTitle || 'your target role'}. Ask me anything about skills, milestone execution, or portfolio planning!`,
      reason: 'SkillBridge AI retrieves only verified learning sources to eliminate hallucinations.',
      nextSteps: [
        'Explore your skill gap breakdown',
        'Ask questions about mastering specific technologies',
        'Get feedback on portfolio project ideas'
      ],
      sources: []
    }
  ]);

  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const samplePrompts = [
    "What are the most critical skills I need to land this role?",
    "How can I prove Spring Boot and React proficiency in my portfolio?",
    "Which curated resources are best for mastering Docker containerization?",
    "Explain the difference between required vs recommended role skills."
  ];

  const handleSend = async (textToSend) => {
    const query = textToSend || input;
    if (!query.trim() || loading) return;

    const userMsg = { sender: 'user', text: query };
    setMessages((prev) => [...prev, userMsg]);
    setInput('');
    setLoading(true);

    try {
      const response = await api.chatWithAi(query, targetRoleId, 'Student active learning roadmap');
      setMessages((prev) => [
        ...prev,
        {
          sender: 'assistant',
          text: response.answer,
          reason: response.recommendationReason,
          nextSteps: response.actionableNextSteps,
          sources: response.sources,
          disclaimer: response.disclaimer,
        }
      ]);
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        {
          sender: 'assistant',
          text: 'I encountered an issue connecting to the RAG knowledge service. Please ensure the backend is active.',
          reason: 'Network or configuration fault',
          nextSteps: [],
          sources: []
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200 shadow-sm flex flex-col h-[750px] overflow-hidden">
      {/* Header */}
      <div className="p-4 sm:p-5 bg-gradient-to-r from-indigo-900 via-slate-900 to-indigo-950 text-white flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-violet-500 to-indigo-500 flex items-center justify-center text-white shadow-md">
            <Sparkles className="w-5 h-5" />
          </div>
          <div>
            <h3 className="font-bold text-base sm:text-lg flex items-center gap-2">
              AI Career Assistant
              <span className="text-[10px] bg-indigo-500/30 text-indigo-200 border border-indigo-400/30 px-2 py-0.5 rounded-full font-semibold">
                RAG Grounded
              </span>
            </h3>
            <p className="text-xs text-indigo-200">
              Answers backed by staff-approved, curated learning resources
            </p>
          </div>
        </div>
      </div>

      {/* Career Outcome Disclaimer Banner */}
      <div className="bg-amber-50 px-4 py-2 border-b border-amber-200/60 flex items-center gap-2 text-xs text-amber-800">
        <Info className="w-4 h-4 text-amber-600 shrink-0" />
        <span>
          <strong>Advisory Note:</strong> AI output is treated as guidance, not a guaranteed career outcome.
        </span>
      </div>

      {/* Messages Scroll Area */}
      <div className="flex-1 overflow-y-auto p-4 sm:p-6 space-y-6 bg-slate-50/50">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`flex gap-3 ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
          >
            {msg.sender === 'assistant' && (
              <div className="w-8 h-8 rounded-lg bg-indigo-600 text-white flex items-center justify-center shrink-0 mt-1 shadow-sm">
                <Bot className="w-4 h-4" />
              </div>
            )}

            <div
              className={`max-w-2xl rounded-2xl p-4 sm:p-5 text-sm leading-relaxed ${
                msg.sender === 'user'
                  ? 'bg-indigo-600 text-white rounded-br-none shadow-sm'
                  : 'bg-white border border-slate-200 rounded-bl-none shadow-sm space-y-4'
              }`}
            >
              <div className="whitespace-pre-line font-medium text-slate-800">
                {msg.text}
              </div>

              {/* Recommendation Reason */}
              {msg.reason && (
                <div className="p-3 bg-indigo-50/70 border border-indigo-100 rounded-xl text-xs text-indigo-900">
                  <span className="font-bold uppercase tracking-wider text-[10px] text-indigo-700 block mb-0.5">
                    Recommendation Rationale
                  </span>
                  {msg.reason}
                </div>
              )}

              {/* Actionable Next Steps */}
              {msg.nextSteps && msg.nextSteps.length > 0 && (
                <div className="space-y-1.5 pt-2 border-t border-slate-100">
                  <span className="text-xs font-bold text-slate-700 uppercase tracking-wider block">
                    Actionable Next Steps
                  </span>
                  {msg.nextSteps.map((step, sIdx) => (
                    <div key={sIdx} className="flex items-start gap-2 text-xs text-slate-600">
                      <CheckCircle className="w-3.5 h-3.5 text-emerald-600 shrink-0 mt-0.5" />
                      <span>{step}</span>
                    </div>
                  ))}
                </div>
              )}

              {/* Curated RAG Citations */}
              {msg.sources && msg.sources.length > 0 && (
                <div className="space-y-2 pt-2 border-t border-slate-100">
                  <span className="text-xs font-bold text-slate-700 uppercase tracking-wider flex items-center gap-1.5">
                    <BookMarked className="w-3.5 h-3.5 text-indigo-600" />
                    Curated Citations & Approved Sources
                  </span>
                  <div className="grid grid-cols-1 gap-1.5">
                    {msg.sources.map((src, srcIdx) => (
                      <div
                        key={srcIdx}
                        className="p-2.5 rounded-lg bg-slate-50 border border-slate-200/70 text-xs flex items-center justify-between gap-2"
                      >
                        <div>
                          <div className="font-semibold text-slate-800">{src.title}</div>
                          <div className="text-[11px] text-slate-500">
                            {src.sourceName} • Relevance: {Math.round(src.relevanceScore * 100)}%
                          </div>
                        </div>
                        <a
                          href={src.sourceUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-indigo-600 hover:text-indigo-800 flex items-center gap-1 text-[11px] font-bold shrink-0"
                        >
                          Source <ExternalLink className="w-3 h-3" />
                        </a>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            {msg.sender === 'user' && (
              <div className="w-8 h-8 rounded-lg bg-slate-800 text-white flex items-center justify-center shrink-0 mt-1 shadow-sm">
                <UserIcon className="w-4 h-4" />
              </div>
            )}
          </div>
        ))}

        {loading && (
          <div className="flex gap-3 items-center text-slate-500 text-xs italic">
            <div className="w-8 h-8 rounded-lg bg-indigo-600 text-white flex items-center justify-center">
              <Loader2 className="w-4 h-4 animate-spin" />
            </div>
            <span>Consulting RAG vector knowledge base and synthesizing guidance...</span>
          </div>
        )}
      </div>

      {/* Suggested prompts chips */}
      <div className="px-4 py-2 bg-white border-t border-slate-100 overflow-x-auto flex gap-2">
        {samplePrompts.map((p, idx) => (
          <button
            key={idx}
            onClick={() => handleSend(p)}
            className="text-xs px-3 py-1.5 rounded-full bg-slate-100 hover:bg-indigo-50 hover:text-indigo-700 text-slate-600 border border-slate-200 whitespace-nowrap transition-colors"
          >
            {p}
          </button>
        ))}
      </div>

      {/* Input bar */}
      <div className="p-4 bg-white border-t border-slate-200">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSend();
          }}
          className="flex items-center gap-2"
        >
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Ask about career roles, skill gaps, projects, or study plans..."
            className="flex-1 px-4 py-3 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
            disabled={loading}
          />
          <button
            type="submit"
            disabled={loading || !input.trim()}
            className="px-5 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white font-semibold flex items-center gap-2 text-sm shadow-md shadow-indigo-500/20"
          >
            <Send className="w-4 h-4" />
            <span>Send</span>
          </button>
        </form>
      </div>
    </div>
  );
}
