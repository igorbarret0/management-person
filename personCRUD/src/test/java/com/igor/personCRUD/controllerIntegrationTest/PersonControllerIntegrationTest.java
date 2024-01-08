package com.igor.personCRUD.controllerIntegrationTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.personCRUD.Config.TestsConfig;
import com.igor.personCRUD.integrationTests.testContainers.AbstractIntegrationTests;
import com.igor.personCRUD.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTests {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup() {

        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        requestSpecification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestsConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("Leandro", "Costa", "leando@email.com",
                "Uberlandia - BR", "male");
    }

    @Test
    @Order(1)
    @DisplayName("Integration test when create one person should return a person object")
    public void createPerson_Case1() throws JsonProcessingException {

        var content = given().spec(requestSpecification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        Person createdPerson = mapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getEmail());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertTrue(person.getId() > 0);
        assertEquals("leando@email.com", person.getEmail());

    }

    @Test
    @Order(2)
    @DisplayName("Integration test when update one person should return a person object updated")
    public void updatePerson_Case1() throws JsonProcessingException {

        person.setFirstName("Leonardo");
        person.setEmail("leonardo@email.com");

        var content = given().spec(requestSpecification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        Person updatedPerson = mapper.readValue(content, Person.class);

        person = updatedPerson;

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getEmail());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertTrue(person.getId() > 0);
        assertEquals("leonardo@email.com", person.getEmail());

    }

    @Test
    @Order(3)
    @DisplayName("Found a user by ID should return a person object when ID is valid")
    public void findPersonById_Case1() throws JsonProcessingException {

        var content =
                given().spec(requestSpecification)
                        .pathParam("id", person.getId())
                        .when()
                        .get("/{id}")
                            .then()
                        .statusCode(200)
                            .extract()
                                .body()
                                    .asString();

        Person foundPerson = mapper.readValue(content, Person.class);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getEmail());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertTrue(person.getId() > 0);
        assertEquals("leonardo@email.com", person.getEmail());

    }

    @Test
    @Order(4)
    @DisplayName("Return a list of all users registered")
    public void findAllPerson_Case1() throws JsonProcessingException {

        given().spec(requestSpecification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .body(new Person(
                        "Gabriela",
                        "Rodrigues",
                        "gabi@email.com",
                        "Rio Branco - Acre",
                        "Female"
                ))
                .when()
                    .post()
                .then()
                    .statusCode(200);

        var content =
                given().spec(requestSpecification)
                        .when()
                        .get()
                        .then()
                            .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        List<Person> allPersons = Arrays.asList(mapper.readValue(content, Person[].class));

        Person foundPersonOne = allPersons.get(0);
        Person foundPersonTwo = allPersons.get(1);

        assertNotNull(allPersons);
        assertEquals(2, allPersons.size());

        assertEquals("leonardo@email.com", foundPersonOne.getEmail());
        assertEquals("gabi@email.com", foundPersonTwo.getEmail());

    }
    
    @Test
    @Order(5)
    @DisplayName("Remove a exists person don't throw exception and return no content")
    public void deletePerson_Case1() {


        given().spec(requestSpecification)
                .pathParam("id", person.getId())
                .when()
                    .delete("/{id}")
                .then()
                    .statusCode(204);



    }

}
