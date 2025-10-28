package loopin.projectbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import loopin.projectbook.model.person.Remark;

public class RemarkTest {

    @Test
    public void constructor_validContent_pendingStatus() {
        Remark remark = new Remark("Call back soon.");
        assertEquals("Call back soon.", remark.content);
        assertEquals(Remark.Status.PENDING, remark.status);
    }

    @Test
    public void constructor_trimsWhitespace() {
        Remark remark = new Remark("  content with spaces  ");
        assertEquals("content with spaces", remark.content);
    }

    @Test
    public void resolve_changesStatusToCompleted() {
        Remark pending = new Remark("Task to complete");
        Remark resolved = pending.resolve();

        // Check that the original object is immutable
        assertEquals(Remark.Status.PENDING, pending.status);

        // Check that the new object has the correct status and content
        assertEquals(Remark.Status.COMPLETED, resolved.status);
        assertEquals(pending.content, resolved.content);
    }

    @Test
    public void equals_sameContent_returnsTrue() {
        Remark remark1 = new Remark("Follow up on email");
        Remark remark2 = new Remark("Follow up on email");
        Remark remark3 = new Remark("follow up on email"); // Case-insensitive check
        Remark resolvedRemark = remark1.resolve(); // Status should be ignored

        // Same object
        assertTrue(remark1.equals(remark1));

        // Same content
        assertTrue(remark1.equals(remark2));

        // Same content, different case (due to getNormalizedContent())
        assertTrue(remark1.equals(remark3));

        // Same content, different status
        assertTrue(remark1.equals(resolvedRemark));
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        Remark remark1 = new Remark("Follow up on email");
        Remark remark2 = new Remark("Follow up on meeting");

        // Different content
        assertFalse(remark1.equals(remark2));

        // Null or different object type
        assertFalse(remark1.equals(null));
        assertFalse(remark1.equals(new Object()));
    }
}
