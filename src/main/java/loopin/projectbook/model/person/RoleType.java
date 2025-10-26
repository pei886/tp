package loopin.projectbook.model.person;

/**
 * Represents the type of role (volunteer, team member or organisation member).
 */
public enum RoleType {
    TEAMMEMBER("Committee: ", "[^\\s].*"),
    VOLUNTEER("Volunteer", ""),
    ORGMEMBER("Organisation: ", "[^\\s].*");

    public final String prefix;
    public final String validationRegex;

    private RoleType(String prefix, String validationRegex) {
        this.prefix = prefix;
        this.validationRegex = validationRegex;
    }
}
