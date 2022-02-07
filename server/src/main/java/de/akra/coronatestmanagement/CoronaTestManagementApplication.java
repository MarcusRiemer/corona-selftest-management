package de.akra.coronatestmanagement;

import com.github.javafaker.Faker;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.repository.PersonGroupRepository;
import de.akra.coronatestmanagement.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CoronaTestManagementApplication {
    private static final Logger log = LoggerFactory.getLogger(CoronaTestManagementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CoronaTestManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(PersonRepository personRepository, PersonGroupRepository groupRepository) {
        return (args) -> {
            // Basic seeding required?
            if (false && groupRepository.count() == 0) {
                Faker faker = new Faker(Locale.GERMAN);
                Random random = new Random();

                faker.name().firstName();

                String[] groups = {"1a", "2a", "3a", "4a", "4b", "5a", "5b", "5c", "6a", "6b", "6c", "7a", "7b", "7c", "8a", "8b", "9a", "9b", "9c", "10a", "10b", "10c"};
                //String[] groups = {"1a", "2a"};
                for (var name : groups) {
                    PersonGroup g = new PersonGroup(name);
                    groupRepository.save(g);

                    int numPeople = 15 + random.nextInt(15);
                    ArrayList<Person> people = new ArrayList<>(numPeople);
                    for (int i = 0; i < numPeople; ++i) {
                        people.add(new Person(faker.name().firstName(), faker.name().lastName(), g));
                    }

                    personRepository.saveAll(people);
                }
            }
        };
    }

}
