package loopin.projectbook.model.person.teammember;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Role;
import loopin.projectbook.model.person.RoleType;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.project.Project;

/**
 * Represents a team member in the ProjectBook.
 */
public class TeamMember extends Person {

    private final Committee committee;

    /**
     * Name, phome, email and committee must be present and non null.
     */
    public TeamMember(Name name, Committee committee, Optional<Phone> phone, Email email, Optional<Telegram> telegram,
            Set<Remark> remarks, List<Project> projects) {
        super(name, new Role(RoleType.TEAMMEMBER, committee.getCommitteeName()), phone, email, telegram,
                remarks, projects);
        requireNonNull(committee);
        this.committee = committee;
    }

    public Committee getCommittee() {
        return committee;
    }

    @Override
    public Person createCopy(Name name, Optional<Phone> phone, Email email, Optional<Telegram> telegram,
            Set<Remark> remarks, List<Project> projects) {
        return new TeamMember(name, committee, phone, email, telegram, remarks, projects);
    }
}
