package loopin.projectbook.logic.commands.projectcommands;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Tests for {@link ProjectRemoveCommand}.
 */
public class ProjectRemoveCommandTest {

    @Test
    public void execute_removeByIndex_success() throws Exception {
        // tests: remove person at index 1 from project
        Person p = new Person("Alice");
        Project proj = new Project(new ProjectName("Website"));
        proj.assignPerson(p);

        ModelStub model = new ModelStub(List.of(p), List.of(proj));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand(Index.fromOneBased(1), new ProjectName("Website"));
        CommandResult res = cmd.execute(model);

        assertTrue(res.getFeedbackToUser().toLowerCase().contains("removed")
                || res.getFeedbackToUser().toLowerCase().contains("unassigned"));
        assertFalse(proj.members.contains(p));
    }

    @Test
    public void execute_removePersonNotInProject_throws() {
        // tests: removing when person not in project -> error
        Person p = new Person("Alice");
        Project proj = new Project(new ProjectName("Website"));

        ModelStub model = new ModelStub(List.of(p), List.of(proj));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand(Index.fromOneBased(1), new ProjectName("Website"));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_removeByName_success() throws Exception {
        // tests: remove by name mode
        Person p = new Person("Alice");
        Project proj = new Project(new ProjectName("Website"));
        proj.assignPerson(p);
        ModelStub model = new ModelStub(List.of(p), List.of(proj));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Alice", new ProjectName("Website"));
        cmd.execute(model);

        assertFalse(proj.members.contains(p));
    }

    // ----------------------------------------------------------------------
    // model stub
    // ----------------------------------------------------------------------
    private static class ModelStub implements Model {
        private final List<Person> persons;
        private final List<Project> projects;

        ModelStub(List<Person> persons, List<Project> projects) {
            this.persons = persons;
            this.projects = projects;
        }

        @Override
        public List<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public List<Project> getFilteredProjectList() {
            return projects;
        }

        @Override
        public Optional<Project> findProjectByName(String name) {
            return projects.stream()
                    .filter(p -> p.getName().toString().equalsIgnoreCase(name))
                    .findFirst();
        }

        @Override
        public void setProject(Project project) {
            // no-op
        }

        // ---- unused ----
        @Override public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {}
        @Override public boolean hasPerson(Person person) { return false; }
        @Override public void setPerson(Person target, Person editedPerson) {}
        @Override public void addPerson(Person person) {}
        @Override public void deletePerson(Person target) {}
        @Override public void deleteProject(Project project) {}
        @Override public boolean hasProject(Project project) { return false; }
        @Override public loopin.projectbook.ReadOnlyProjectBook getProjectBook() { return null; }
        @Override public void setProjectBook(loopin.projectbook.ReadOnlyProjectBook projectBook) {}
    }

    // ----------------------------------------------------------------------
    // minimal Project + Person
    // ----------------------------------------------------------------------
    private static class Project extends loopin.projectbook.model.project.Project {
        final List<Person> members = new ArrayList<>();

        Project(ProjectName name) {
            super(name);
        }

        @Override
        public boolean hasMember(Person p) {
            return members.contains(p);
        }

        @Override
        public void assignPerson(Person p) {
            members.add(p);
        }

        public void removePerson(Person p) {
            members.remove(p);
        }
    }

    private static class Person extends loopin.projectbook.model.person.Person {
        private final String name;

        Person(String name) {
            super(new loopin.projectbook.model.person.Name(name),
                    Optional.empty(),
                    new loopin.projectbook.model.person.Email(name.toLowerCase() + "@example.com"),
                    Optional.empty(),
                    new ArrayList<>(),
                    new ArrayList<>());
            this.name = name;
        }

        @Override
        public boolean isSamePerson(loopin.projectbook.model.person.Person otherPerson) {
            return otherPerson.getName().toString().equalsIgnoreCase(this.name);
        }
    }
}
