package loopin.projectbook.model.person;

/**
 * Represents the type of role (volunteer, team member or organisation member).
 */
public enum RoleType {
    TEAMMEMBER("Committee: ", "[^\\s].*"),
    VOLUNTEER("Volunteer", ""),
    ORGMEMBER("Organisation: ", "[^\\s].*");

    public final String PREFIX;
    public final String VALIDATION_REGEX;

    private RoleType(String prefix, String validationRegex) {
        PREFIX = prefix;
	VALIDATION_REGEX = validationRegex;
    }
}
