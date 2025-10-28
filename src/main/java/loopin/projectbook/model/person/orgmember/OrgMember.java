package loopin.projectbook.model.person.orgmember;

import java.util.Set;

import loopin.projectbook.model.person.*;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents an Organisation Member in the project book.
 */
public class OrgMember extends Person {

    private final Organisation organisation;

    /**
     * Name, email and tags must be present and non null but phone can be null.
     */
    public OrgMember(Name name, Organisation organisation, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        super(name, new Role(RoleType.ORGMEMBER, organisation.toString()), phone, email, telegram, tags, remarks);
        this.organisation = organisation;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        return new OrgMember(name, organisation, phone, email, telegram, tags, remarks);
    }
}
