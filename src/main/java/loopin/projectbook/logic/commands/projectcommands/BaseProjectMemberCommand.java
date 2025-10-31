package loopin.projectbook.logic.commands.projectcommands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_AMBIGUOUS_NAME;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static loopin.projectbook.logic.Messages.MESSAGE_NO_PERSON;
import static loopin.projectbook.logic.Messages.MESSAGE_NO_PROJECT;

import java.util.List;
import java.util.Locale;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Abstract base for project membership commands that need to resolve a {@link Person}
 * either by exact (case-insensitive) name or by index in the current filtered list.
 *
 * Subclasses call {@link #resolveTargetPerson(Model, String, Index)} to obtain the target person
 * using consistent error messages and behavior.
 */
abstract class BaseProjectMemberCommand extends Command {

    /**
     * Resolves a target person by either:
     *   - an exact (case-insensitive) name match, if {@code name} is non-{@code null}; or
     *   - an index into the current filtered person list, if {@code name} is {@code null}.
     *
     * @param model backing model; must not be {@code null}
     * @param name case-insensitive exact name to match, or {@code null} to use {@code index}
     * @param index index into the current filtered person list (used if {@code name} is {@code null})
     * @return the uniquely resolved {@link Person}
     * @throws CommandException if the name is not found or ambiguous, or if the index is out of range
     */
    protected final Person resolveTargetPerson(Model model, String name, Index index) throws CommandException {
        requireNonNull(model);
        if (name != null) {
            return resolveByExactName(model, name);
        }
        List<Person> shown = model.getFilteredPersonList();
        if (index.getZeroBased() >= shown.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return shown.get(index.getZeroBased());
    }

    /**
     * Returns the unique person whose name exactly matches {@code name} under trim + lowercasing comparison.
     *
     * @param model backing model; must not be {@code null}
     * @param name person name to match (case-insensitive, exact)
     * @return the unique matching {@link Person}
     * @throws CommandException if none or more than one person matches
     */
    private Person resolveByExactName(Model model, String name) throws CommandException {
        String needle = name.trim().toLowerCase(Locale.ROOT);
        List<Person> pool = model.getProjectBook().getPersonList();
        List<Person> matches = pool.stream()
                .filter(p -> p.getName() != null
                        && p.getName().toString().trim().toLowerCase(Locale.ROOT).equals(needle))
                .toList();

        if (matches.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_PERSON, name));
        }
        if (matches.size() > 1) {
            throw new CommandException(String.format(MESSAGE_AMBIGUOUS_NAME, name));
        }
        return matches.get(0);
    }

    /**
     * Resolves and returns the {@link Project} identified by the given {@link ProjectName}.
     *
     * This helper performs a case-insensitive lookup in the model and standardizes
     * the "project not found" error handling used by all project-membership commands.
     * It centralizes common logic from {@link ProjectAssignCommand} and
     * {@link ProjectRemoveCommand} to maintain consistent error messages and behavior.
     *
     * @param model the backing {@link Model} used to access the current project list; must not be {@code null}
     * @param projectName the validated {@link ProjectName} to locate; must not be {@code null}
     * @return the unique {@link Project} whose name matches {@code projectName}
     * @throws CommandException if no project with the specified name exists
     */
    protected final Project resolveProjectByName(Model model, ProjectName projectName) throws CommandException {
        requireNonNull(model);
        requireNonNull(projectName);
        String lookup = projectName.toString();
        return model.findProjectByName(lookup)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_NO_PROJECT, lookup)));
    }
}
