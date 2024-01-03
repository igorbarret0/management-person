package com.igor.personCRUD.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.personCRUD.exceptionHandler.ResourceNotFoundException;
import com.igor.personCRUD.model.Person;
import com.igor.personCRUD.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PersonService personService;

    private Person person0;
    private Person invalidPerson;

    @BeforeEach
    public void setup() {
        person0 = new Person("Leandro", "Costa", "leando@email.com",
                "Uberlandia - BR", "male");

        invalidPerson = new Person("", "", "", "", "");

    }

    @Test
    @DisplayName("When create person should return the object person")
    public void createPerson_Case1() throws Exception {

        when(personService.createPerson(any(Person.class)))
                .thenAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person0)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(person0.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person0.getLastName()))
                .andExpect(jsonPath("$.email").value(person0.getEmail()));

    }

    @Test
    @DisplayName("When create a person with invalid data throw exception")
    public void createPerson_Case2() throws Exception {

        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidPerson)))
                        .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @DisplayName("Get all persons when person exists")
    public void findAllPerson_Case1() throws Exception {

        List<Person> persons = new ArrayList<>();
        persons.add(person0);
        persons.add(new Person(
                "Leonardo",
                "Costa",
                "leonardo@email.com",
                "Rio Preto",
                "Male"
        ));

        when(personService.findAllPerson()).thenReturn(persons);

        ResultActions response = mockMvc.perform(get("/person"));

        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(persons.size()))
                .andExpect(jsonPath("$[0]").value(person0));
    }

    @Test
    @DisplayName("Get person by ID when ID is valid")
    public void findPersonById_Case1() throws Exception {

        long personId = 1L;

        when(personService.findPersonById(personId)).thenReturn(person0);

        ResultActions response =
                mockMvc.perform(get("/person/{id}", personId));

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(person0.getFirstName()))
                .andExpect(jsonPath("$.email").value(person0.getEmail()));

    }

    @Test
    @DisplayName("Get a person by ID when ID isn't valid")
    public void findPersonById_Case2() throws Exception {

        long personId = 1L;

        when(personService.findPersonById(personId)).thenThrow(ResourceAccessException.class);

        ResultActions response =
                mockMvc.perform(get("/person{id}", personId));

        response
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("Update a person with valid data")
    public void updatePerson_Case1() throws Exception {

        //long personId = 1L;
        Person updatedPerson = new Person(
                "Leonardo",
                "Costa",
                "leonardo@email.com",
                "Rio Preto",
                "Male");
        //when(personService.findPersonById(personId)).thenReturn(person0);
        when(personService.updatePerson(any(Person.class)))
                .thenAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response =
                mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedPerson))
                );

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedPerson.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedPerson.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedPerson.getEmail()));



    }

    @Test
    @DisplayName("Update a person with invalid return status 422")
    public void updatePerson_Case2() throws Exception {

        //long personId = 1L;
        //when(personService.findPersonById(personId)).thenReturn(person0);
        when(personService.updatePerson(any(Person.class)))
                .thenAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response =
                mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidPerson)));

        response
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @DisplayName("Update a person who does not exists return status 404")
    public void updatePerson_Case3() throws Exception {

        when(personService.updatePerson(any(Person.class))).thenThrow(ResourceNotFoundException.class);

        Person updatedPerson = new Person(
                "Leonardo",
                "Costa",
                "leonardo@erudio.com.br",
                "Uberlândia - MG",
                "Male"
        );

        ResultActions response =
                mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedPerson)));

        response
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("Delete a person who exists returns no content")
    public void deletePerson_Case1() throws Exception {

        long personId = 1L;

        doNothing().when(personService).deletePerson(personId);

        ResultActions response =
                mockMvc.perform(delete("/person/{id}", personId));

        response
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Delete a person who does not exists returns not found")
    public void deletePerson_Case2() throws Exception {

        long personId = 1L;

        doThrow(ResourceNotFoundException.class).when(personService).deletePerson(personId);

        ResultActions response =
                mockMvc.perform(delete("/person/{id}", personId));

        response
                .andExpect(status().isNotFound());

    }



}
