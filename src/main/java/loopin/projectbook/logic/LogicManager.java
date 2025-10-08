package loopin.projectbook.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.logic.parser.ProjectBookParser;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final ProjectBookParser projectBookParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        projectBookParser = new ProjectBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = projectBookParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveProjectBook(model.getProjectBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyProjectBook getProjectBook() {
        return model.getProjectBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getProjectBookFilePath() {
        return model.getProjectBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
