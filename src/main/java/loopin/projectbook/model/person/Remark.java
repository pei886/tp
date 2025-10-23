package loopin.projectbook.model.person;

import java.util.Objects;

/**
 * Represents an update remark for a Person in the project book.
 */
public class Remark {
    /**
     * Represents the completion status of the update remark.
     */
    public enum Status { PENDING, COMPLETED }

    public final String content;
    public final Status status;

    /**
     * Creates a new UpdateRemark with a PENDING status.
     * @param content The update message.
     */
    public Remark(String content) {
        this(content, Status.PENDING); // New remarks start as pending
    }
    /**
     * Main constructor for a Remark, used for loading from storage.
     * @param content The remark content.
     * @param status The remark status.
     */
    public Remark(String content, Status status) { // <--- NEW CONSTRUCTOR
        this.content = content.trim();
        this.status = status;
    }

    /**
     * Returns a lower-cased version of the content for case-insensitive duplicate checking.
     */
    public String getNormalizedContent() {
        return content.toLowerCase();
    }

    /**
     * Returns a new Remark with the same content but COMPLETED status.
     * This is the suggested way to "resolve" a remark in the model.
     * @return A new COMPLETED Remark.
     */
    public Remark resolve() {
        return new Remark(this.content, Status.COMPLETED);
    }

    /**
     * Returns a new Remark with the same content but PENDING status.
     * @return A new PENDING Remark.
     */
    public Remark unresolve() { // Optional: to un-resolve a remark
        return new Remark(this.content, Status.PENDING);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Remark)) {
            return false;
        }

        Remark otherRemark = (Remark) other;
        // Remarks are considered a duplicate if their content is the same (case-insensitive).
        // The status (pending/completed) is ignored for the purpose of rejecting a duplicate add.
        return getNormalizedContent().equals(otherRemark.getNormalizedContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNormalizedContent());
    }

    @Override
    public String toString() {
        return content;
    }
}
