package com.igor.personCRUD.integrationTestsSwagger;

import com.igor.personCRUD.Config.TestsConfig;
import com.igor.personCRUD.integrationTests.testContainers.AbstractIntegrationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTests {

    @Test
    @DisplayName("Should display swaggerUi page")
    public void ShouldDisplay_SwaggerUiPage() {

        var content =
                given()
                .basePath("swagger-ui/index.html")
                .port(TestsConfig.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        assertTrue(content.contains("Swagger UI"));

    }

}
