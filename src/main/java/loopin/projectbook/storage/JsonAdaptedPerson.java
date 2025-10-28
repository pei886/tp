package loopin.projectbook.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Role;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String telegram;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String role;
    private final List<JsonAdaptedRemark> remarks = new ArrayList<>();
    private final List<String> projectNames = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("telegram") String telegram,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags, @JsonProperty("role") String role,
                             @JsonProperty("remarks") List<JsonAdaptedRemark> remarks) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.role = (role == null || role.isBlank()) ? "Unknown" : role;
        if (remarks != null) {
            this.remarks.addAll(remarks);
        }
        if (projectNames != null) this.projectNames.addAll(projectNames);
        
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        role = source.getRole() != null ? source.getRole().fullRole : "Unknown";
        phone = source.getPhone() != null ? source.getPhone().value : null;
        email = source.getEmail().value;
        telegram = source.getTelegram() != null ? source.getTelegram().value : null;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        remarks.addAll(source.getRemarks().stream()
                .map(JsonAdaptedRemark::new)
        role = source.getRole();
        projectNames.addAll(source.getProjects().stream()
                .map(project -> project.getName().toString())
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final Phone modelPhone;
        if (phone == null) {
            modelPhone = null; // Person allows null phone
        } else if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        } else {
            modelPhone = new Phone(phone);
        }

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        final Telegram modelTelegram;
        if (telegram == null) {
            modelTelegram = null; // Person allows null telegram
        } else if (!Telegram.isValidTelegram(telegram)) {
            throw new IllegalValueException(Telegram.MESSAGE_CONSTRAINTS);
        } else {
            modelTelegram = new Telegram(telegram);
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);

        // Remarks
        final Set<Remark> modelRemarks = new HashSet<>();
        for (JsonAdaptedRemark remark : remarks) {
            modelRemarks.add(remark.toModelType());
        }

        // Role is guaranteed non-null by constructor (defaults to "Unknown")
        String[] modelRole = role.split(" ", 2);
        Person modelPerson = null;

        switch (modelRole[0]) {
        case "Unknown":
        case "Volunteer":
            modelPerson = new Volunteer(modelName, modelPhone, modelEmail, modelTelegram, modelTags, modelRemarks);
            break;
        case "Committee:":
            final Committee modelCommittee = new Committee(modelRole[1]);
            modelPerson = new TeamMember(modelName, modelCommittee, modelPhone, modelEmail, modelTelegram,
                    modelTags, modelRemarks);
            break;
        case "Organisation:":
            final Organisation modelOrganisation = new Organisation(modelRole[1]);
            modelPerson = new OrgMember(modelName, modelOrganisation, modelPhone, modelEmail, modelTelegram,
                    modelTags, modelRemarks);
            break;
        default:
            assert false;
            throw new IllegalValueException("Unknown role: " + role);
        }

        return modelPerson;
    }

}