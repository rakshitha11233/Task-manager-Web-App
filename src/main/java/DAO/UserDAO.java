package DAO;
import java.util.concurrent.ConcurrentHashMap;

import model.User;

public class UserDAO {
    private static final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static boolean addUser(User user) {
        return users.putIfAbsent(user.getUsername(), user) == null;
    }

    public static User findByUsername(String username) {
        return users.get(username);
    }

    public static boolean validateCredentials(String username, String password) {
        User u = users.get(username);
        return u != null && u.getPassword().equals(password);
    }
}


