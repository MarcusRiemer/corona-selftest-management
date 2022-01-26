package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

}
