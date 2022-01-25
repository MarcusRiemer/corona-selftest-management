package de.akra.coronatestmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * A group of people that will usually be tested together. This class is not simply
 * named `GROUP` because apparently that clashes with the H2 SQL syntax.
 */
@Entity
public class PersonGroup {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private String name;

    protected PersonGroup() {

    }

    public PersonGroup(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PersonGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
