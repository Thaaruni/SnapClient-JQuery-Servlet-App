package SnapClient.backend.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import java.io.IOException;

@MultipartConfig(location = "/tmp", maxFileSize = 5 * 1024 * 1024)
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
        }
        else if (Objects.isNull(req.getContentType()) ||
                !req.getContentType().contains("multipart/form-data")) {
            resp.sendError(400, "Invalid Content Type");
        } else {
            try {
                String name = req.getParameter("name");
                String address = req.getParameter("address");
                Part profilePicture = req.getPart("profilePicture");

                if (name == null || !name.trim().matches("[A-Za-z ]+")) {
                    resp.sendError(400, "Invalid Name");
                } else if (address == null || address.trim().length() < 3) {
                    resp.sendError(400, "Invalid Address");
                } else if (profilePicture == null || profilePicture.getContentType() == null ||
                        !profilePicture.getContentType().contains("image/")) {
                    resp.sendError(400, "Invalid Profile Picture");
                } else {
                    resp.getWriter().printf("<h1>Save a Customer name=%s, address=%s</h1>"
                            , name, address);
                }
            } catch (IllegalStateException e) {
                resp.sendError(400, "Profile picture size exceeds the upload limit of 5MB");
            }
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