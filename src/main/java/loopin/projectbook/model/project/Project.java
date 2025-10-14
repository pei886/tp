package loopin.projectbook.model.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.person.Person;

import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents a project in the ProjectBook system.
 * <p>
 * A Project contains identifying information such as its name and description,
 * timestamps for creation and last update, and a list of {@link Membership}
 * objects representing people who are part of the project.
 */
public class Project {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Membership> memberships = new ArrayList<>();

    /**
     * Creates a new Project with the given id, name, and description.
     *
     * @param id          unique identifier for the project
     * @param name        name of the project
     * @param description short description of the project
     */
    public Project(UUID id, String name, String description) {
        requireAllNonNull(id, name, description);
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /** @return the unique ID of this project */
    public UUID getId() { return id; }

    /** @return the name of this project */
    public String getName() { return name; }

    /** @return the description of this project */
    public String getDescription() { return description; }

    /** @return the timestamp when this project was created */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /** @return the timestamp of the last update to this project */
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    /**
     * Returns all people who are members of this project.
     *
     * @return a list of {@link Person} objects representing all members
     */
    public List<Person> getAllPeople() {
        return memberships.stream()
                .map(Membership::getPerson)
                .collect(Collectors.toList());
    }

    /**
     * Updates the timestamp indicating when this project was last modified.
     *
     * @param updatedAt the new update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Adds a new membership (person) to this project.
     *
     * @param membership the {@link Membership} to add
     */
    public void addMembership(Membership membership) {
        memberships.add(membership);
    }

    /**
     * Returns a string representation of this project, including its name,
     * description, and membership list.
     *
     * @return formatted string representation of the project
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("description", description)
                .add("memberships", memberships)
                .toString();
    }

}
