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
 * Tests for {@link ProjectAssignCommand}.
 */
public class ProjectAssignCommandTest {

    @Test
    public void execute_assignByIndex_success() throws Exception {
        // tests: assigning person at index 1 to existing project
        Person p = new Person("Alice");
        Project proj = new Project(new ProjectName("Website"));
        ModelStub model = new ModelStub(List.of(p), List.of(proj));

        ProjectAssignCommand cmd = new ProjectAssignCommand(Index.fromOneBased(1), new ProjectName("Website"));
        CommandResult res = cmd.execute(model);

        assertTrue(res.getFeedbackToUser().contains("Assigned"));
        assertTrue(proj.members.contains(p));
    }

    @Test
    public void execute_assignByName_success() throws Exception {
        // tests: assigning person by name to project
        Person p = new Person("Alice");
        Project proj = new Project(new ProjectName("Website"));
        ModelStub model = new ModelStub(List.of(p), List.of(proj));

        ProjectAssignCommand cmd = new ProjectAssignCommand("Alice", new ProjectName("Website"));
        CommandResult res = cmd.execute(model);

        assertTrue(res.getFeedbackToUser().contains("Assigned"));
        assertTrue(proj.members.contains(p));
    }

    @Test
    public void execute_personAlreadyInProject_throws() {
        // tests: assigning again -> error
        Person p = new Person("Alice");
        Project proj = new Project(new ProjectName("Website"));
        proj.assignPerson(p);

        ModelStub model = new ModelStub(List.of(p), List.of(proj));
        ProjectAssignCommand cmd = new ProjectAssignCommand("Alice", new ProjectName("Website"));
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().contains("already"));
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
            // in real model, this would replace in list
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
    // minimal Project + Person stand-ins
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
