const API_BASE = '/api';

export async function login(username, password) {
  const res = await fetch(`${API_BASE}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ username, password })
  });
  return res.json();
}

export async function register(username, password) {
  const res = await fetch(`${API_BASE}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ username, password })
  });
  return res.json();
}

export async function fetchTasks(filter='') {
  const res = await fetch(`${API_BASE}/tasks${filter ? '?filter='+filter : ''}`, {
    credentials: 'include'
  });
  return res.json();
}

export async function toggleTask(id) {
  const res = await fetch(`${API_BASE}/tasks?action=toggle&id=${id}`, {
    method: 'POST',
    credentials: 'include'
  });
  return res.json();
}

export async function addTask(task) {
  const res = await fetch(`${API_BASE}/tasks`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(task)
  });
  return res.json();
}
export async function logout() {
  await fetch(`${API_BASE}/logout`, {
    method: 'POST',
    credentials: 'include'
  });
}

