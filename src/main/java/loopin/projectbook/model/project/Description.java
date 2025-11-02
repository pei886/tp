package loopin.projectbook.model.project;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.commons.util.AppUtil.checkArgument;

/**
 * Represents a Project's description in the project book.
 * Guarantees: is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_CONSTRAINTS =
            "Descriptions can contain any characters, but it should not be blank";

    /*
     * Descriptions are optional (can be blank).
     * If not blank, the first character must not be a whitespace.
     */
    public static final String VALIDATION_REGEX = "|[^ \\n][\\s\\S]*";

    private String value;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description.
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_CONSTRAINTS);
        value = description;
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String description) {
        return description.matches(VALIDATION_REGEX) && !description.isEmpty();
    }

    /**
     * Sets the description value.
     *
     * @param description A valid description (can be blank).
     */
    public void setDescription(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_CONSTRAINTS);
        this.value = description;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Description)) {
            return false;
        }

        Description otherDescription = (Description) other;
        return value.equals(otherDescription.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
