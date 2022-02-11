package de.akra.coronatestmanagement.repository;

import de.akra.coronatestmanagement.model.PersonTest;
import de.akra.coronatestmanagement.model.PersonTestExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PersonTestExemptionRepository extends JpaRepository<PersonTestExemption, UUID> {
    @Query(value = """
           SELECT
             COUNT(pte.reason) FILTER(WHERE pte.reason = 'RECOVERED') AS numRecovered,
             COUNT(pte.reason) FILTER(WHERE pte.reason = 'VACCINATED') AS numVaccinated
           FROM person p
             INNER JOIN person_test_exemption pte ON pte.person_id = p.id
             INNER JOIN person_test pt ON pt.person_id = p.id
           WHERE ((pte."BEGIN" >= :date) AND (:date <= pte."END"))
             AND pt.date = :date""",
           nativeQuery = true)
    CountExemptions countExemptions(
            @Param("date") LocalDate date
    );

    interface CountExemptions {
        int getNumVaccinated();

        int getNumRecovered();
    }
}
