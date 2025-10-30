package loopin.projectbook.testutil;

import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_ORGANISATION;

import java.util.Optional;

import loopin.projectbook.logic.commands.personcommands.EditCommand.EditPersonDescriptor;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.person.teammember.Committee;

/**
 * Utilities for generating CLI strings for Person-related commands in tests.
 * Updated to match EditPersonDescriptor's current API and AddCommand grammar.
 */
public class PersonUtil {

    /** Builds a full "add ..." command line for {@code person}. */
    public static String getAddCommand(Person person) {
        return "add " + getPersonDetails(person);
    }

    /**
     * Returns CLI args for the given person:
     * n/ p?/ e/ tg?/ (c/ or o/ depending on role)
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();

        sb.append(PREFIX_NAME).append(person.getName().fullName).append(" ");

        person.getPhone().ifPresent(ph ->
                sb.append(PREFIX_PHONE).append(ph.value).append(" "));

        sb.append(PREFIX_EMAIL).append(person.getEmail().value).append(" ");

        person.getTelegram().ifPresent(tg ->
                sb.append(PREFIX_TELEGRAM).append(tg.value).append(" "));

        // role-specific:
        if (person instanceof TeamMember tm) {
            Committee c = tm.getCommittee();
            // Committee might not expose .value; toString() should print the user value.
            sb.append(PREFIX_COMMITEE).append(c.toString()).append(" ");
        } else if (person instanceof OrgMember om) {
            Organisation o = om.getOrganisation();
            sb.append(PREFIX_ORGANISATION).append(o.toString()).append(" ");
        }

        return sb.toString().trim();
    }

    /**
     * Returns an edit command string for {@code descriptor}.
     * Only includes fields present in the descriptor. For phone/telegram,
     * presence includes explicit removal (empty Optional) — in that case,
     * we omit emitting those fields (adjust if your grammar supports explicit removal flags).
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();

        descriptor.getName().ifPresent(name ->
                sb.append(PREFIX_NAME).append(name.fullName).append(" "));

        if (descriptor.hasPhoneEdit()) {
            descriptor.editedPhone().ifPresent(phone ->
                    sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        }

        descriptor.getEmail().ifPresent(email ->
                sb.append(PREFIX_EMAIL).append(email.value).append(" "));

        if (descriptor.hasTelegramEdit()) {
            descriptor.editedTelegram().ifPresent(tg ->
                    sb.append(PREFIX_TELEGRAM).append(tg.value).append(" "));
        }

        descriptor.getCommittee().ifPresent(c ->
                sb.append(PREFIX_COMMITEE).append(c.toString()).append(" "));

        descriptor.getOrganisation().ifPresent(o ->
                sb.append(PREFIX_ORGANISATION).append(o.toString()).append(" "));

        return sb.toString().trim();
    }
}
