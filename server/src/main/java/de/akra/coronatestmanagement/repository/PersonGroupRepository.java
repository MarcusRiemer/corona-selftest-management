package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.PersonGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonGroupRepository extends JpaRepository<PersonGroup, UUID> {
    Optional<PersonGroup> findById(UUID id);

    //@Query("SELECT g.id AS groupId, COUNT(g.id) as numTests FROM PersonGroup g LEFT OUTER JOIN g.people p LEFT OUTER JOIN p.tests t WHERE t.date = :date GROUP BY g.id")
    @Query("""
              SELECT g AS group, COUNT(t.id) as numTests
              FROM PersonGroup g 
                LEFT OUTER JOIN g.people p
                LEFT OUTER JOIN p.tests t
              WHERE t.date = :date 
                OR t.date IS NULL
              GROUP BY g.id
              ORDER BY g.name""")
    List<GroupNumTests> findGroupWithNumTestsByDay(
            @Param("date") LocalDate date
    );

    public interface GroupNumTests {
        PersonGroup getGroup();
        int getNumTests();
    }

    /*public record GroupNumTests(PersonGroup group, int numTests) {
        protected GroupNumTests() {
            this(null, -1);
        }
    }*/
}
