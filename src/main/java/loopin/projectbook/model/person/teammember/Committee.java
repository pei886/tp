package loopin.projectbook.model.person.teammember;

import static java.util.Objects.requireNonNull;

/**
 * Represents a committee to which a {@code TeamMember} belongs in the project book.
 * A {@code Committee} is identified by its name.
 * This class ensures that committee names conform to the defined constraints and
 * supports equality checks and proper hashing for use in collections.
 *
 */
public class Committee {
    public static final String MESSAGE_CONSTRAINTS = "Committee should not start with a blank";

    public final String committeeName;

    public Committee(String committee) {
        requireNonNull(committee);
        this.committeeName = committee;
    }

    /**
     * Checks if committee is valid, it cannot be empty
     *
     * @param committee User input for commmittee
     * @return boolean
     */
    public static boolean isValidCommittee(String committee) {
        if (committee == null || committee.isBlank()) {
            return false;
        }
        return true;
    }

    /**
     * Returns the committee name.
     */
    public String getCommitteeName() {
        return committeeName;
    }

    @Override
    public String toString() {
        return "Committee: " + this.committeeName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Committee)) {
            return false;
        }

        Committee otherCommittee = (Committee) other;
        return this.committeeName.equals(otherCommittee.committeeName);
    }

    @Override
    public int hashCode() {
        return this.committeeName.hashCode();
    }
}
