package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Task;
import util.DatabaseUtil;

public class TaskDAO {

    public static Task createTask(String owner, Task t) {
        String sql = "INSERT INTO tasks (owner, title, description, due_date, completed) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, owner);
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getDescription());
            ps.setDate(4, Date.valueOf(t.getDueDate()));
            ps.setBoolean(5, t.isCompleted());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                t.setId(keys.getLong(1));
            }
            t.setOwner(owner);
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Task> getAll(String owner) {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE owner = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, owner);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Optional<Task> getById(String owner, long id) {
        String sql = "SELECT * FROM tasks WHERE owner = ? AND id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, owner);
            ps.setLong(2, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static boolean updateTask(String owner, Task updated) {
        String sql = "UPDATE tasks SET title=?, description=?, due_date=?, completed=? WHERE owner=? AND id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, updated.getTitle());
            ps.setString(2, updated.getDescription());
            ps.setDate(3, Date.valueOf(updated.getDueDate()));
            ps.setBoolean(4, updated.isCompleted());
            ps.setString(5, owner);
            ps.setLong(6, updated.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteTask(String owner, long id) {
        String sql = "DELETE FROM tasks WHERE owner=? AND id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, owner);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Task> filterByStatus(String owner, boolean completed) {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE owner=? AND completed=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, owner);
            ps.setBoolean(2, completed);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Task mapRow(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getLong("id"));
        t.setOwner(rs.getString("owner"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setDueDate(rs.getDate("due_date").toLocalDate());
        t.setCompleted(rs.getBoolean("completed"));
        return t;
    }
    public static boolean toggleTask(String owner, long id) {
    String sql = "UPDATE tasks SET completed = NOT completed WHERE owner=? AND id=?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, owner);
        ps.setLong(2, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}
