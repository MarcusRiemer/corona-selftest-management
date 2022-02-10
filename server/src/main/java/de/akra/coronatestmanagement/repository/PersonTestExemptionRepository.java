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

}
