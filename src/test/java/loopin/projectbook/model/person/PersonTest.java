package loopin.projectbook.model.person;

import static loopin.projectbook.logic.commands.CommandTestUtil.*;
import static loopin.projectbook.testutil.TypicalPersons.ALICE;
import static loopin.projectbook.testutil.TypicalPersons.BOB;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns false
        Person editedAlice = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withTelegram(VALID_TELEGRAM_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same email, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withTelegram(VALID_TELEGRAM_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same phone, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withTelegram(VALID_TELEGRAM_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same telegram, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different telegram -> returns false
        editedAlice = new PersonBuilder(ALICE).withTelegram(VALID_TELEGRAM_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

    }

    @Test
    public void toStringMethod() {
        String expected = Volunteer.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone().map(p -> p.value).orElse("nil")
                + ", email=" + ALICE.getEmail()
                + ", telegram=" + ALICE.getTelegram().map(p -> p.value).orElse("nil")
                + ", remarks=" + ALICE.getRemarks() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
