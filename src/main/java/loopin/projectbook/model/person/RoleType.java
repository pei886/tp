package loopin.projectbook.model.person;

/**
 * Represents the type of role (volunteer, team member or organisation member).
 */
public enum RoleType {
    TEAMMEMBER("Committee: ", "[^\\s].*"),
    VOLUNTEER("Volunteer", ""),
    ORGMEMBER("Organisation: ", "[^\\s].*");

    private final String prefix;
    private final String validationRegex;

    private RoleType(String prefix, String validationRegex) {
        this.prefix = prefix;
	this.validationRegex = validationRegex;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getValidationRegex() {
        return validationRegex;
    }
}
