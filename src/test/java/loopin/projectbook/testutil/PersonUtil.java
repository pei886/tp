package loopin.projectbook.testutil;

import static loopin.projectbook.logic.parser.CliSyntax.*;

import java.util.Set;

import loopin.projectbook.logic.commands.EditCommand.EditPersonDescriptor;
import loopin.projectbook.logic.commands.personcommands.AddOrgMemberCommand;
import loopin.projectbook.logic.commands.personcommands.AddTeamMemberCommand;
import loopin.projectbook.logic.commands.personcommands.AddVolunteerCommand;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.person.volunteer.Volunteer;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        if (person instanceof Volunteer) {
            return AddVolunteerCommand.COMMAND_WORD + " " + getPersonDetails(person);
        } else if (person instanceof TeamMember) {
            return AddTeamMemberCommand.COMMAND_WORD + " " + getPersonDetails(person);
        } else if (person instanceof OrgMember) {
            return AddOrgMemberCommand.COMMAND_WORD + " " + getPersonDetails(person);
        } else {
            throw new IllegalArgumentException("Unknown person type: " + person.getClass());
        }
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().map(p -> p.value).orElse("nil") + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_TELEGRAM + person.getTelegram().map(t -> t.value).orElse("nil") + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE)
                .append(phone.map(p -> p.value).orElse("nil")).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getTelegram().ifPresent(telegram -> sb.append(PREFIX_TELEGRAM)
                .append(telegram.map(t -> t.value).orElse("nil")).append(" "));

        return sb.toString();
    }
}
