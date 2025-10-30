package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.logic.commands.ClearCommand;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.DeleteCommand;
import loopin.projectbook.logic.commands.EditCommand;
import loopin.projectbook.logic.commands.ExitCommand;
import loopin.projectbook.logic.commands.HelpCommand;
import loopin.projectbook.logic.commands.ListCommand;
import loopin.projectbook.logic.commands.personcommands.AddOrgMemberCommand;
import loopin.projectbook.logic.commands.personcommands.AddProjectCommand;
import loopin.projectbook.logic.commands.personcommands.AddTeamMemberCommand;
import loopin.projectbook.logic.commands.personcommands.AddVolunteerCommand;
import loopin.projectbook.logic.commands.personcommands.FindCommand;
import loopin.projectbook.logic.commands.personcommands.FindRoleCommand;
import loopin.projectbook.logic.commands.personcommands.RemarkCommand;
import loopin.projectbook.logic.commands.personcommands.ResolveRemarkCommand;
import loopin.projectbook.logic.commands.projectcommands.ProjectAssignCommand;
import loopin.projectbook.logic.commands.projectcommands.ProjectFindCommand;
import loopin.projectbook.logic.commands.projectcommands.ProjectListCommand;
import loopin.projectbook.logic.commands.projectcommands.ProjectRemoveCommand;
import loopin.projectbook.logic.commands.projectcommands.ViewProjectCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.logic.parser.person.AddOrgMemberCommandParser;
import loopin.projectbook.logic.parser.project.AddProjectCommandParser;
import loopin.projectbook.logic.parser.person.AddTeamMemberCommandParser;
import loopin.projectbook.logic.parser.person.AddVolunteerCommandParser;
import loopin.projectbook.logic.parser.person.FindCommandParser;
import loopin.projectbook.logic.parser.person.FindRoleCommandParser;
import loopin.projectbook.logic.parser.person.RemarkCommandParser;
import loopin.projectbook.logic.parser.person.ResolveRemarkCommandParser;
import loopin.projectbook.logic.parser.project.ProjectAssignCommandParser;
import loopin.projectbook.logic.parser.project.ProjectFindCommandParser;
import loopin.projectbook.logic.parser.project.ProjectRemoveCommandParser;
import loopin.projectbook.logic.parser.project.ViewProjectCommandParser;

/**
 * Parses user input.
 */
public class ProjectBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(ProjectBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case RemarkCommand.COMMAND_WORD:
            return new RemarkCommandParser().parse(arguments);

        case ResolveRemarkCommand.COMMAND_WORD:
            return new ResolveRemarkCommandParser().parse(arguments);

        case AddTeamMemberCommand.COMMAND_WORD:
            return new AddTeamMemberCommandParser().parse(arguments);

        case AddOrgMemberCommand.COMMAND_WORD:
            return new AddOrgMemberCommandParser().parse(arguments);

        case FindRoleCommand.COMMAND_WORD:
            return new FindRoleCommandParser().parse(arguments);

        case "project": {
            final String trimmed = arguments.trim();
            if (trimmed.isEmpty()) {
                throw new ParseException("Unknown project subcommand. Try:\n"
                        + ProjectAssignCommand.MESSAGE_USAGE + "\n"
                        + ProjectRemoveCommand.MESSAGE_USAGE);
            }

            // first token = subcommand ("assign"/"remove"); rest = sub-args
            String[] parts = trimmed.split("\\s+", 2);
            String sub = parts[0];
            String rest = parts.length > 1 ? parts[1] : "";

            switch (sub) {
            case AddProjectCommand.SUBCOMMAND:
                return new AddProjectCommandParser().parse(" " + rest);
            case ProjectAssignCommand.SUBCOMMAND:
                return new ProjectAssignCommandParser().parse(rest);
            case ProjectRemoveCommand.SUBCOMMAND:
                return new ProjectRemoveCommandParser().parse(rest);
            case ViewProjectCommand.SUBCOMMAND:
                return new ViewProjectCommandParser().parse(" " + rest);
            case ProjectListCommand.SUBCOMMAND:
                logger.info("Projects listed");
                return new ProjectListCommand();
            case ProjectFindCommand.SUBCOMMAND:
                logger.info("Finding Projects, going to parse keywords now");
                return new ProjectFindCommandParser().parse("" + rest);
            default:
                throw new ParseException("Unknown project subcommand. Try:\n"
                        + ProjectFindCommand.MESSAGE_USAGE + "\n"
                        + ProjectListCommand.MESSAGE_USAGE + "\n"
                        + ProjectAssignCommand.MESSAGE_USAGE + "\n"
                        + ProjectRemoveCommand.MESSAGE_USAGE + "\n"
                        + ViewProjectCommand.MESSAGE_USAGE);
            }
        }
        case AddVolunteerCommand.COMMAND_WORD:
            return new AddVolunteerCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
