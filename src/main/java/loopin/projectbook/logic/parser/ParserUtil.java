package loopin.projectbook.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.commons.util.StringUtil;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.project.Description;
import loopin.projectbook.model.project.ProjectName;
import loopin.projectbook.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }
    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String telegram} into a {@code Telegram}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code telegram} is invalid.
     */
    public static Telegram parseTelegram(String telegram) throws ParseException {
        requireNonNull(telegram);
        String trimmedTelegram = telegram.trim();
        if (!Telegram.isValidTelegram(trimmedTelegram)) {
            throw new ParseException(Telegram.MESSAGE_CONSTRAINTS);
        }
        return new Telegram(trimmedTelegram);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses the given {@code String} into a {@code Committee} object.
     * The input string is trimmed of leading and trailing whitespaces and validates
     * against {@link Committee#isValidCommittee(String)}. If the input does not satisfy
     * the committee constraints, a {@code ParseException} is thrown.
     *
     * @param committee the string representing a committee
     * @return a {@code Committee} object with the given valid committee name}
     * @throws ParseException if the given string is invalid according to {@link Committee#MESSAGE_CONSTRAINTS}
     */
    public static Committee parseCommittee(String committee) throws ParseException {
        requireNonNull(committee);
        String trimmedCommittee = committee.trim();
        if (!Committee.isValidCommittee(trimmedCommittee)) {
            throw new ParseException(Committee.MESSAGE_CONSTRAINTS);
        }
        return new Committee(trimmedCommittee);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }


    /**
     * Parses the given {@code String} into a {@code ProjectName} object.
     * The input string is trimmed of leading and trailing whitespaces.
     * If the resulting string is empty, a {@code ParseException} is thrown.
     *
     * @param name the string representing a project name
     * @return a {@code ProjectName} object with the given valid name
     * @throws ParseException if the given string is empty or invalid
     */
    public static ProjectName parseProjectName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            throw new ParseException(ProjectName.MESSAGE_CONSTRAINTS);
        }
        return new ProjectName(trimmedName);
    }

    /**
     * Parses {@code String organisation} into a {@code Organisation}.
     */
    public static Organisation parseOrganisation(String organisation) throws ParseException {
        requireNonNull(organisation);
        String trimmedOrg = organisation.trim();
        if (trimmedOrg.isEmpty()) {
            throw new ParseException(Organisation.MESSAGE_CONSTRAINTS);
        }
        return new Organisation(trimmedOrg);
    }

    /**
     * Parses a {@code String description} into a {@code Description}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static Description parseDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!Description.isValidDescription(trimmedDescription)) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }
        return new Description(trimmedDescription);
    }
}
