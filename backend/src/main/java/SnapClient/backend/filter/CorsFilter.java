package SnapClient.backend.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;
import java.util.stream.Stream;

@WebFilter(filterName = "cors-filter", urlPatterns = "/*")
public class CorsFilter extends HttpFilter {

    boolean allowedAnyOrigin;
    String[] allowedOrigins = {};

    public CorsFilter() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        String allowedOrigins = properties.getProperty("app.allowed.origins");
        if (allowedOrigins != null) {
            this.allowedOrigins = Stream.of(allowedOrigins.split(",")).map(String::trim)
                    .toArray(String[]::new);
            for (String origin : this.allowedOrigins) {
                if (origin.contains("*"))  {
                    allowedAnyOrigin = true;
                    break;
                }
            }
        }
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String origin = req.getHeader("Origin");
        if (origin != null && !allowedAnyOrigin &&
                Stream.of(allowedOrigins).noneMatch(origin::contains)){
            res.sendError(400, "Invalid origin");
        }else{
            if (origin != null) res.addHeader("Access-Control-Allow-Origin", origin);
            chain.doFilter(req, res);
        }
    }
}