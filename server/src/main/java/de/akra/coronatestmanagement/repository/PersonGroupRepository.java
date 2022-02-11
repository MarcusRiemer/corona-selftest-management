package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.PersonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PersonGroupRepository extends JpaRepository<PersonGroup, UUID> {

    @Query("""
            SELECT 
              g AS group, 
              COUNT(t.id) as numTests
            FROM PersonGroup g 
              LEFT OUTER JOIN g.people p
              LEFT OUTER JOIN PersonTest t ON (t.person.id = p.id AND t.date = :date)
            GROUP BY g.id, g.name
            ORDER BY LENGTH(g.name) ASC, g.name""")
    List<GroupNumTests> findGroupWithNumTestsByDay(
            @Param("date") LocalDate date
    );

    @Query("SELECT g FROM PersonGroup g WHERE g.name IN :names")
    List<PersonGroup> findAllByName(@Param("names") Set<String> names);


    interface GroupNumTests {
        PersonGroup getGroup();

        int getNumTests();
    }
}
