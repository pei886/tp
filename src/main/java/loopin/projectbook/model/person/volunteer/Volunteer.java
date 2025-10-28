package loopin.projectbook.model.person.volunteer;

import java.util.Set;

import loopin.projectbook.model.person.*;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents a Volunteeer in the project book.
 */
public class Volunteer extends Person {

    /**
     * Name, email and tags must be present and non null but phone can be null.
     */
    public Volunteer(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        super(name, new Role(RoleType.VOLUNTEER, ""), phone, email, telegram, tags, remarks);
    }

    @Override
    public Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        return new Volunteer(name, phone, email, telegram, tags, remarks);
    }
}
