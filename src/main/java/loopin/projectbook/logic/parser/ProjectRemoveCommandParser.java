package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PROJECT;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.ProjectRemoveCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;

public final class ProjectRemoveCommandParser implements Parser<ProjectRemoveCommand> {

    @Override
    public ProjectRemoveCommand parse(String args) throws ParseException {
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROJECT);

            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
            String projectName = argMultimap.getValue(PREFIX_PROJECT)
                    .map(n -> {
                        try { return ParserUtil.parseProjectName(n); }
                        catch (ParseException e) { throw new RuntimeException(e); }
                    })
                    .orElseThrow(() -> new ParseException(ProjectRemoveCommand.MESSAGE_USAGE));

            return new ProjectRemoveCommand(index, projectName);

        } catch (RuntimeException re) {
            if (re.getCause() instanceof ParseException) {
                throw (ParseException) re.getCause();
            }
            throw re;
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProjectRemoveCommand.MESSAGE_USAGE), pe);
        }
    }
}
