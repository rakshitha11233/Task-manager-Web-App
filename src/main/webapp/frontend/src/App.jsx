import { useState } from 'react';
import LoginPage from './components/LoginPage.jsx';
import RegisterPage from './components/RegisterPage.jsx';
import TaskDashboard from './components/TaskDashboard.jsx';

export default function App() {
  const [user, setUser] = useState(null);
  const [showRegister, setShowRegister] = useState(false);

  if (!user) {
    return showRegister ? (
      <RegisterPage 
        onRegister={setUser} 
        onSwitch={() => setShowRegister(false)} 
      />
    ) : (
      <LoginPage 
        onLogin={setUser} 
        onSwitch={() => setShowRegister(true)} 
      />
    );
  }

  return <TaskDashboard username={user} onLogout={() => setUser(null)} />;
}
