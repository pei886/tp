package loopin.projectbook.storage;

import static loopin.projectbook.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static loopin.projectbook.testutil.Assert.assertThrows;
import static loopin.projectbook.testutil.TypicalPersons.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.model.person.Address;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final JsonAdaptedRemark INVALID_REMARK_NULL_CONTENT = new JsonAdaptedRemark(null, "PENDING");
    private static final JsonAdaptedRemark INVALID_REMARK_BAD_STATUS = new JsonAdaptedRemark("Test", "BAD_STATUS");
    private static final List<JsonAdaptedRemark> INVALID_REMARKS_STATUS = List.of(INVALID_REMARK_BAD_STATUS);

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_ROLE = BENSON.getRole();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

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
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_ROLE, VALID_REMARKS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                null, VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_ROLE, VALID_REMARKS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, null, VALID_EMAIL, VALID_TAGS, VALID_ROLE, VALID_REMARKS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, null, VALID_TAGS, VALID_ROLE, VALID_REMARKS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, invalidTags, VALID_ROLE, VALID_REMARKS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    // --- NEW REMARK TESTS ---

    @Test
    public void toModelType_validRemarks_returnsPersonWithRemarks() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_ROLE, VALID_REMARKS);

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
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_ROLE, INVALID_REMARKS_STATUS);

        // The exception is thrown by JsonAdaptedRemark.toModelType() within JsonAdaptedPerson.toModelType()
        String expectedMessage = "Remark status is invalid: BAD_STATUS";
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRemarkContent_throwsIllegalValueException() {
        List<JsonAdaptedRemark> invalidRemarks = List.of(INVALID_REMARK_NULL_CONTENT);
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_ROLE, invalidRemarks);

        // The exception is thrown by JsonAdaptedRemark.toModelType() when content is null
        String expectedMessage = String.format(JsonAdaptedRemark.MISSING_FIELD_MESSAGE_FORMAT, "content");
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }
}
