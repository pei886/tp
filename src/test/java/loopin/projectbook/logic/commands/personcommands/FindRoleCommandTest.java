package loopin.projectbook.logic.commands.personcommands;

import static loopin.projectbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static loopin.projectbook.testutil.TypicalPersons.getTypicalProjectBook;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.ModelManager;
import loopin.projectbook.model.UserPrefs;
import loopin.projectbook.model.person.RoleMatchesPredicate;

public class FindRoleCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalProjectBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalProjectBook(), new UserPrefs());
    }

    @Test
    public void execute_noRoleFound_emptyList() {
        RoleMatchesPredicate predicate = new RoleMatchesPredicate('x'); // invalid role
        FindRoleCommand command = new FindRoleCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = "0 persons listed!";

        // Use CommandResult that matches actual flags returned by the command
        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage,
                false,
                false,
                model.getFilteredPersonList().size() > 0,
                false
        );

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_teamMembers_found() {
        RoleMatchesPredicate predicate = new RoleMatchesPredicate('t'); // team members
        FindRoleCommand command = new FindRoleCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        int size = expectedModel.getFilteredPersonList().size();
        String expectedMessage = size + " persons listed!";

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage,
                false,
                false,
                true,
                false
        );

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_volunteers_found() {
        RoleMatchesPredicate predicate = new RoleMatchesPredicate('v'); // volunteers
        FindRoleCommand command = new FindRoleCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        int size = expectedModel.getFilteredPersonList().size();
        String expectedMessage = size + " persons listed!";

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage,
                false,
                false,
                true,
                false
        );

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

}
