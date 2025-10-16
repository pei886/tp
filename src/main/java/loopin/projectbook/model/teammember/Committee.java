package loopin.projectbook.model.teammember;

/**
 * Represents a committee for team members in the ProjectBook.
 */
public class Committee {
    private String committeeName;

    public Committee(String committee) {
        this.committeeName = committee;
    }

    @Override
    public String toString() {
        return this.committeeName;
    }
}
