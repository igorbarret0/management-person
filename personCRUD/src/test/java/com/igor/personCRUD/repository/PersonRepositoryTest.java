package com.igor.personCRUD.repository;

import com.igor.personCRUD.integrationTests.testContainers.AbstractIntegrationTests;
import com.igor.personCRUD.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest extends AbstractIntegrationTests {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("Save a person should return the saved person")
    public void savePerson_Case1() {

        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");

        Person savedPerson = personRepository.save(person0);

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    @DisplayName("List all persons when use find all method then return persons list")
    public void findAll_Case1() {
        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");
        Person person1 = new Person("Lucas", "Moura", "moura@email.com", "Vila Velha - BR", "male");

        personRepository.save(person0);
        personRepository.save(person1);

        List<Person> allPersons = personRepository.findAll();

        assertNotNull(allPersons);
        assertEquals(2, allPersons.size());
    }

    @Test
    @DisplayName("Get a person by ID when ID exists")
    public void findById_Case1() {


        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");
        personRepository.save(person0);

        Person foundPerson = personRepository.findById(person0.getId()).get();

        assertNotNull(foundPerson);
        assertTrue(foundPerson.getId() > 0);
        assertEquals(person0, foundPerson);
    }

    @Test
    @DisplayName("Get a person by email when email exists")
    public void findByEmail() {

        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");
        personRepository.save(person0);

        Person foundPerson = personRepository.findByEmail(person0.getEmail()).get();

        assertNotNull(foundPerson);
        assertTrue(foundPerson.getId() > 0);
        assertEquals(person0, foundPerson);

    }

    @Test
    @DisplayName("Updated a person and should return the person updated")
    public void updatedPerson_Case1() {

        String emailUpdated = "leandro@hotmail.com";

        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");
        personRepository.save(person0);

        Person foundPerson = personRepository.findById(person0.getId()).get();
        foundPerson.setEmail(emailUpdated);
        personRepository.save(foundPerson);

        Person updatedPerson = personRepository.save(foundPerson);

        assertNotNull(updatedPerson);
        assertEquals(emailUpdated, updatedPerson.getEmail());

    }

    @Test
    @DisplayName("Delete a person by ID when ID exists")
    public void deletePerson_Case1() {

        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");
        personRepository.save(person0);

        personRepository.deleteById(person0.getId());
        Optional<Person> foundPerson = personRepository.findById(person0.getId());

       assertTrue(foundPerson.isEmpty());


    }

    @Test
    @DisplayName("Find a person by firstName and lastName when first and lastName is valid")
    public void findByFirstAndLastName_Case1() {

        Person person0 = new Person("Leandro", "Costa", "leando@email.com", "Uberlandia - BR", "male");
        personRepository.save(person0);

        String firstName = "Leandro";
        String lastName = "Costa";

        Person foundPerson = personRepository.findByJPQL(firstName, lastName);

        assertNotNull(foundPerson);
        assertEquals(firstName, foundPerson.getFirstName());
        assertEquals(lastName, foundPerson.getLastName());

    }


}
