package loopin.projectbook.model.teammember;

public class Committee {
    public String committeeName;

    public Committee(String committee) {
        this.committeeName = committee;
    }

    @Override
    public String toString() {
        return this.committeeName;
    }
}
