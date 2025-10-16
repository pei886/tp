package loopin.projectbook.model.project.exceptions;

/**
 * Signals that the operation is unable to find the specified Project.
 */
public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super("Target project not found in the list");
    }
}
