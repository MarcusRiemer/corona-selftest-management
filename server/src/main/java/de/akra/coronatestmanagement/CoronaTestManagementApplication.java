package de.akra.coronatestmanagement;

import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.repository.PersonGroupRepository;
import de.akra.coronatestmanagement.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class CoronaTestManagementApplication {
    private static final Logger log = LoggerFactory.getLogger(CoronaTestManagementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CoronaTestManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(PersonRepository personRepository, PersonGroupRepository groupRepository) {
        return (args) -> {
            // One main group
            PersonGroup g = new PersonGroup("7a");
            groupRepository.save(g);


            // Create a few people
            personRepository.saveAll(Arrays.asList(
                    new Person("Anton", "Erster", g),
                    new Person("Berta", "Zweite", g),
                    new Person("Cäsar", "Dritter", g)
            ));

            // fetch all customers
            log.info("People found with findAll():");
            log.info("----------------------------");
            for (Person p : personRepository.findAll()) {
                log.info(p.toString());
            }
            log.info("");

            PersonGroup stored = groupRepository.findById(g.getId()).orElseThrow();

            log.info("People in group:" + stored.getPeople().size());


        };
    }

}
