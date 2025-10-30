package loopin.projectbook.logic.parser.project;

import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PROJECT;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.projectcommands.ProjectAssignCommand;
import loopin.projectbook.logic.parser.ArgumentMultimap;
import loopin.projectbook.logic.parser.ArgumentTokenizer;
import loopin.projectbook.logic.parser.Parser;
import loopin.projectbook.logic.parser.ParserUtil;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.project.ProjectName;

/**
 * Parses input arguments and creates a new {@link ProjectAssignCommand}.
 *
 * Supported forms:
 *   INDEX project/PROJECT_NAME
 *   n/NAME project/PROJECT_NAME
 */
public final class ProjectAssignCommandParser implements Parser<ProjectAssignCommand> {

    @Override
    public ProjectAssignCommand parse(String args) throws ParseException {
        final String trimmed = args == null ? "" : args.trim();
        final ArgumentMultimap map = ArgumentTokenizer.tokenize(" " + trimmed, PREFIX_NAME, PREFIX_PROJECT);

        final ProjectName projectName = requireProjectName(map, ProjectAssignCommand.MESSAGE_USAGE);

        if (isNameMode(trimmed)) {
            final String name = map.getValue(PREFIX_NAME)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .orElseThrow(() -> new ParseException(ProjectAssignCommand.MESSAGE_USAGE));
            return new ProjectAssignCommand(name, projectName);
        }

        final Index index = parseIndexFromPreamble(map, ProjectAssignCommand.MESSAGE_USAGE);
        return new ProjectAssignCommand(index, projectName);
    }

    // ---- helpers ----

    private static boolean isNameMode(String trimmedArgs) {
        return trimmedArgs.startsWith(PREFIX_NAME.getPrefix());
    }

    private static ProjectName requireProjectName(ArgumentMultimap map, String usage) throws ParseException {
        String raw = map.getValue(PREFIX_PROJECT)
                .orElseThrow(() -> new ParseException(usage));
        return ParserUtil.parseProjectName(raw);
    }

    private static Index parseIndexFromPreamble(ArgumentMultimap map, String usage) throws ParseException {
        String preamble = map.getPreamble().trim();
        if (preamble.isEmpty()) {
            throw new ParseException(usage);
        }
        return ParserUtil.parseIndex(preamble);
    }
}
