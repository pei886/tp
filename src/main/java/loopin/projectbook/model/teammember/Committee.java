package loopin.projectbook.model.teammember;

import loopin.projectbook.model.person.Address;

public class Committee {
    public static final String MESSAGE_CONSTRAINTS = "Committee should not start with a blank";

    public String committeeName;

    public Committee(String committee) {
        this.committeeName = committee;
    }

    /**
     * Checks if committee is valid, it cannot be empty
     * @param committee User input for commmittee
     * @return boolean
     */
    public static boolean isValidCommittee(String committee) {
        if (committee == null || committee.isBlank()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.committeeName;
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
