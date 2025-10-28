package loopin.projectbook.model.project;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the project book.
 * Guarantees: immutable; is valid as declared in {@link #isValidProjectName(String)}
 */
public class ProjectName {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public ProjectName(String name) {
        requireNonNull(name);
        checkArgument(isValidProjectName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidProjectName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ProjectName)) {
            return false;
        }

        ProjectName otherName = (ProjectName) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
