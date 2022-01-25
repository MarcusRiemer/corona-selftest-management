package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends CrudRepository<Person, UUID> {
    //List<Person> findByGroup(UUID groupId);
    //Person findById(UUID id);
}
