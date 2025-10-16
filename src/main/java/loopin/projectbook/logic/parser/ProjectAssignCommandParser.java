package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PROJECT;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.ProjectAssignCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.project.ProjectName;

/**
 * Parses input arguments and creates a new {@link ProjectAssignCommand} object.
 * Expected format: {@code INDEX project/PROJECT_NAME}
 *
 */
public final class ProjectAssignCommandParser implements Parser<ProjectAssignCommand> {

    @Override
    public ProjectAssignCommand parse(String args) throws ParseException {
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROJECT);

            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

            ProjectName projectName = argMultimap.getValue(PREFIX_PROJECT)
                    .map(n -> {
                        try { return ParserUtil.parseProjectName(n); }
                        catch (ParseException e) { throw new RuntimeException(e); }
                    })
                    .orElseThrow(() -> new ParseException(ProjectAssignCommand.MESSAGE_USAGE));

            return new ProjectAssignCommand(index, projectName);

        } catch (RuntimeException re) {
            if (re.getCause() instanceof ParseException) {
                throw (ParseException) re.getCause();
            }
            throw re;
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProjectAssignCommand.MESSAGE_USAGE), pe);
        }
    }
}
