package loopin.projectbook.logic.commands.projectcommands;

import static javafx.collections.FXCollections.observableArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static loopin.projectbook.commons.core.index.Index.fromOneBased;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.ReadOnlyUserPrefs;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.model.project.Description;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Tests for {@link ProjectAssignCommand}.
 *
 * What we test:
 *  - success (by index and by name)
 *  - already a member -> throws with MESSAGE_ALREADY
 *  - project not found -> throws
 *  - person name not found -> throws
 *  - side effects: project gains member; person gains back-reference
 */
public class ProjectAssignCommandTest {

    // --- helpers: real domain factories ------------------------------------------------------------

    /** Create a real Project using your actual constructor (ProjectName, Description). */
    private static Project proj(String name) {
        return new Project(new ProjectName(name), new Description("test project"));
    }

    /** Create a simple concrete Person (Volunteer) for tests. */
    private static Volunteer vol(String name) {
        return new Volunteer(
                new Name(name),
                Optional.empty(), // phone
                new Email(name.toLowerCase().replace(" ", "") + "@ex.com"),
                Optional.empty(), // telegram
                new HashSet<Remark>(),
                new ArrayList<Project>()
        );
    }

    // --- tests -------------------------------------------------------------------------------------

    @Test
    public void execute_assignByIndex_success() throws Exception {
        // Assigning a person (by index) who is NOT yet in project should add and succeed
        Person alice = vol("Alice");
        Project website = proj("Website");

        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList(website));

        ProjectAssignCommand cmd = new ProjectAssignCommand(fromOneBased(1), new ProjectName("Website"));
        CommandResult result = cmd.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Assigned"));
        assertTrue(website.hasMember(alice), "Alice should be a member after assign.");
        // Back-reference should be added by the command
        assertTrue(alice.getProjects().contains(website), "Person should reference the assigned project.");
        assertTrue(model.setProjectCalled, "Model#setProject should be called.");
    }

    @Test
    public void execute_assignByName_success() throws Exception {
        // Assigning a person (by name) who is NOT yet in project should add and succeed
        Person alice = vol("Alice");
        Project website = proj("Website");

        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList(website));

        ProjectAssignCommand cmd = new ProjectAssignCommand("Alice", new ProjectName("Website"));
        CommandResult result = cmd.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Assigned"));
        assertTrue(website.hasMember(alice));
        assertTrue(alice.getProjects().contains(website));
    }

    @Test
    public void execute_alreadyInProject_throwsCommandException() {
        // Assigning a person who is already in project should throw MESSAGE_ALREADY
        Person alice = vol("Alice");
        Project website = proj("Website");
        website.assignPerson(alice);

        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList(website));

        ProjectAssignCommand cmd = new ProjectAssignCommand("Alice", new ProjectName("Website"));
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().contains("already"), "Should indicate already in project.");
        // Membership should remain unchanged
        assertTrue(website.hasMember(alice));
    }

    @Test
    public void execute_projectNotFound_throwsCommandException() {
        // If project cannot be found by name, command fails
        Person alice = vol("Alice");
        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList());

        ProjectAssignCommand cmd = new ProjectAssignCommand("Alice", new ProjectName("Missing Project"));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_personNameNotFound_throwsCommandException() {
        // Name mode but person not resolvable should throw via BaseProjectMemberCommand
        Project website = proj("Website");
        ModelStub model = new ModelStub(observableArrayList(), observableArrayList(website));

        ProjectAssignCommand cmd = new ProjectAssignCommand("Alice", new ProjectName("Website"));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    // --- model stub -------------------------------------------------------------------------------

    /**
     * Minimal stub implementing ALL methods of Model used/required by tests.
     * Note: findProjectByName applies the same normalization logic as production
     * (trim + collapse internal whitespace + case-insensitive).
     */
    private static final class ModelStub implements Model {
        private final ObservableList<Person> persons;
        private final ObservableList<Project> projects;
        boolean setProjectCalled = false;

        ModelStub(ObservableList<Person> persons, ObservableList<Project> projects) {
            this.persons = persons;
            this.projects = projects;
        }

        // Persons / Projects lists
        @Override public ObservableList<Person> getFilteredPersonList() { return persons; }
        @Override public ObservableList<Project> getFilteredProjectList() { return projects; }

        // Finding / setting projects
        @Override
        public java.util.Optional<Project> findProjectByName(String name) {
            String needle = normalize(name);
            return projects.stream()
                    .filter(p -> normalize(p.getName().toString()).equals(needle))
                    .findFirst();
        }
        private static String normalize(String s) {
            return s == null ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase();
        }
        @Override public void setProject(Project project) { setProjectCalled = true; }

        // Predicates
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {}
        @Override public void updateFilteredProjectList(Predicate<Project> predicate) {}

        // People ops (not used here)
        @Override public boolean hasPerson(Person person) { return false; }
        @Override public void deletePerson(Person target) {}
        @Override public void addPerson(Person person) {}
        @Override public void setPerson(Person target, Person editedPerson) {}

        // Project ops (not used here)
        @Override public boolean hasProject(Project project) { return false; }
        @Override public void addProject(Project project) {}
        @Override public void deleteProject(Project project) {}

        // User prefs / GUI (required by interface)
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {}
        @Override public ReadOnlyUserPrefs getUserPrefs() { return null; }
        @Override public GuiSettings getGuiSettings() { return new GuiSettings(); }
        @Override public void setGuiSettings(GuiSettings guiSettings) {}

        // File path / book (required by interface)
        @Override public Path getProjectBookFilePath() { return null; }
        @Override public void setProjectBookFilePath(Path projectBookFilePath) {}
        @Override public void setProjectBook(ReadOnlyProjectBook projectBook) {}
        @Override public ReadOnlyProjectBook getProjectBook() { return null; }
    }
}
