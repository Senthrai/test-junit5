package com.senthrai.junit5mavensample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    PersonService personService;

    @BeforeEach
    void setUp() {
        PersonRepository personRepository = new PersonRepository();
        personService = new PersonService(personRepository);
    }

    @Test
    void shouldCreatePerson() {
        Person person = personService.create(new Person(null, "sen", "senthrai@gmail.com"));
        assertNotNull(person.getId());
        assertEquals("sen", person.getName());
        assertEquals("senthrai@gmail.com", person.getEmail());
    }

    @Test
    void shouldCreatePerson2() {
        Person person = personService.create(new Person(null, "sen", "senthrai@gmail.com"));
        assertThat(person.getId()).isNotNull();
        assertThat(person.getName()).isEqualTo("sen");
        assertThat(person.getEmail()).isEqualTo("senthrai@gmail.com").endsWith("@gmail.com");
    }

    @Test
    void showAssertjAwesomeness(){
        String name = "Senthrai Sen";
        int age = 31;
        assertThat(name).startsWith("Sen");
        assertThat(name).containsIgnoringCase("thrai");
        assertThat(age).isGreaterThan(30);
        //==============================================================================\\

        Person person1 = new Person(1L, "Sen", "senthrai@gmail.com");
        Person person2 = new Person(2L, "Nes", "nesraith@gmail.com");
        Person person3 = new Person(1L, "Sen", "senthrai@gmail.com");

        assertThat(person1).usingRecursiveComparison().isEqualTo(person3);

        Person person4 = new Person(null, "Sen", "senthrai@gmail.com");
        Person person5 = new Person(null, "Sen", "senthrai@gmail.com");
        assertThat(person4)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(person5);

        assertThat(person4)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email")
                .isEqualTo(person5);

        //==============================================================================\\
        List<Person> personList = List.of(person1, person2, person4);
        Person person = new Person(2L, "Nes", "nesraith@gmail.com");

        //Requires to override hashcode & equals method
        assertThat(personList).contains(person);

        assertThat(person).usingRecursiveComparison()
                .comparingOnlyFields("id")
                .isIn(personList);
    }

    @Test
    void shoulThrowExceptionWhenCreatePersonWithDuplicateEmail(){
        String email = UUID.randomUUID().toString()+"@gmail.com";
        personService.create(new Person(null, "senthrai", email));

        //Junit
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            personService.create(new Person(null, "senthrai", email));
        });
        assertTrue(exception.getMessage().contentEquals("Person with email '"+email+"' already exists"));

        //Assertj
        assertThatThrownBy(() -> {
            personService.create(new Person(null, "sen", email));
        }).isInstanceOf(RuntimeException.class)
          .hasMessage("Person with email '"+email+"' already exists");

    }
}