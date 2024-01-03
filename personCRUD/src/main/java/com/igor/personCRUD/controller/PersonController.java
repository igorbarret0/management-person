package com.igor.personCRUD.controller;

import com.igor.personCRUD.exceptionHandler.ResourceNotFoundException;
import com.igor.personCRUD.model.Person;
import com.igor.personCRUD.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService service;
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody @Valid Person person) {

        var personCreated =  service.createPerson(person);

        return new ResponseEntity<>(personCreated, HttpStatus.OK);

    }
    @GetMapping
    public ResponseEntity<List<Person>> findAllPerson() {

        var allPersons = service.findAllPerson();

        return new ResponseEntity<>(allPersons, HttpStatus.OK);

    }
    @GetMapping("/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable(value = "id") Long id) {

            Person savedPerson = service.findPersonById(id);
            return new ResponseEntity<>(savedPerson, HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody @Valid Person person) {

            var updatedPerson = service.updatePerson(person);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePerson(@PathVariable(value = "id") Long id) {

        service.deletePerson(id);

        return ResponseEntity.noContent().build();
    }



}
