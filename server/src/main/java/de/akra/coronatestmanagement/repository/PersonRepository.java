package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.model.PersonTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    /// JPA Query language can't do LEFT OUTER JOIN FETCH p.tests t ON (t.person.id = p.id AND t.date = :date)
    @Query("""
            SELECT p
            FROM Person p
              LEFT OUTER JOIN FETCH p.exemptions
            WHERE p.personGroup.id = :groupId 
            ORDER BY p.firstName, p.lastName
            """)
    List<Person> findPeopleForGroupByDate(
            @Param("groupId") UUID groupId
    );
}
