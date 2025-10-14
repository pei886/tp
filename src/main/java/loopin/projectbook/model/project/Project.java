package loopin.projectbook.model.project;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.person.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

public class Project {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Membership> memberships = new ArrayList<>();

    public Project(UUID id, String name, String description) {
        requireAllNonNull(id, name, description);
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public List<Person> getAllPeople() {
        return memberships.stream()
                .map(Membership::getPerson)
                .collect(Collectors.toList());
    }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void addMembership(Membership membership) {
        memberships.add(membership);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("description", description)
                .add("memberships", memberships)
                .toString();
    }

}
