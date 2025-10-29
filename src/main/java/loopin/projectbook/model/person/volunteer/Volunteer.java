package loopin.projectbook.model.person.volunteer;

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
import loopin.projectbook.model.tag.Tag;

/**
 * Represents a Volunteeer in the project book.
 */
public class Volunteer extends Person {

    /**
     * Name, email and tags must be present and non null but phone can be null.
     */
    public Volunteer(Name name, Optional<Phone> phone, Email email, Optional<Telegram> telegram,
            Set<Tag> tags, Set<Remark> remarks, List<Project> projects) {
        super(name, new Role(RoleType.VOLUNTEER, ""), phone, email, telegram, tags, remarks, projects);
    }

    @Override
    public Person createCopy(Name name, Optional<Phone> phone, Email email, Optional<Telegram> telegram,
            Set<Tag> tags, Set<Remark> remarks, List<Project> projects) {
        return new Volunteer(name, phone, email, telegram, tags, remarks, projects);
    }
}
