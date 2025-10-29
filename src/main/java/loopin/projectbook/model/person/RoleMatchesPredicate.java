package loopin.projectbook.model.person;

import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Role} matches the given role shortcut (t/v/o).
 */
public class RoleMatchesPredicate implements Predicate<Person> {

    private final char roleShortcut;

    public RoleMatchesPredicate(char roleShortcut) {
        this.roleShortcut = roleShortcut;
    }

    @Override
    public boolean test(Person person) {
        RoleType type = person.getRole().getRoleType();

        switch (roleShortcut) {
        case 't':
            return type == RoleType.TEAMMEMBER;
        case 'v':
            return type == RoleType.VOLUNTEER;
        case 'o':
            return type == RoleType.ORGMEMBER;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RoleMatchesPredicate
                && roleShortcut == ((RoleMatchesPredicate) other).roleShortcut);
    }
}
