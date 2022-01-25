package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.PersonGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "groups", path = "groups")
public interface GroupRepository extends CrudRepository<PersonGroup, UUID> {
    //Group findById(UUID id);
}
