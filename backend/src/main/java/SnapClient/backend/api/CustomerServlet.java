package SnapClient.backend.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "customer-servlet",
        urlPatterns = "/customers/*",
        loadOnStartup = 0)
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!(req.getPathInfo() == null || req.getPathInfo().equals("/"))){
            resp.sendError(404, "Resource Not Found");
            return;
        }
        resp.getWriter().println("<h1>Get All Customer List</h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!(req.getPathInfo() == null || req.getPathInfo().equals("/"))){
            resp.sendError(404, "Invalid Resource Path");
            return;
        }
        resp.getWriter().println("<h1>Save a Customer</h1>");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null){
            resp.sendError(400, "No ID Provided");
        }else if (!req.getPathInfo().matches("/[Cc]\\d{3}/?")){
            resp.sendError(400, "Invalid ID Format");
        }else{
            resp.getWriter().println("<h1>Delete Customer: %s</h1>".formatted(req.
                    getPathInfo().substring(1)));
        }
    }
}