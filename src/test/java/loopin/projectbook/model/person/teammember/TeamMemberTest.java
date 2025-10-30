package loopin.projectbook.model.person.teammember;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.project.Project;

public class TeamMemberTest {

    private final Name name = new Name("John Doe");
    private final Committee committee = new Committee("Operations");
    private final Optional<Phone> phone = Optional.of(new Phone("98765432"));
    private final Email email = new Email("john@example.com");
    private final Optional<Telegram> telegram = Optional.of(new Telegram("john_d"));
    private final List<Project> projects = new ArrayList<>();
    private final HashSet<Remark> remarks = new HashSet<>();

    @Test
    public void constructor_validInputs_success() {
        TeamMember member = new TeamMember(name, committee, phone, email, telegram, remarks, projects);

        assertEquals(name, member.getName());
        assertEquals(committee, member.getCommittee());
        assertEquals(email, member.getEmail());
        assertEquals(phone, member.getPhone());
        assertEquals(telegram, member.getTelegram());
        assertNotNull(member.getRole());
        assertEquals("Operations", member.getCommittee().getCommitteeName());
    }

    @Test
    public void constructor_nullCommittee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new TeamMember(name, null, phone, email, telegram, remarks, projects));
    }

    @Test
    public void getCommittee_returnsCorrectCommittee() {
        TeamMember member = new TeamMember(name, committee, phone, email, telegram, remarks, projects);
        assertEquals(committee, member.getCommittee());
    }

    @Test
    public void createCopy_returnsNewTeamMemberWithSameCommittee() {
        TeamMember original = new TeamMember(name, committee, phone, email, telegram, remarks, projects);
        Name newName = new Name("Jane Doe");

        Person copy = original.createCopy(newName, phone, email, telegram, remarks, projects);

        assertTrue(copy instanceof TeamMember);
        TeamMember copiedMember = (TeamMember) copy;

        assertEquals(newName, copiedMember.getName());
        assertEquals(original.getCommittee(), copiedMember.getCommittee());
        assertEquals(original.getEmail(), copiedMember.getEmail());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        TeamMember member1 = new TeamMember(name, committee, phone, email, telegram, remarks, projects);
        TeamMember member2 = new TeamMember(new Name("John Doe"), new Committee("Operations"),
                Optional.of(new Phone("98765432")), new Email("john@example.com"),
                Optional.of(new Telegram("john_d")), new HashSet<>(), new ArrayList<>());

        assertTrue(member1.equals(member2));
    }

    @Test
    public void equals_differentCommittees_returnsFalse() {
        TeamMember member1 = new TeamMember(name, new Committee("Ops"), phone, email, telegram, remarks, projects);
        TeamMember member2 = new TeamMember(name, new Committee("Finance"), phone, email, telegram, remarks, projects);

        assertFalse(member1.equals(member2));
    }
}
