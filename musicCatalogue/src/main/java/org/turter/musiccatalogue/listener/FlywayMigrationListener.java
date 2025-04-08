package org.turter.musiccatalogue.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.flywaydb.core.Flyway;

public class FlywayMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5434/musicdb", "user", "password")
                .baselineVersion("0")
                .load();

        flyway.baseline();
        flyway.migrate();
        System.out.println("Flyway migration completed!");
    }

}