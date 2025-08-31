package servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.UserDAO;
import model.User;
import util.JsonUtil;
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User incoming = JsonUtil.GSON.fromJson(req.getReader(), User.class);
        if (incoming == null || incoming.getUsername() == null || incoming.getPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"invalid\"}");
            return;
        }
        boolean ok = UserDAO.validateCredentials(incoming.getUsername(), incoming.getPassword());
        if (!ok) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"invalid credentials\"}");
            return;
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("username", incoming.getUsername());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\":\"login successful\",\"username\":\""+incoming.getUsername()+"\"}");
    }
}
    

