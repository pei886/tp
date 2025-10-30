package loopin.projectbook.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import loopin.projectbook.logic.parser.Prefix;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX = "The project index provided is invalid";
    public static final String MESSAGE_PROJECT_NOT_FOUND_BY_NAME = "No project found with name: %s";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String formatPerson(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                //.append("; Address: ")
                //.append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns a formatted string representation of the given {@code Project}.
     * The formatted string includes the project's name, description, and creation timestamp,
     * separated by semicolons for readability.
     *
     * @param project the project to format; must not be {@code null}
     * @return a human-readable string summarizing the project's key details
     */

    public static String formatProject(Project project) {
        final StringBuilder builder = new StringBuilder();
        builder.append(project.getName())
                .append("; Description: ")
                .append(project.getDescription())
                .append("; Created at: ")
                .append(project.getCreatedAt());
        return builder.toString();
    }
}
