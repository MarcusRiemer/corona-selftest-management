package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.PersonGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "groups", path = "groups")
public interface PersonGroupRepository extends CrudRepository<PersonGroup, UUID> {
    @EntityGraph(value = "PersonGroup.people", type = EntityGraph.EntityGraphType.LOAD)
    Optional<PersonGroup> findById(UUID id);
}
