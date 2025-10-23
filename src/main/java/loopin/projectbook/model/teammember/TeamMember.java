package loopin.projectbook.model.teammember;

import java.util.HashSet;
import java.util.Set;

import loopin.projectbook.model.person.Address;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;

/**
 * Represents a team member in the ProjectBook.
 */
public class TeamMember extends Person {

    private final Committee committee;

    /**
     * Name, phome, email and committee must be present and non null.
     */
    public TeamMember(Name name, Phone phone, Email email, Committee committee, Set<Remark> remarks) {
        super(name, phone, email, new Address("NIL"), new HashSet<>(), new HashSet<>());
        this.committee = committee;
    }

    public Committee getCommittee() {
        return committee;
    }

    @Override
    public String getRole() {
        return this.committee.toString();
    }
}
