import { useEffect, useState } from 'react'; 
import { fetchTasks, toggleTask, addTask, logout } from '../services/api';

export default function TaskDashboard({ username, onLogout }) {
  const [tasks, setTasks] = useState([]);
  const [filter, setFilter] = useState('');
  const [newTask, setNewTask] = useState({ title: '', description: '', dueDate: '' });

  // Load tasks for the user
  const loadTasks = async (f = '') => {
    const data = await fetchTasks(f);
    setTasks(data);
  };

  useEffect(() => { loadTasks(filter); }, [filter]);

  // Complete task
  const handleComplete = async (id) => {
    await toggleTask(id); // toggleTask marks as completed
    loadTasks(filter);
  };

  // Delete task
  const handleDelete = async (id) => {
   await fetch('/api/tasks?id=' + id, {
  method: 'DELETE',
  credentials: 'include',
});
    loadTasks(filter);
  };

  // Update task
  const handleUpdate = async (id) => {
    const t = tasks.find(task => task.id === id);
    const newTitle = prompt("New Title", t.title);
    const newDescription = prompt("New Description", t.description);
    const newDueDate = prompt("New Due Date (yyyy-mm-dd)", t.dueDate);
    if (!newTitle) return;

    await fetch('/api/tasks', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  credentials: 'include',
  body: JSON.stringify({
    id: id,
    title: newTitle,
    description: newDescription,
    dueDate: newDueDate,
    completed: t.completed
  })
});
    loadTasks(filter);
  };

  // Add new task
  const handleAdd = async (e) => {
    e.preventDefault();
    if (!newTask.title) return alert('Title is required');
    await addTask({
      ...newTask,
      dueDate: newTask.dueDate || new Date().toISOString().split('T')[0], // default today
    });
    setNewTask({ title: '', description: '', dueDate: '' });
    loadTasks(filter);
  };

  // Logout
  const handleLogout = async () => {
    await logout();
    onLogout(null);
  };

  return (
    <div>
      <h2>{username}'s Dashboard</h2>
      <button onClick={handleLogout} style={{ float: 'right' }}>Logout</button>

      <div style={{ marginBottom: '1rem' }}>
        <button onClick={() => setFilter('')}>All</button>
        <button onClick={() => setFilter('completed')}>Completed</button>
        <button onClick={() => setFilter('pending')}>Pending</button>
      </div>

      <ul>
        {tasks.map((t) => (
          <li key={t.id} style={{ marginBottom: '0.5rem' }}>
            <strong>{t.title}</strong> - {t.description} (Due: {t.dueDate})
            
            {!t.completed && (
              <button onClick={() => handleComplete(t.id)} style={{ marginLeft: '1rem' }}>
                Complete
              </button>
            )}
            {t.completed && <span style={{ color: 'green', marginLeft: '1rem' }}>Completed</span>}

            {/* Update button */}
            <button 
              onClick={() => handleUpdate(t.id)} 
              style={{ marginLeft: '0.5rem' }}
            >
              Update
            </button>

            {/* Delete button */}
            <button 
              onClick={() => handleDelete(t.id)} 
              style={{ marginLeft: '0.5rem' }}
            >
              Delete
            </button>
          </li>
        ))}
      </ul>

      <form onSubmit={handleAdd} style={{ marginTop: '1rem' }}>
        <h3>Add Task</h3>
        <input
          placeholder="Title"
          value={newTask.title}
          onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
        /><br />
        <input
          placeholder="Description"
          value={newTask.description}
          onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
        /><br />
        <input
          type="date"
          value={newTask.dueDate}
          onChange={(e) => setNewTask({ ...newTask, dueDate: e.target.value })}
        /><br />
        <button type="submit">Add Task</button>
      </form>
    </div>
  );
}