package de.akra.coronatestmanagement.model;

import javax.persistence.*;
import java.util.*;

/**
 * A group of people that will usually be tested together. This class is not simply
 * named `GROUP` because apparently that clashes with the H2 SQL syntax.
 */
@Entity
@NamedEntityGraph(name = "PersonGroup.people", attributeNodes = @NamedAttributeNode("people"))
public class PersonGroup {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "personGroup")
    private Set<Person> people = new HashSet<>();

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
        return "PersonGroup{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    public Set<Person> getPeople() {
        return people;
    }
}
