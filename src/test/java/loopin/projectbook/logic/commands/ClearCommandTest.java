package loopin.projectbook.logic.commands;

import static loopin.projectbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static loopin.projectbook.testutil.TypicalPersons.getTypicalProjectBook;

import org.junit.jupiter.api.Test;

import loopin.projectbook.model.Model;
import loopin.projectbook.model.ModelManager;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyProjectBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyProjectBook_success() {
        Model model = new ModelManager(getTypicalProjectBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalProjectBook(), new UserPrefs());
        expectedModel.setProjectBook(new ProjectBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
