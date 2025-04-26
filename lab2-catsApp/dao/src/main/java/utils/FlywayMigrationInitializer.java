package utils;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;

public class FlywayMigrationInitializer {

    public static void initializeFlyway() {
        Configuration hibernateConfig = new Configuration().configure();

        String url = hibernateConfig.getProperty("hibernate.connection.url");
        String username = hibernateConfig.getProperty("hibernate.connection.username");
        String password = hibernateConfig.getProperty("hibernate.connection.password");

        Flyway flyway = Flyway.configure()
                .dataSource(url, username, password)
                .load();

        flyway.migrate();

        System.out.println("Database migration completed successfully.");
    }
}