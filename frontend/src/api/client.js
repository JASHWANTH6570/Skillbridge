const rawBase = import.meta.env.VITE_API_BASE_URL || '';
const API_BASE = rawBase.startsWith('http') ? `${rawBase}/api` : rawBase ? `https://${rawBase}/api` : '/api';

function getAuthHeaders() {
  const token = localStorage.getItem('skillbridge_token');
  const headers = {
    'Content-Type': 'application/json',
  };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
}

export const api = {
  // Auth
  async login(username, password) {
    const res = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Login failed');
    }
    return res.json();
  },

  async register(data) {
    const res = await fetch(`${API_BASE}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Registration failed');
    }
    return res.json();
  },

  async getCurrentUser() {
    const res = await fetch(`${API_BASE}/auth/me`, {
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error('Not authenticated');
    return res.json();
  },

  // Roles & Skills
  async getRoles() {
    const res = await fetch(`${API_BASE}/roles`);
    if (!res.ok) throw new Error('Failed to load roles');
    return res.json();
  },

  async getRole(id) {
    const res = await fetch(`${API_BASE}/roles/${id}`);
    if (!res.ok) throw new Error('Failed to load role details');
    return res.json();
  },

  async getSkills() {
    const res = await fetch(`${API_BASE}/skills`);
    if (!res.ok) throw new Error('Failed to load skills');
    return res.json();
  },

  async getMySkills() {
    const res = await fetch(`${API_BASE}/skills/my-skills`, {
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error('Failed to load student skills');
    return res.json();
  },

  async saveAssessment(targetRoleId, skills) {
    const res = await fetch(`${API_BASE}/skills/assess`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ targetRoleId, skills }),
    });
    if (!res.ok) throw new Error('Failed to save assessment');
    return res.json();
  },

  async getGapAnalysis(roleId) {
    const res = await fetch(`${API_BASE}/skills/gap-analysis/${roleId}`, {
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error('Failed to compute gap analysis');
    return res.json();
  },

  // Roadmaps & Milestones
  async generateRoadmap(roleId) {
    const res = await fetch(`${API_BASE}/roadmaps/generate/${roleId}`, {
      method: 'POST',
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error('Failed to generate roadmap');
    return res.json();
  },

  async getActiveRoadmap() {
    const res = await fetch(`${API_BASE}/roadmaps/active`, {
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error('Failed to load active roadmap');
    return res.json();
  },

  async toggleMilestone(milestoneId) {
    const res = await fetch(`${API_BASE}/roadmaps/milestones/${milestoneId}/toggle`, {
      method: 'PATCH',
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error('Failed to update milestone status');
    return res.json();
  },

  async updateMilestone(milestoneId, data) {
    const res = await fetch(`${API_BASE}/roadmaps/milestones/${milestoneId}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Failed to edit milestone');
    return res.json();
  },

  // AI Career Assistant with RAG
  async chatWithAi(message, roleId, context) {
    const res = await fetch(`${API_BASE}/ai/chat`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ message, roleId, context }),
    });
    if (!res.ok) throw new Error('AI Assistant communication failed');
    return res.json();
  },

};
