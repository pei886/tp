package loopin.projectbook.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.tag.Tag;

/**
 * Contains utility methods for populating {@code ProjectBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new TeamMember(new Name("Alex Yeoh"), new Committee("Operations"),
                    new Phone("87438807"), new Email("alexyeoh@example.com"), new Telegram("AlexYeoh123"),
                    getTagSet("Meeting shifted to Thursday"),
                    getRemarkSet("Check venue booking"),
                    new ArrayList<Project>()),

            new Volunteer(new Name("Bernice Yu"),
                    new Phone("99272758"), new Email("berniceyu@example.com"), new Telegram("BurnIce"),
                    getTagSet(),
                    getEmptyRemarkSet(),
                    new ArrayList<Project>()),

            new OrgMember(new Name("Charlotte Oliveiro"), new Organisation("GreenCorp"),
                    new Phone("93210283"), new Email("charlotte@example.com"), new Telegram("CharredOlive"),
                    getTagSet(),
                    getRemarkSet("Follow up on sponsorship"),
                    new ArrayList<Project>()),

            new TeamMember(new Name("David Li"), new Committee("Logistics"),
                    new Phone("91031282"), new Email("lidavid@example.com"), new Telegram("DavidLi918"),
                    getTagSet(),
                    getEmptyRemarkSet(),
                    new ArrayList<Project>()),

            new Volunteer(new Name("Irfan Ibrahim"),
                    new Phone("92492021"), new Email("irfan@example.com"), new Telegram("Irfan_Ibrahim"),
                    getTagSet("Posted to new booth"),
                    getRemarkSet("Needs T-shirt (Size L)"),
                    new ArrayList<Project>()),

            new OrgMember(new Name("Roy Balakrishnan"), new Organisation("PixelWorks"),
                    new Phone("92624417"), new Email("royb@example.com"), new Telegram("Roy_Balakrishnan172"),
                    getTagSet("Need mockup by Wednesday"),
                    getRemarkSet("Invoice pending", "Confirm logo dimensions"),
                    new ArrayList<Project>()),
        };
    }

    public static ReadOnlyProjectBook getSampleProjectBook() {
        ProjectBook sampleAb = new ProjectBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a remark set containing the list of strings given.
     * Remarks are initialized as PENDING by default (via the Remark constructor).
     */
    public static Set<Remark> getRemarkSet(String... strings) {
        return Arrays.stream(strings)
                .map(Remark::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an empty, unmodifiable Set of Remarks for persons with no sample data.
     */
    public static Set<Remark> getEmptyRemarkSet() {
        return Collections.emptySet();
    }

}
