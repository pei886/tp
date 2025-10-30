package loopin.projectbook.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Remark.Status;

/**
 * Jackson-friendly version of {@link Remark}.
 */
class JsonAdaptedRemark {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Remark's %s field is missing!";

    private final String content;
    private final String status; // Stored as a string (e.g., "PENDING", "COMPLETED")

    /**
     * Constructs a {@code JsonAdaptedRemark} with the given remark details.
     */
    @JsonCreator
    public JsonAdaptedRemark(@JsonProperty("content") String content, @JsonProperty("status") String status) {
        this.content = content;
        this.status = status;
    }

    /**
     * Converts a given {@code Remark} into this class for Jackson use.
     */
    public JsonAdaptedRemark(Remark source) {
        content = source.content;
        status = source.status.name();
    }

    /**
     * Converts this Jackson-friendly adapted remark object into the model's {@code Remark} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Remark toModelType() throws IllegalValueException {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "content"));
        }

        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "status"));
        }

        try {
            Status modelStatus = Status.valueOf(status);
            return new Remark(content, modelStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException("Remark status is invalid: " + status);
        }
    }
}
