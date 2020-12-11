package com.sjtupm.manage.repository;

import com.sjtupm.manage.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Integer> {
    List<Person> findPersonByNameEqualsAndRoleEquals(String name, Integer role);
    Optional<Person> findPersonById(Integer id);
}
