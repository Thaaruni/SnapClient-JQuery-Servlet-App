package SnapClient.backend.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.util.Properties;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //ResourceBundle props = ResourceBundle.getBundle("application.properties");
        final AppProperties PROPS = new AppProperties();
        final BasicDataSource BDS = new BasicDataSource();

        BDS.setDriverClassName(PROPS.getProperty("app.datasource.driver"));
        BDS.setUsername(PROPS.getProperty("app.datasource.user"));
        BDS.setPassword(PROPS.getProperty("app.datasource.password"));
        BDS.setUrl(PROPS.getProperty("app.datasource.url"));
        BDS.setInitialSize(PROPS.getIntProperty("app.datasource.initial-size", 5));
        BDS.setMaxTotal(PROPS.getIntProperty("app.datasource.max-total", 15));
        BDS.setMaxIdle(PROPS.getIntProperty("app.datasource.max-idle", 5));
        BDS.setMinIdle(PROPS.getIntProperty("app.datasource.min-idle", 2));

        sce.getServletContext().setAttribute("DATA_SOURCE", BDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private static class AppProperties extends Properties {

        public AppProperties() {
            try {
                load(getClass().getResourceAsStream("/application.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int getIntProperty(String key, Integer defaultValue) {
            return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
        }
    }
}