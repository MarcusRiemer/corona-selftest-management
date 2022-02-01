package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.PersonTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PersonTestRepository extends JpaRepository<PersonTest, UUID> {
    @Query("""
           SELECT t
           FROM PersonTest t
             JOIN FETCH t.person p
           WHERE p.personGroup.id = :groupId
             AND t.date = :date
           ORDER BY p.firstName, p.lastName
           """)
    List<PersonTest> findTestForGroupByDate(
            @Param("groupId") UUID groupId,
            @Param("date") LocalDate date
    );

    @Query("""
           SELECT g
           FROM PersonTest t 
             JOIN t.person p
             JOIN p.personGroup g
           WHERE t.date = :date 
             AND t.result = 'POSITIVE'
           GROUP BY g.id
           """)
    int countPositiveTests(
            @Param("date") LocalDate date
    );
}
