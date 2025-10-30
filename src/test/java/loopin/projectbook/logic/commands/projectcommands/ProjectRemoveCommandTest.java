package loopin.projectbook.logic.commands.projectcommands;

import static javafx.collections.FXCollections.observableArrayList;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.commons.core.index.Index;
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
 * Tests for {@link ProjectRemoveCommand}.
 *
 * What we test:
 *  - success (by index and by name)
 *  - person not in project -> throws with MESSAGE_NOT_IN
 *  - project not found -> throws
 *  - person name not found -> throws
 *  - side effects: project loses member; (no requirement to remove back-reference from person)
 */
public class ProjectRemoveCommandTest {

    // --- helpers: real domain factories ------------------------------------------------------------

    private static Project proj(String name) {
        return new Project(new ProjectName(name), new Description("test project"));
    }

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
    public void execute_removeByIndex_success() throws Exception {
        // Removing a person (by index) who IS in the project should succeed
        Person alice = vol("Alice");
        Project website = proj("Website");
        website.assignPerson(alice);

        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList(website));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand(Index.fromOneBased(1), new ProjectName("Website"));
        CommandResult result = cmd.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Removed"));
        assertFalse(website.hasMember(alice), "Alice should be removed from project.");
        assertTrue(model.setProjectCalled, "Model#setProject should be called.");
        // NOTE: command does not remove person's back-reference; do NOT assert that here.
    }

    @Test
    public void execute_removeByName_success() throws Exception {
        // Removing a person (by name) who IS in the project should succeed
        Person alice = vol("Alice");
        Project website = proj("Website");
        website.assignPerson(alice);

        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList(website));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Alice", new ProjectName("Website"));
        CommandResult result = cmd.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Removed"));
        assertFalse(website.hasMember(alice));
    }

    @Test
    public void execute_personNotInProject_throwsCommandException() {
        // Trying to remove a person who is NOT in the project should throw MESSAGE_NOT_IN
        Person alice = vol("Alice");
        Project website = proj("Website");
        // not adding Alice to website

        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList(website));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Alice", new ProjectName("Website"));
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(ProjectRemoveCommand.MESSAGE_NOT_IN, alice.getName()), ex.getMessage());
    }

    @Test
    public void execute_projectNotFound_throwsCommandException() {
        // If the project name cannot be found, the command should fail
        Person alice = vol("Alice");
        ModelStub model = new ModelStub(observableArrayList(alice), observableArrayList());

        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Alice", new ProjectName("Missing Project"));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_personNameNotFound_throwsCommandException() {
        // Name mode, but person is not resolvable → throws
        Project website = proj("Website");
        ModelStub model = new ModelStub(observableArrayList(), observableArrayList(website));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Alice", new ProjectName("Website"));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    // --- model stub -------------------------------------------------------------------------------

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

        // Finding / setting projects (with same normalization as production)
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
        @Override public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {}
        @Override public void updateFilteredProjectList(java.util.function.Predicate<Project> predicate) {}

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
