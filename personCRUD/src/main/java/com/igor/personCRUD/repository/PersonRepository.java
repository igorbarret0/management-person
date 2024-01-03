package com.igor.personCRUD.repository;

import com.igor.personCRUD.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    @Query(value = "SELECT p FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName")
    Person findByJPQL(String firstName, String lastName);


}
