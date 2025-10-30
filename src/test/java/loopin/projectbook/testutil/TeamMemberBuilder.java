package loopin.projectbook.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.tag.Tag;
import loopin.projectbook.model.util.SampleDataUtil;

/**
 * A utility class to help with building {@code TeamMember} objects for tests.
 */
public class TeamMemberBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_TELEGRAM = "amybee";
    public static final String DEFAULT_COMMITTEE = "Operations";

    private Name name;
    private Committee committee;
    private Optional<Phone> phone;
    private Email email;
    private Optional<Telegram> telegram;
    private Set<Tag> tags;
    private Set<Remark> remarks;
    private List<Project> projects;

    /**
     * Creates a {@code TeamMemberBuilder} with default details.
     */
    public TeamMemberBuilder() {
        name = new Name(DEFAULT_NAME);
        committee = new Committee(DEFAULT_COMMITTEE);
        phone = Optional.of(new Phone(DEFAULT_PHONE));
        email = new Email(DEFAULT_EMAIL);
        telegram = Optional.of(new Telegram(DEFAULT_TELEGRAM));
        tags = new HashSet<>();
        remarks = new HashSet<>();
        projects = new ArrayList<>();
    }

    /**
     * Initializes the {@code TeamMemberBuilder} with the data of {@code teamMemberToCopy}.
     */
    public TeamMemberBuilder(TeamMember teamMemberToCopy) {
        name = teamMemberToCopy.getName();
        committee = teamMemberToCopy.getCommittee();
        phone = teamMemberToCopy.getPhone();
        email = teamMemberToCopy.getEmail();
        telegram = teamMemberToCopy.getTelegram();
        tags = new HashSet<>(teamMemberToCopy.getTags());
        remarks = new HashSet<>(teamMemberToCopy.getRemarks());
        projects = new ArrayList<>(teamMemberToCopy.getProjects());
    }

    public TeamMemberBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    public TeamMemberBuilder withCommittee(String committeeName) {
        this.committee = new Committee(committeeName);
        return this;
    }

    public TeamMemberBuilder withPhone(String phone) {
        this.phone = Optional.of(new Phone(phone));
        return this;
    }

    public TeamMemberBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public TeamMemberBuilder withTelegram(String telegram) {
        this.telegram = Optional.of(new Telegram(telegram));
        return this;
    }

    public TeamMemberBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public TeamMemberBuilder withRemarks(String... remarks) {
        this.remarks = SampleDataUtil.getRemarkSet(remarks);
        return this;
    }

    public TeamMemberBuilder withProjects(Project... projects) {
        this.projects = List.of(projects);
        return this;
    }

    /**
     * Runs the builder and returns a team member
     */
    public TeamMember build() {
        return new TeamMember(name, committee, phone, email, telegram, tags, remarks, projects);
    }
}
