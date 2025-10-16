package loopin.projectbook.model.person;

import java.util.Objects;

/**
 * Represents an update remark for a Person in the project book.
 */
public class Remark {
    public enum Status { PENDING, COMPLETED }

    public final String content;
    public final Status status;

    /**
     * Creates a new UpdateRemark with a PENDING status.
     * @param content The update message.
     */
    public Remark(String content) {
        this.content = content.trim();
        this.status = Status.PENDING; // New remarks start as pending
    }

    /**
     * Returns a lower-cased version of the content for case-insensitive duplicate checking.
     */
    public String getNormalizedContent() {
        return content.toLowerCase();
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