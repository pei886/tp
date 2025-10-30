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
 *
 * Each test is labelled with what it is testing for.
 */
public class ProjectRemoveCommandTest {

    @Test
    public void execute_removeByIndex_success() throws Exception {
        // tests: removing a person (by index) who IS in the project should succeed
        Person alice = new TestPerson("Alice");
        TestProject website = new TestProject(new ProjectName("Website"));
        website.assignPerson(alice);

        ModelStub model = new ModelStub(List.of(alice), List.of(website));

        ProjectRemoveCommand cmd =
                new ProjectRemoveCommand(Index.fromOneBased(1), new ProjectName("Website"));

        CommandResult result = cmd.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Removed"));
        assertFalse(website.hasMember(alice), "Person should have been removed from project.");
        assertTrue(model.setProjectCalled, "Model#setProject should be called to persist change.");
    }

    @Test
    public void execute_removeByName_success() throws Exception {
        // tests: removing a person (by name) who IS in the project should succeed
        Person alice = new TestPerson("Alice");
        TestProject website = new TestProject(new ProjectName("Website"));
        website.assignPerson(alice);

        ModelStub model = new ModelStub(List.of(alice), List.of(website));

        ProjectRemoveCommand cmd =
                new ProjectRemoveCommand("Alice", new ProjectName("Website"));

        CommandResult result = cmd.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Removed"));
        assertFalse(website.hasMember(alice));
    }

    @Test
    public void execute_personNotInProject_throwsCommandException() {
        // tests: trying to remove a person who is NOT in the project should throw
        Person alice = new TestPerson("Alice");
        TestProject website = new TestProject(new ProjectName("Website"));
        // NOTE: we do NOT add Alice to website.members here

        ModelStub model = new ModelStub(List.of(alice), List.of(website));

        ProjectRemoveCommand cmd =
                new ProjectRemoveCommand("Alice", new ProjectName("Website"));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(ProjectRemoveCommand.MESSAGE_NOT_IN, alice.getName()), ex.getMessage());
    }

    @Test
    public void execute_projectNotFound_throwsCommandException() {
        // tests: if the project name cannot be found in model, the command should fail
        Person alice = new TestPerson("Alice");
        ModelStub model = new ModelStub(List.of(alice), List.of()); // no projects

        ProjectRemoveCommand cmd =
                new ProjectRemoveCommand("Alice", new ProjectName("MissingProject"));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_personNameNotFound_throwsCommandException() {
        // tests: name mode, but person name not in filtered list → BaseProjectMemberCommand should fail
        // we simulate it by providing an empty person list
        ModelStub model = new ModelStub(List.of(), List.of(new TestProject(new ProjectName("Website"))));

        ProjectRemoveCommand cmd =
                new ProjectRemoveCommand("Alice", new ProjectName("Website"));

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    // ======================================================================
    // Model stub
    // ======================================================================

    /**
     * Minimal model stub to support:
     * - getFilteredPersonList()
     * - getFilteredProjectList()
     * - findProjectByName(String)
     * - setProject(Project)
     *
     * This is enough for BaseProjectMemberCommand + ProjectRemoveCommand to work.
     */
    private static class ModelStub implements Model {
        private final List<Person> persons;
        private final List<Project> projects;
        boolean setProjectCalled = false;

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
            // in real model, this would replace the project in the list
            setProjectCalled = true;
        }

        // ------------------------------------------------------------------
        // unused Model methods — no-op
        // ------------------------------------------------------------------
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

    // ======================================================================
    // Test doubles for Project and Person
    // ======================================================================

    /**
     * Test double for Project that tracks members in-memory.
     */
    private static class TestProject extends loopin.projectbook.model.project.Project {
        private final List<Person> members = new ArrayList<>();

        TestProject(ProjectName name) {
            super(name);
        }

        @Override
        public boolean hasMember(Person person) {
            return members.contains(person);
        }

        @Override
        public void assignPerson(Person person) {
            members.add(person);
        }

        @Override
        public void removePerson(Person person) {
            members.remove(person);
        }
    }

    /**
     * Test double for Person with a simple name-based identity.
     */
    private static class TestPerson extends loopin.projectbook.model.person.Person {
        private final String rawName;

        TestPerson(String name) {
            super(
                    new loopin.projectbook.model.person.Name(name),
                    Optional.empty(),
                    new loopin.projectbook.model.person.Email(name.toLowerCase() + "@example.com"),
                    Optional.empty(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            this.rawName = name;
        }

        @Override
        public boolean isSamePerson(loopin.projectbook.model.person.Person otherPerson) {
            return otherPerson.getName().toString().equalsIgnoreCase(rawName);
        }
    }
}
