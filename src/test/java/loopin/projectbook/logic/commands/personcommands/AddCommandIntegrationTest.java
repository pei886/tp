package loopin.projectbook.logic.commands.personcommands;

import static loopin.projectbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static loopin.projectbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static loopin.projectbook.testutil.TypicalPersons.getTypicalProjectBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.Messages;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.ModelManager;
import loopin.projectbook.model.UserPrefs;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {
    private static final boolean IS_PERSON_VIEW = true;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalProjectBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getProjectBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddVolunteerCommand((Volunteer) validPerson), model,
                String.format(AddVolunteerCommand.MESSAGE_SUCCESS, Messages.formatPerson(validPerson)),
                expectedModel, IS_PERSON_VIEW);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getProjectBook().getPersonList().get(0);
        assertCommandFailure(new AddVolunteerCommand((Volunteer) personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
