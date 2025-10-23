package loopin.projectbook.model.project;

import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.person.Person;

/**
 * Represents a project in the ProjectBook system.
 * <p>
 * A Project contains identifying information such as its name and description,
 * timestamps for creation and last update, and a list of {@link Membership}
 * objects representing people who are part of the project.
 */
public class Project {

    //    private final UUID id;
    private final ProjectName name;
    private final Description description;
    private final LocalDateTime createdAt;
    private LastUpdate lastUpdate;

    private final List<Membership> memberships = new ArrayList<>();

    /**
     * Creates a new Project with the given id, name, and description.
     *
     * @param name        name of the project
     * @param description short description of the project
     */
    public Project(ProjectName name, Description description) {
        requireAllNonNull(name, description);
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    //    /** @return the unique ID of this project */
    //    public UUID getId() {
    //        return id;
    //    }

    /** @return the name of this project */
    public ProjectName getName() {
        return name;
    }

    /** @return the description of this project */
    public Description getDescription() {
        return description;
    }

    /** @return the timestamp when this project was created */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void recordUpdate(LastUpdate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LastUpdate getLatestUpdate() {
        return lastUpdate;
    }

    /**
     * Returns all people who are members of this project.
     *
     * @return a list of {@link Person} objects representing all members
     */
    public List<Person> getAllPeople() {
        return java.util.Collections.unmodifiableList(
                memberships.stream()
                        .map(Membership::getPerson)
                        .collect(Collectors.toList())
        );
    }

//    /**
//     * Updates the timestamp indicating when this project was last modified.
//     *
//     */
//    public void touch() {
//        this.updatedAt = LocalDateTime.now();
//    }

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

    /**
     * Returns {@code true} if the specified {@code Person} is a member of this project.
     *
     * @param p the person to check for membership
     * @return {@code true} if the person is already in the project and {@code false} otherwise
     */
    public boolean hasMember(Person p) {
        return memberships.stream().anyMatch(m -> m.getPerson().isSamePerson(p));
    }

    /**
     * Assigns the specified {@code Person} to this project as a new member.
     *
     * Updates the project's last modified timestamp.
     *
     * @param p the person to assign
     * @throws IllegalStateException if the person is already a member of the project
     */
    public void assignPerson(Person p) {
        if (hasMember(p)) {
            throw new IllegalStateException("Person is already in this project.");
        }
        memberships.add(new Membership(p));
        LastUpdate update = LastUpdate.memberAdded(p);
        recordUpdate(update);
    }

    /**
     * Removes the specified {@code Person} from this project.
     * Updates the project's last modified timestamp.
     *
     * @param p the person to remove
     * @throws IllegalStateException if the person is not currently a member of the project
     */
    public void removePerson(Person p) {
        boolean removed = memberships.removeIf(m -> m.getPerson().isSamePerson(p));
        if (!removed) {
            throw new IllegalStateException("Person is not in this project.");
        }
        LastUpdate update = LastUpdate.memberRemoved(p);
        recordUpdate(update);
    }

}
