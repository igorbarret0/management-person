package com.igor.personCRUD.integrationTests.testContainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTests.Initializer.class)
public class AbstractIntegrationTests  {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        // CRIA UM CONTAINER EM RUNTIME
        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private static Map<String, String> createConnectionConfigurtion() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            MapPropertySource testContainers = new MapPropertySource(
                    "testcontainers",
                    (Map)createConnectionConfigurtion());

            environment.getPropertySources().addFirst(testContainers);

        }
    }

}
