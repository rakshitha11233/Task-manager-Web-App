package DAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import model.Task;

public class TaskDAO {
    private static final ConcurrentHashMap<String, ConcurrentHashMap<Long, Task>> store = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    public static Task createTask(String owner, Task t) {
        long id = idCounter.getAndIncrement();
        t.setId(id);
        t.setOwner(owner);
        store.computeIfAbsent(owner, k -> new ConcurrentHashMap<>()).put(id, t);
        return t;
    }

    public static List<Task> getAll(String owner) {
        return new ArrayList<>(store.getOrDefault(owner, new ConcurrentHashMap<>()).values());
    }

    public static Optional<Task> getById(String owner, long id) {
        return Optional.ofNullable(store.getOrDefault(owner, new ConcurrentHashMap<>()).get(id));
    }

    public static boolean updateTask(String owner, Task updated) {
        Map<Long, Task> map = store.get(owner);
        if (map == null || !map.containsKey(updated.getId())) return false;
        updated.setOwner(owner);
        map.put(updated.getId(), updated);
        return true;
    }

    public static boolean deleteTask(String owner, long id) {
        Map<Long, Task> map = store.get(owner);
        if (map == null) return false;
        return map.remove(id) != null;
    }

    public static List<Task> filterByStatus(String owner, boolean completed) {
        return getAll(owner).stream().filter(t -> t.isCompleted() == completed).collect(Collectors.toList());
    }
}
    

