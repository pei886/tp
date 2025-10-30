package loopin.projectbook.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_TELEGRAM = "amybee";

    private Name name;
    private Optional<Phone> phone;
    private Email email;
    private Optional<Telegram> telegram;
    private Set<Remark> remarks;
    private List<Project> projects;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = Optional.of(new Phone(DEFAULT_PHONE));
        email = new Email(DEFAULT_EMAIL);
        telegram = Optional.of(new Telegram(DEFAULT_TELEGRAM));
        remarks = new HashSet<>();
        projects = new ArrayList();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        telegram = personToCopy.getTelegram();
        remarks = new HashSet<>(personToCopy.getRemarks());
        projects = new ArrayList(personToCopy.getProjects());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = Optional.of(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Telegram} of the {@code Person} that we are building.
     */
    public PersonBuilder withTelegram(String telegram) {
        this.telegram = Optional.of(new Telegram(telegram));
        return this;
    }

    /**
     * Parses the {@code remarks} into a {@code Set<Remark>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withRemarks(String ... remarks) {
        this.remarks = SampleDataUtil.getRemarkSet(remarks);
        return this;
    }

    /**
     * Returns a concrete {@code Person} object (Volunteer by default).
     */
    public Person build() {
        return new Volunteer(name, phone, email, telegram, remarks, projects);
    }

    /**
     * Builds and returns a {@code TeamMember} with the given committee name.
     */
    public TeamMember buildTeamMember(String committeeName) {
        Committee committee = new Committee(committeeName);
        return new TeamMember(name, committee, phone, email, telegram, remarks, projects);
    }

    /**
     * Builds and returns a {@code OrgMember} with the given organisation name.
     */
    public OrgMember buildOrgMember(String organisationName) {
        Organisation organisation = new Organisation(organisationName);
        return new OrgMember(name, organisation, phone, email, telegram, remarks, projects);
    }

    public Volunteer buildVolunteer() {
        return new Volunteer(name, phone, email, telegram, remarks, projects);
    }
}
