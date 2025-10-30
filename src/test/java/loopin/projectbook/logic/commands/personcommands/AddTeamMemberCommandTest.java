package loopin.projectbook.logic.commands.personcommands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.ReadOnlyUserPrefs;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.testutil.PersonBuilder;

public class AddTeamMemberCommandTest {

    @Test
    public void constructor_nullTeamMember_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTeamMemberCommand(null));
    }

    @Test
    public void execute_teamMemberAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        TeamMember validTeamMember = new PersonBuilder()
                .withName("Jane Tan")
                .withEmail("jane@example.com")
                .withPhone("98765432")
                .buildTeamMember("Marketing");

        AddTeamMemberCommand command = new AddTeamMemberCommand(validTeamMember);
        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(AddTeamMemberCommand.MESSAGE_SUCCESS,
                        Messages.formatPerson(validTeamMember)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTeamMember), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicateTeamMember_throwsCommandException() {
        TeamMember validTeamMember = new PersonBuilder().buildTeamMember("Logistics");
        AddTeamMemberCommand command = new AddTeamMemberCommand(validTeamMember);
        ModelStub modelStub = new ModelStubWithPerson(validTeamMember);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        TeamMember alice = new PersonBuilder().withName("Alice").buildTeamMember("Marketing");
        TeamMember bob = new PersonBuilder().withName("Bob").buildTeamMember("Finance");

        AddTeamMemberCommand addAliceCommand = new AddTeamMemberCommand(alice);
        AddTeamMemberCommand addBobCommand = new AddTeamMemberCommand(bob);

        // same object is true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values is true
        AddTeamMemberCommand addAliceCommandCopy = new AddTeamMemberCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types is false
        assertFalse(addAliceCommand.equals(1));

        // null is false
        assertFalse(addAliceCommand.equals(null));

        // different person is false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        TeamMember alice = (TeamMember) new PersonBuilder().withName("Alice").build();
        AddTeamMemberCommand command = new AddTeamMemberCommand(alice);

        String str = command.toString();
        assertTrue(str.contains("AddTeamMemberCommand"));
        assertTrue(str.contains("Alice"));
    }

    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getProjectBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProjectBookFilePath(Path projectBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProjectBook(ReadOnlyProjectBook projectBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProjectBook getProjectBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPersonInPlace(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Project> getFilteredProjectList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Project> findProjectByName(String name) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredProjectList(Predicate<Project> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

    }

    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyProjectBook getProjectBook() {
            return new ProjectBook();
        }
    }
}
