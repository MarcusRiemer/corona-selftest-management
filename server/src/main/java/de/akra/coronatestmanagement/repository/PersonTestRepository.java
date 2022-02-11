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
            """)
    List<PersonTest> findTestsForGroupByDate(
            @Param("groupId") UUID groupId,
            @Param("date") LocalDate date
    );

    @Query(value = """
           SELECT 
             COUNT(t.ID) AS numTests,
             COUNT(t.result) FILTER(WHERE t.result = 'POSITIVE') AS numPositive,
             COUNT(t.result) FILTER(WHERE t.result = 'NEGATIVE') AS numNegative,
             COUNT(t.result) FILTER(WHERE t.result = 'UNKNOWN') AS numUnknown,
             COUNT(t.origin) FILTER(WHERE t.origin = 'LOCAL') AS numLocal,
             COUNT(t.origin) FILTER(WHERE t.origin = 'THIRD_PARTY') AS numThirdParty,
             COUNT(t.origin) FILTER(WHERE t.origin = 'SELF_DISCLOSURE') AS numSelfDisclosure
           FROM person_test t
           WHERE t.date = :date
           """,
           nativeQuery = true)
    CountTests countTests(
            @Param("date") LocalDate date
    );

    @Query(value = """
           SELECT pg.name,
             COUNT(t.ID) AS numTests,
             COUNT(t.result) FILTER(WHERE t.result = 'POSITIVE') AS numPositive,
             COUNT(t.result) FILTER(WHERE t.result = 'NEGATIVE') AS numNegative,
             COUNT(t.result) FILTER(WHERE t.result = 'UNKNOWN') AS numUnknown,
             COUNT(t.origin) FILTER(WHERE t.origin = 'LOCAL') AS numLocal,
             COUNT(t.origin) FILTER(WHERE t.origin = 'THIRD_PARTY') AS numThirdParty,
             COUNT(t.origin) FILTER(WHERE t.origin = 'SELF_DISCLOSURE') AS numSelfDisclosure
           FROM person_test t
             INNER JOIN person p ON t.person_id = p.id
             INNER JOIN person_group pg ON pg.id = p.person_group_id
           WHERE t.date = :date
           GROUP BY pg.id, pg.name
           ORDER BY LENGTH (pg.name) ASC, pg.name
           """,
           nativeQuery = true)
    List<CountGroupTests> countTestsByGroup(
            @Param("date") LocalDate date
    );

    interface CountTests {
        int getNumTests();

        int getNumPositive();

        int getNumNegative();

        int getNumUnknown();

        int getNumLocal();

        int getNumThirdParty();

        int getNumSelfDisclosure();
    }

    interface CountGroupTests extends CountTests {
        String getId();

        String getName();
    }
}
