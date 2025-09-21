import React, { useState } from 'react';
import { register } from '../services/api';

export default function RegisterPage({ onRegister, onSwitch }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    const res = await register(username, password);
    if (res.error) setError(res.error);
    else onRegister(res.username);
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <input placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} /><br />
        <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} /><br />
        <button type="submit">Register</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <p>
        Already have an account?{' '}
        <button onClick={onSwitch}>Back to Login</button>
      </p>
    </div>
  );
}
