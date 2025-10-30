package loopin.projectbook.model.person.teammember;

import static loopin.projectbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CommitteeTest {

    @Test
    public void constructor_nullCommittee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Committee(null));
    }

    @Test
    public void constructor_validCommittee_success() {
        Committee committee = new Committee("Operations");
        assertEquals("Operations", committee.getCommitteeName());
    }

    @Test
    public void isValidCommittee() {
        // null committee
        assertFalse(Committee.isValidCommittee(null));

        // invalid committees
        assertFalse(Committee.isValidCommittee(""));     // empty string
        assertFalse(Committee.isValidCommittee("   "));  // spaces only

        // valid committees
        assertTrue(Committee.isValidCommittee("Finance"));
        assertTrue(Committee.isValidCommittee("Operations"));
        assertTrue(Committee.isValidCommittee("Tech Team"));
        assertTrue(Committee.isValidCommittee("C1")); // alphanumeric allowed
    }

    @Test
    public void equals() {
        Committee finance = new Committee("Finance");
        Committee financeCopy = new Committee("Finance");
        Committee ops = new Committee("Operations");

        // same object -> returns true
        assertTrue(finance.equals(finance));

        // same values -> returns true
        assertTrue(finance.equals(financeCopy));

        // null -> returns false
        assertFalse(finance.equals(null));

        // different type -> returns false
        assertFalse(finance.equals(5));

        // different values -> returns false
        assertFalse(finance.equals(ops));
    }

    @Test
    public void hashCode_sameCommittee_equalHashCodes() {
        Committee c1 = new Committee("Operations");
        Committee c2 = new Committee("Operations");
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void toString_correctFormat() {
        Committee committee = new Committee("Operations");
        assertEquals("Committee: Operations", committee.toString());
    }
}
