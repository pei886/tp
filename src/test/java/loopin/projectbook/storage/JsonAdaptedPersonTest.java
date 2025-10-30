package loopin.projectbook.storage;

import static loopin.projectbook.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static loopin.projectbook.testutil.Assert.assertThrows;
import static loopin.projectbook.testutil.TypicalPersons.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TELEGRAM = "*()!HI";
    private static final String INVALID_ROLE = "test123";
    private static final String INVALID_PROJECTS = "ABCP ROJECT";
    private static final JsonAdaptedRemark INVALID_REMARK_NULL_CONTENT = new JsonAdaptedRemark(null, "PENDING");
    private static final JsonAdaptedRemark INVALID_REMARK_BAD_STATUS = new JsonAdaptedRemark("Test", "BAD_STATUS");
    private static final List<JsonAdaptedRemark> INVALID_REMARKS_STATUS = List.of(INVALID_REMARK_BAD_STATUS);

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_TELEGRAM = BENSON.getTelegram().toString();
    private static final String VALID_ROLE = BENSON.getRole().toString();
    private static final List<JsonAdaptedProject> VALID_PROJECTS = BENSON.getProjects().stream()
            .map(JsonAdaptedProject::new)
            .toList();

    private static final JsonAdaptedRemark VALID_REMARK_A = new JsonAdaptedRemark("Needs follow up", "PENDING");
    private static final JsonAdaptedRemark VALID_REMARK_B = new JsonAdaptedRemark("Resolved today", "COMPLETED");
    private static final List<JsonAdaptedRemark> VALID_REMARKS = List.of(VALID_REMARK_A, VALID_REMARK_B);

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_ROLE, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(null, VALID_ROLE, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_ROLE, INVALID_PHONE, VALID_EMAIL, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_ROLE, null, VALID_EMAIL, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_ROLE, VALID_PHONE, INVALID_EMAIL, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_ROLE, VALID_PHONE, null, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTelegram_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_ROLE, VALID_PHONE, VALID_EMAIL, INVALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = Telegram.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullTelegram_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_ROLE, VALID_PHONE, VALID_EMAIL, null,
                VALID_REMARKS, VALID_PROJECTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Telegram.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    // --- NEW REMARK TESTS ---

    @Test
    public void toModelType_validRemarks_returnsPersonWithRemarks() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_ROLE, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM,
                VALID_REMARKS, VALID_PROJECTS);

        // Assert that the model person is created successfully and has the correct number of remarks.
        assertEquals(2, person.toModelType().getRemarks().size());

        // Assert that the remark content and status are correctly parsed (relies on Remark.equals)
        // Check for PENDING
        assertTrue(person.toModelType().getRemarks().stream()
                .anyMatch(r -> r.content.equals("Needs follow up") && r.status.equals(Remark.Status.PENDING)));
        // Check for COMPLETED
        assertTrue(person.toModelType().getRemarks().stream()
                .anyMatch(r -> r.content.equals("Resolved today") && r.status.equals(Remark.Status.COMPLETED)));
    }

    @Test
    public void toModelType_invalidRemarkStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_ROLE, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM,
                INVALID_REMARKS_STATUS, VALID_PROJECTS);

        // The exception is thrown by JsonAdaptedRemark.toModelType() within JsonAdaptedPerson.toModelType()
        String expectedMessage = "Remark status is invalid: BAD_STATUS";
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRemarkContent_throwsIllegalValueException() {
        List<JsonAdaptedRemark> invalidRemarks = List.of(INVALID_REMARK_NULL_CONTENT);
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_ROLE, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM,
                invalidRemarks, VALID_PROJECTS);

        // The exception is thrown by JsonAdaptedRemark.toModelType() when content is null
        String expectedMessage = String.format(JsonAdaptedRemark.MISSING_FIELD_MESSAGE_FORMAT, "content");
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }
}
