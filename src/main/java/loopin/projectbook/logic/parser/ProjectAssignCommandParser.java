package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PROJECT;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.ProjectAssignCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.project.ProjectName;

/**
 * Parses input arguments and creates a new {@link ProjectAssignCommand} object.
 *
 * Supported formats:
 * {@code INDEX project/PROJECT_NAME}
 * {@code n/NAME project/PROJECT_NAME}
 */
public final class ProjectAssignCommandParser implements Parser<ProjectAssignCommand> {

    @Override
    public ProjectAssignCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROJECT, PREFIX_NAME);

        ProjectName projectName = argMultimap.getValue(PREFIX_PROJECT)
                .map(n -> {
                    try {
                        return ParserUtil.parseProjectName(n);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ParseException(ProjectAssignCommand.MESSAGE_USAGE));

        String preamble = argMultimap.getPreamble().trim();
        Optional<String> name = argMultimap.getValue(PREFIX_NAME)
                .mapString(::trim)
                .filter(s -> !s.isEmpty());

        int selectorCount = (preamble.isEmpty() ? 0 : 1) + (name.isPresent() ? 1 : 0);

        if (selectorCount != 1) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, ProjectAssignCommand.MESSAGE_USAGE
            ));
        }

        try {
            if (!preamble.isEmpty()) {
                // index-based assignment
                Index index = ParserUtil.parseIndex(preamble);
                return new ProjectAssignCommand(index, projectName);
            } else {
                // name-based assignment
                return new ProjectAssignCommand(name.get(), projectName);
            }
        } catch (RuntimeException re) {
            if (re.getCause() instanceof ParseException) {
                throw (ParseException) re.getCause();
            }
            throw re;
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, ProjectAssignCommand.MESSAGE_USAGE), pe);
        }
}
