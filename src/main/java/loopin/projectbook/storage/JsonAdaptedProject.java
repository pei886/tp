package loopin.projectbook.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Description;
import loopin.projectbook.model.project.LastUpdate;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Jackson-friendly version of {@link Project}.
 */
class JsonAdaptedProject {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Project's %s field is missing!";

    private final String name;
    private final String description;
    private final List<String> members = new ArrayList<>();
    private final String createdAt;
    private final String lastUpdateMessage;
    private final String lastUpdateTimestamp;

    /**
     * Constructs a {@code JsonAdaptedProject} with the given project details.
     */
    @JsonCreator
    public JsonAdaptedProject(@JsonProperty("name") String name,
                              @JsonProperty("description") String description,
                              @JsonProperty("members") List<String> members,
                              @JsonProperty("createdAt") String createdAt,
                              @JsonProperty("lastUpdateMessage") String lastUpdateMessage,
                              @JsonProperty("lastUpdateTimestamp") String lastUpdateTimestamp) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.lastUpdateMessage = lastUpdateMessage;
        this.lastUpdateTimestamp = lastUpdateTimestamp;

        if (members != null) {
            this.members.addAll(members);
        }
    }

    /**
     * Converts a given {@code Project} into this class for Jackson use.
     */
    public JsonAdaptedProject(Project source) {
        this.name = source.getName().toString();
        this.description = source.getDescription().toString();
        this.createdAt = source.getCreatedAt().toString();

        LastUpdate lu = source.getLatestUpdate();
        if (lu != null && lu.hasUpdate()) {
            this.lastUpdateMessage = lu.getUpdateMessage();
            this.lastUpdateTimestamp = lu.getTimestamp().toString();
        } else {
            this.lastUpdateMessage = null;
            this.lastUpdateTimestamp = null;
        }

        for (Person p : source.getAllPeople()) {
            this.members.add(p.getEmail().value);
        }

    }

    /**
     * Converts this Jackson-friendly adapted project object into the model's {@code Project} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted project.
     */
    public Project toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ProjectName.class.getSimpleName()));
        }
        if (!ProjectName.isValidProjectName(name)) {
            throw new IllegalValueException(ProjectName.MESSAGE_CONSTRAINTS);
        }
        final ProjectName modelName = new ProjectName(name);

        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        // createdAt: prefer stored value; fallback for legacy data
        final LocalDateTime modelCreatedAt =
                (createdAt != null && !createdAt.isEmpty())
                        ? LocalDateTime.parse(createdAt)
                        : LocalDateTime.now();

        // lastUpdate: reconstruct if both fields exist; else default
        final LastUpdate modelLastUpdate =
                (lastUpdateMessage != null && lastUpdateTimestamp != null)
                        ? new LastUpdate(lastUpdateMessage, LocalDateTime.parse(lastUpdateTimestamp))
                        : new LastUpdate();

        return new Project(modelName, modelDescription, modelCreatedAt, modelLastUpdate);
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return members;
    }
}
