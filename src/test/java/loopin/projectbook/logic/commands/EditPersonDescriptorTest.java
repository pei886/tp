package loopin.projectbook.logic.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.EditCommand.EditPersonDescriptor;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.orgmember.Organisation;

/**
 * Tests for {@link EditPersonDescriptor} aligned to the new API
 * (hasPhoneEdit/editedPhone, hasTelegramEdit/editedTelegram, etc.).
 */
public class EditPersonDescriptorTest {

    @Test
    public void isAnyFieldEdited_false_whenEmpty() {
        EditPersonDescriptor d = new EditPersonDescriptor();
        assertFalse(d.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_true_whenAnySet() {
        EditPersonDescriptor d = new EditPersonDescriptor();
        d.setName(new Name("Alex"));
        assertTrue(d.isAnyFieldEdited());
    }

    @Test
    public void phone_edit_presentAndRemoval() {
        EditPersonDescriptor d = new EditPersonDescriptor();
        assertFalse(d.hasPhoneEdit());

        d.setPhone(Optional.of(new Phone("91234567")));
        assertTrue(d.hasPhoneEdit());
        assertEquals(Optional.of(new Phone("91234567")), d.editedPhone());

        d.setPhone(Optional.empty()); // explicit removal
        assertTrue(d.hasPhoneEdit());
        assertEquals(Optional.empty(), d.editedPhone());
    }

    @Test
    public void telegram_edit_presentAndRemoval() {
        EditPersonDescriptor d = new EditPersonDescriptor();
        assertFalse(d.hasTelegramEdit());

        d.setTelegram(Optional.of(new Telegram("alex_tg")));
        assertTrue(d.hasTelegramEdit());
        assertEquals(Optional.of(new Telegram("alex_tg")), d.editedTelegram());

        d.setTelegram(Optional.empty());
        assertTrue(d.hasTelegramEdit());
        assertEquals(Optional.empty(), d.editedTelegram());
    }

    @Test
    public void roleSpecific_fields_setters() {
        EditPersonDescriptor d = new EditPersonDescriptor();
        d.setCommittee(new Committee("Marketing"));
        d.setOrganisation(new Organisation("NUS"));
        assertTrue(d.isAnyFieldEdited());
        assertEquals(Optional.of(new Committee("Marketing")), d.getCommittee());
        assertEquals(Optional.of(new Organisation("NUS")), d.getOrganisation());
    }

    @Test
    public void equals_works() {
        EditPersonDescriptor a = new EditPersonDescriptor();
        a.setName(new Name("Alex"));
        a.setEmail(new Email("a@ex.com"));

        EditPersonDescriptor b = new EditPersonDescriptor(a);
        assertEquals(a, b);

        b.setEmail(new Email("b@ex.com"));
        assertNotEquals(a, b);
    }

    @Test
    public void toString_containsFields() {
        EditPersonDescriptor d = new EditPersonDescriptor();
        d.setName(new Name("Alex"));
        d.setPhone(Optional.of(new Phone("91234567")));
        d.setEmail(new Email("a@ex.com"));
        d.setTelegram(Optional.of(new Telegram("alex_tg")));
        String s = d.toString();
        assertTrue(s.contains("Alex"));
        assertTrue(s.contains("a@ex.com"));
    }
}
