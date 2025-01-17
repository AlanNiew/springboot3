package com.example.springboot3.service;

import com.example.springboot3.entity.Person;
import com.example.springboot3.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> findByName(String name) {
        return personRepository.findByName(name);
    }
}
