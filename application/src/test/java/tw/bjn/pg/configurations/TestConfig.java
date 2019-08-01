package tw.bjn.pg.configurations;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer("postgres:11.4")
                .withDatabaseName("databaseName")
                .withUsername("user")
                .withPassword("pw");
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer postgreSQLContainer) {
        if (!postgreSQLContainer.isRunning())
            postgreSQLContainer.start();
        // TODO: update test DB schema
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .username(postgreSQLContainer.getUsername())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .url(postgreSQLContainer.getJdbcUrl())
                .build();
    }

}
