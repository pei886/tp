package loopin.projectbook.model.person.teammember;

import java.util.Set;

import loopin.projectbook.model.person.*;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents a team member in the ProjectBook.
 */
public class TeamMember extends Person {

    private final Committee committee;

    /**
     * Name, phome, email and committee must be present and non null.
     */
    public TeamMember(Name name, Committee committee, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        super(name, new Role(RoleType.TEAMMEMBER, committee.toString()), phone, email, telegram, tags, remarks);
        this.committee = committee;
    }

    public Committee getCommittee() {
        return committee;
    }

    @Override
    public Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        return new TeamMember(name, committee, phone, email, telegram, tags, remarks);
    }
}
