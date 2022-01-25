package de.akra.coronatestmanagement;

import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.repository.GroupRepository;
import de.akra.coronatestmanagement.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoronaTestManagementApplication {
	private static final Logger log = LoggerFactory.getLogger(CoronaTestManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CoronaTestManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PersonRepository personRepository, GroupRepository groupRepository) {
		return (args) -> {
			// One main group
			PersonGroup g = new PersonGroup("7a");
			groupRepository.save(g);


			// Create a few people
			personRepository.save(new Person("Anton", "Erster", g));
			personRepository.save(new Person("Berta", "Zweite", g));
			personRepository.save(new Person("Cäsar", "Dritter",g ));

			// fetch all customers
			log.info("People found with findAll():");
			log.info("----------------------------");
			for (Person p : personRepository.findAll()) {
				log.info(p.toString());
			}
			log.info("");

			log.info("People in grouo:");

			PersonGroup stored = groupRepository.findById(g.getId()).orElseThrow();



		};
	}

}
