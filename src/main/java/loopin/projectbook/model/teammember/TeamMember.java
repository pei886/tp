package loopin.projectbook.model.teammember;

import loopin.projectbook.model.person.Address;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;

import java.util.HashSet;

public class TeamMember extends Person {

    private final Committee committee;

    public TeamMember(Name name, Phone phone, Email email, Committee committee) {
        super(name, phone, email, new Address("NIL"), new HashSet<>());
        this.committee = committee;
    }

    public Committee getCommittee() {
        return this.committee;
    }
}
