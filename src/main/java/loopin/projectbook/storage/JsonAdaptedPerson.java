package loopin.projectbook.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import loopin.projectbook.model.project.Project;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String role;
    private final String phone;
    private final String email;
    private final String telegram;
    private final List<JsonAdaptedProject> projects = new ArrayList<>(); // Renamed for clarity
    private final List<JsonAdaptedRemark> remarks = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("role") String role,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("telegram") String telegram,
                             @JsonProperty("remarks") List<JsonAdaptedRemark> remarks,
                             @JsonProperty("projects") List<JsonAdaptedProject> projects) {
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;

        if (projects != null) {
            this.projects.addAll(projects);
        }
        if (remarks != null) {
            this.remarks.addAll(remarks);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        role = source.getRole().fullRole;
        phone = source.getPhone().map(phone -> phone.value).orElse(null);
        email = source.getEmail().value;
        telegram = source.getTelegram().map(telegram -> telegram.value).orElse(null);
        projects.addAll(source.getProjects().stream()
                .map(JsonAdaptedProject::new)
                .collect(Collectors.toList()));
        remarks.addAll(source.getRemarks().stream()
                .map(JsonAdaptedRemark::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final Optional<Phone> modelPhone;
        if (phone == null) {
            modelPhone = Optional.empty(); // Person allows null phone
        } else if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        } else {
            modelPhone = Optional.of(new Phone(phone));
        }

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        final Optional<Telegram> modelTelegram;
        if (telegram == null) {
            modelTelegram = Optional.empty(); // Person allows null telegram
        } else if (!Telegram.isValidTelegram(telegram)) {
            throw new IllegalValueException(Telegram.MESSAGE_CONSTRAINTS);
        } else {
            modelTelegram = Optional.of(new Telegram(telegram));
        }

        // Remarks
        final Set<Remark> modelRemarks = new HashSet<>();
        for (JsonAdaptedRemark remark : remarks) {
            modelRemarks.add(remark.toModelType());
        }

        final List<Project> modelProjects = new ArrayList<>();

        if (role == null) {
            throw new IllegalValueException(Role.MESSAGE_CONSTRAINTS);
        }
        String[] modelRole = role.split(" ", 2);

        switch (modelRole[0]) {
        case "Volunteer":
            return new Volunteer(modelName, modelPhone, modelEmail, modelTelegram,
                    modelRemarks, modelProjects);
        case "Committee:":
            final Committee modelCommittee = new Committee(modelRole[1]);
            return new TeamMember(modelName, modelCommittee, modelPhone, modelEmail, modelTelegram,
                    modelRemarks, modelProjects);
        case "Organisation:":
            final Organisation modelOrganisation = new Organisation(modelRole[1]);
            return new OrgMember(modelName, modelOrganisation, modelPhone, modelEmail, modelTelegram,
                    modelRemarks, modelProjects);
        default:
            assert false;
            throw new IllegalValueException("Unknown role: " + role);
        }

    }

}
