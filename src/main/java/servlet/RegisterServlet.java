
package servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.UserDAO;
import model.User;
import util.JsonUtil;


public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = JsonUtil.GSON.fromJson(req.getReader(), User.class);
        if (user.getUsername() == null || user.getPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"username and password required\"}");
            return;
        }
        UserDAO userDAO = new UserDAO();
        boolean added = userDAO.addUser(user);
        if (!added) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"user exists\"}");
            return;
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(JsonUtil.GSON.toJson(user));
    }
}

