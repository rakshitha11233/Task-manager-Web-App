
package servlet;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.TaskDAO;
import model.Task;
import util.JsonUtil;

public class TaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = getUsernameFromSession(req, resp);
        if (username == null) return;

        String filter = req.getParameter("filter");
        List<Task> list;
        if ("completed".equalsIgnoreCase(filter)) list = TaskDAO.filterByStatus(username, true);
        else if ("pending".equalsIgnoreCase(filter)) list = TaskDAO.filterByStatus(username, false);
        else list = TaskDAO.getAll(username);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(JsonUtil.GSON.toJson(list));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = getUsernameFromSession(req, resp);
        if (username == null) return;

        String action = req.getParameter("action");
        if ("toggle".equalsIgnoreCase(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            TaskDAO.getById(username, id).ifPresent(t -> t.setCompleted(!t.isCompleted()));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"toggled\"}");
            return;
        }

        Task t = JsonUtil.GSON.fromJson(req.getReader(), Task.class);
        if (t.getTitle() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"title required\"}");
            return;
        }
        if (t.getDueDate() == null) t.setDueDate(LocalDate.now());

        Task created = TaskDAO.createTask(username, t);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(JsonUtil.GSON.toJson(created));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = getUsernameFromSession(req, resp);
        if (username == null) return;

        Task t = JsonUtil.GSON.fromJson(req.getReader(), Task.class);
        if (t.getId() == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"id required\"}");
            return;
        }
        boolean ok = TaskDAO.updateTask(username, t);
        if (!ok) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"task not found\"}");
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(JsonUtil.GSON.toJson(t));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = getUsernameFromSession(req, resp);
        if (username == null) return;

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"id required\"}");
            return;
        }
        long id = Long.parseLong(idParam);
        boolean ok = TaskDAO.deleteTask(username, id);
        if (!ok) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"task not found\"}");
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\":\"deleted\"}");
    }

    private String getUsernameFromSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"not authenticated\"}");
            return null;
        }
        return session.getAttribute("username").toString();
    }
}

