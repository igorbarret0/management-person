package com.igor.personCRUD.service;

import com.igor.personCRUD.exceptionHandler.UserAlreadyRegisteredException;
import com.igor.personCRUD.exceptionHandler.ResourceNotFoundException;
import com.igor.personCRUD.model.Person;
import com.igor.personCRUD.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    public Person createPerson(Person person) {
        Optional<Person> savedPerson = repository.findByEmail(person.getEmail());

        if (savedPerson.isPresent()) {
            throw new UserAlreadyRegisteredException("Person already exists with given email: " + person.getEmail());
        }

        return repository.save(person);
    }

    public List<Person> findAllPerson() {
        return repository.findAll();
    }

    public Person findPersonById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found for this ID"));
    }

    public Person updatePerson (Person person) {

        var entity = repository.findById(person.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Record not found for thid ID"));

        person.setFirstName(entity.getFirstName());
        person.setLastName(person.getLastName());
        person.setAddress(person.getAddress());
        person.setGender(person.getGender());
        person.setEmail(person.getEmail());

        return repository.save(person);

    }

    public void deletePerson(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record no found for this ID"));

        repository.delete(entity);
    }

}
