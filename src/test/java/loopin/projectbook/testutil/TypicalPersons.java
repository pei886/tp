package loopin.projectbook.testutil;

import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TELEGRAM_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.person.Person;

/**
 * Typical persons to be used for testing.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withTelegram("alice_pauline").withEmail("alice@example.com")
            .withPhone("94351253").withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withTelegram("bensonmeier").withEmail("johnd@example.com")
            .withPhone("98765432").withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withPhone("95352563").withEmail("heinz@example.com")
            .withTelegram("carl_kurz").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withPhone("87652533").withEmail("cornelia@example.com")
            .withTelegram("danielmeier").withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
            .withPhone("9482224").withEmail("werner@example.com")
            .withTelegram("ellemeyer").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
            .withPhone("9482427").withEmail("lydia@example.com")
            .withTelegram("fionakunz").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best")
            .withPhone("9482442").withEmail("anna@example.com")
            .withTelegram("georgebest").build();

    public static final Person HOON = new PersonBuilder().withName("Hoon Meier")
            .withPhone("8482424").withEmail("stefan@example.com")
            .withTelegram("hoonmeier").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller")
            .withPhone("8482131").withEmail("hans@example.com")
            .withTelegram("idamueller").build();

    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
            .withTelegram(VALID_TELEGRAM_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
            .withTelegram(VALID_TELEGRAM_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier";

    private TypicalPersons() {}

    public static ProjectBook getTypicalProjectBook() {
        ProjectBook ab = new ProjectBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
