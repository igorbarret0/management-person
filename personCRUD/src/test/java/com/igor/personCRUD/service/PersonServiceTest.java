package com.igor.personCRUD.service;

import com.igor.personCRUD.exceptionHandler.ResourceNotFoundException;
import com.igor.personCRUD.exceptionHandler.UserAlreadyRegisteredException;
import com.igor.personCRUD.model.Person;
import com.igor.personCRUD.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person0;

    @BeforeEach
    public void setup() {
        person0 = new Person("Leandro", "Costa", "leando@email.com",
                "Uberlandia - BR", "male");

    }

    @Test
    @DisplayName("When create person then return the person object")
    public void createPerson_Case1() {

        when(personRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(personRepository.save(person0)).thenReturn(person0);

        Person savedPerson = personService.createPerson(person0);

        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(), savedPerson.getEmail());
    }

    @Test
    @DisplayName("When tryna register a person who already registered should throw exception")
    public void createPerson_Case2() {

       when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person0));

       assertThrows(UserAlreadyRegisteredException.class, () -> {
           personService.createPerson(person0);
       });

    }

    @Test
    @DisplayName("Should return all persons")
    public void findAll_Case1() {

        Person person1 = new Person("Igor", "Barreto", "igor@email.com", "Rio Preto - SP", "male");

        when(personRepository.findAll()).thenReturn(List.of(person0, person1));

        List<Person> allPersons = personService.findAllPerson();

        assertEquals(2, allPersons.size());
        assertEquals(allPersons.get(0), person0);
        assertEquals(allPersons.get(1), person1);


    }

    @Test
    @DisplayName("Should return a empty list")
    public void findAll_Case2() {

        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        List<Person> allPersons = personService.findAllPerson();

       assertTrue(allPersons.isEmpty());
       assertEquals(0, allPersons.size());

    }

    @Test
    @DisplayName("Should return a person by ID when ID exists")
    public void findById_Case1() {

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person0));

        Optional<Person> foundPerson = Optional.ofNullable(personService.findPersonById(1L));

        assertEquals(person0.getEmail(), foundPerson.get().getEmail());

    }

    @Test
    @DisplayName("Should throw exception when ID does not exist")
    public void findById_Case2() {

        when(personRepository.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            personService.findPersonById(99L);
        });

    }

    @Test
    @DisplayName("Should updated person when person exists")
    public void updatePerson_Case1() {

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person0));

        person0.setId(1L);
        person0.setFirstName("Leonardo");
        person0.setEmail("leonardo@email.com");

        when(personRepository.save(person0)).thenReturn(person0);

        Person updatedPerson = personService.updatePerson(person0);

        assertNotNull(updatedPerson);
        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("leonardo@email.com", updatedPerson.getEmail());


    }

    @Test
    @DisplayName("Should delete a person when person exists and does not throw any exception")
    public void deletePerson_Case1() {

        person0.setId(1L);

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person0));
        doNothing().when(personRepository).delete(person0);

        personService.deletePerson(person0.getId());

        verify(personRepository, times(1)).delete(person0);

    }

    @Test
    @DisplayName("Should throw exception when tryna updated a person who does not exists")
    public void deletePerson_Case2() {

        when(personRepository.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            personService.deletePerson(99L);
        });

    }

}
