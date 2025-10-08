package loopin.projectbook.logic.commands;

import static loopin.projectbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static loopin.projectbook.logic.commands.CommandTestUtil.showPersonAtIndex;
import static loopin.projectbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static loopin.projectbook.testutil.TypicalPersons.getTypicalProjectBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import loopin.projectbook.model.Model;
import loopin.projectbook.model.ModelManager;
import loopin.projectbook.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalProjectBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getProjectBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
