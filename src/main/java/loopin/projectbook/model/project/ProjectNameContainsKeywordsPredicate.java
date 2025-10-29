package loopin.projectbook.model.project;

import java.util.List;
import java.util.function.Predicate;

import loopin.projectbook.commons.util.StringUtil;
import loopin.projectbook.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Project}'s {@code ProjectName} matches any of the keywords given.
 */
public class ProjectNameContainsKeywordsPredicate implements Predicate<Project> {

    private final List<String> keywords;

    public ProjectNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Project project) {
        String projectNameLower = project.getName().toString().toLowerCase();
        return keywords.stream()
                .anyMatch(keyword -> projectNameLower.contains(keyword.toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ProjectNameContainsKeywordsPredicate
                && keywords.equals(((ProjectNameContainsKeywordsPredicate) other).keywords));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}