package loopin.projectbook.model.person.volunteer;

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
 * Represents a Volunteeer in the project book.
 */
public class Volunteer extends Person {

    /**
     * Name, email and tags must be present and non null but phone can be null.
     */
    public Volunteer(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
        super(name, new Role(RoleType.VOLUNTEER, ""), phone, email, telegram, tags);
    }

    @Override
    public Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
        return new Volunteer(name, phone, email, telegram, tags);
    }
}
