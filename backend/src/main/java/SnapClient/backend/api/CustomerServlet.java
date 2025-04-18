package SnapClient.backend.api;

import SnapClient.backend.to.CustomerTo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import SnapClient.backend.business.CustomerBusinessLogic;
import SnapClient.backend.to.CustomerTo;
import org.apache.commons.dbcp2.BasicDataSource;
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
        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("DATA_SOURCE");
        try (Connection connection = cp.getConnection()) {
            List<CustomerTo<String>> customerList = CustomerBusinessLogic.getAllCustomers(connection);

            resp.setContentType("application/json");
            resp.setStatus(200);

            StringBuilder json = new StringBuilder();
            json.append("[");
            customerList.forEach(c ->{
                String profilePicUrl = req.getScheme() + "://" +
                        req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/upload/" +
                        c.getProfilePicture();
                json.append("""
                         {
                         "id": "%s",
                         "name": "%s",
                         "address": "%s",
                         "profilePicture": "%s"
                         }
                         """.formatted(c.getId(), c.getName(), c.getAddress(), profilePicUrl));
                json.append(",");
            });
            if (!customerList.isEmpty()) json.deleteCharAt(json.length() - 1);
            json.append("]");
            resp.getWriter().write(json.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                    BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("DATA_SOURCE");
                    try(Connection connection = cp.getConnection()) {
                        String uploadDirPath = getServletContext().getRealPath("/upload");
                        CustomerTo<String> newCustomer = CustomerBusinessLogic
                                .saveCustomer(connection, uploadDirPath,
                                        new CustomerTo<>(null, name, address, profilePicture.getInputStream()));

                        resp.setContentType("application/json");
                        resp.setStatus(201);

                        String profilePicUrl = req.getScheme() + "://" +
                                req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/upload/" +
                                newCustomer.getProfilePicture();

                        String json = """
                                 {
                                 "id": "%s",
                                 "name": "%s",
                                 "address": "%s",
                                 "profilePicture": "%s",
                                 }
                                 """.formatted(newCustomer.getId(), name, address, profilePicUrl);
                        resp.getWriter().println(json);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
            BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("DATA_SOURCE");
            try (Connection connection = cp.getConnection()) {
                String uploadDirPath = getServletContext().getRealPath("/upload");
                String customerId = req.getPathInfo().substring(1);
                if (CustomerBusinessLogic.deleteCustomer(connection, uploadDirPath, customerId)){
                    resp.setStatus(204);
                }else{
                    resp.sendError(404, "Invalid ID");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}