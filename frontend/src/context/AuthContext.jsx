import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('skillbridge_token');
    const storedUser = localStorage.getItem('skillbridge_user');
    if (token && storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        localStorage.removeItem('skillbridge_user');
      }
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    const res = await api.login(username, password);
    localStorage.setItem('skillbridge_token', res.token);
    const userData = {
      id: res.id,
      username: res.username,
      email: res.email,
      fullName: res.fullName,
      role: res.role,
      targetRoleId: res.targetRoleId,
    };
    localStorage.setItem('skillbridge_user', JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const register = async (data) => {
    const res = await api.register(data);
    localStorage.setItem('skillbridge_token', res.token);
    const userData = {
      id: res.id,
      username: res.username,
      email: res.email,
      fullName: res.fullName,
      role: res.role,
      targetRoleId: res.targetRoleId,
    };
    localStorage.setItem('skillbridge_user', JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const logout = () => {
    localStorage.removeItem('skillbridge_token');
    localStorage.removeItem('skillbridge_user');
    setUser(null);
  };

  const updateUserTargetRole = (roleId) => {
    if (user) {
      const updated = { ...user, targetRoleId: roleId };
      setUser(updated);
      localStorage.setItem('skillbridge_user', JSON.stringify(updated));
    }
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, updateUserTargetRole }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
