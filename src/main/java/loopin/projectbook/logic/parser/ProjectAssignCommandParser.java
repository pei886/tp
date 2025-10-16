package loopin.projectbook.logic.parser;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.ProjectAssignCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;

import static loopin.projectbook.logic.parser.Prefix.PREFIX_PROJECT;

public final class ProjectAssignCommandParser implements Parser<ProjectAssignCommand> {
    @Override
    public ProjectAssignCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_PROJECT);
        Index index = ParserUtil.parseIndex(map.getPreamble());
        String name = map.getValue(PREFIX_PROJECT)
                .map(n -> {
                    try { return ParserUtil.parseProjectName(n); }
                    catch (ParseException e) { throw new RuntimeException(e); }
                })
                .orElseThrow(() -> new ParseException(ProjectAssignCommand.MESSAGE_USAGE));
        return new ProjectAssignCommand(index, name);
    }
}
