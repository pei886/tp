package loopin.projectbook.model.person;

/**
 * Represents a Person's role in the project book.
 * Guarantees: immutable
 */
public class Role {

    public final RoleType roleType;
    public final String fullRole;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleType A valid RoleType.
     * @param roleName A valid role Name.
     */
    public Role(RoleType roleType, String roleName) {
        assert (roleType != null && roleName != null);
        this.roleType = roleType;
	assert (isValidRoleName(roleName));
	fullRole = roleType.getPrefix() + roleName;
    }

    /**
     * Returns true if a given string is a valid role name for the given role.
     */
    public boolean isValidRoleName(String test) {
        return test.matches(roleType.getValidationRegex());
    }

    public RoleType getRoleType() {
        return roleType;
    }

    @Override
    public String toString() {
        return fullRole;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return fullRole.equals(otherRole.fullRole);
    }

    @Override
    public int hashCode() {
        return fullRole.hashCode();
    }

}
