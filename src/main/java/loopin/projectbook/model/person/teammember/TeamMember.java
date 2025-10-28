package loopin.projectbook.model.person.teammember;

import java.util.Set;

import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Role;
import loopin.projectbook.model.person.RoleType;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents a team member in the ProjectBook.
 */
public class TeamMember extends Person {

    private final Committee committee;

    /**
     * Name, phome, email and committee must be present and non null.
     */
    public TeamMember(Name name, Committee committee, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
        super(name, new Role(RoleType.TEAMMEMBER, committee.toString()), phone, email, telegram, tags);
        this.committee = committee;
    }

    public Committee getCommittee() {
        return committee;
    }

    @Override
    public Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
        return new TeamMember(name, committee, phone, email, telegram, tags);
    }
}
