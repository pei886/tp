package loopin.projectbook.model.project.exceptions;

/**
 * Signals that the operation would result in duplicate Projects.
 * Projects are considered duplicates if they have the same identity.
 */
public class DuplicateProjectException extends RuntimeException {
    public DuplicateProjectException() {
        super("Operation would result in duplicate projects");
    }
}
