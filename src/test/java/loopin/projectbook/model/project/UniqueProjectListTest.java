package loopin.projectbook.model.project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import loopin.projectbook.model.project.exceptions.DuplicateProjectException;
import loopin.projectbook.model.project.exceptions.ProjectNotFoundException;

/**
 * Tests for {@link UniqueProjectList}.
 */
public class UniqueProjectListTest {

    // ----------------------------------------------------------------------
    // helper minimal Project for tests
    // ----------------------------------------------------------------------
    private static Project project(String name) {
        return new Project(new ProjectName(name));
    }

    @Test
    public void contains_null_throwsNullPointerException() {
        // tests: contains(null) should NPE
        UniqueProjectList list = new UniqueProjectList();
        assertThrows(NullPointerException.class, () -> list.contains(null));
    }

    @Test
    public void contains_existingProject_returnsTrue() {
        // tests: contains() returns true when exact project already in list
        UniqueProjectList list = new UniqueProjectList();
        Project p = project("Alpha");
        list.add(p);
        assertTrue(list.contains(p));
    }

    @Test
    public void contains_nonExistingProject_returnsFalse() {
        // tests: contains() returns false when project not in list
        UniqueProjectList list = new UniqueProjectList();
        list.add(project("Alpha"));
        assertFalse(list.contains(project("Beta")));
    }

    @Test
    public void findByName_exactMatch_returnsProject() {
        // tests: findByName returns project when exact name used
        UniqueProjectList list = new UniqueProjectList();
        Project p = project("Website Revamp");
        list.add(p);

        Optional<Project> found = list.findByName("Website Revamp");
        assertTrue(found.isPresent());
        assertEquals(p, found.get());
    }

    @Test
    public void findByName_whitespaceAndCaseInsensitivity_succeeds() {
        // tests: findByName normalizes whitespace and case
        UniqueProjectList list = new UniqueProjectList();
        Project p = project("  Website   Revamp ");
        list.add(p);

        assertTrue(list.findByName("website revamp").isPresent());
        assertTrue(list.findByName("   WEBSITE   REVAMP   ").isPresent());
    }

    @Test
    public void findByName_null_returnsEmptyOptional() {
        // tests: findByName(null) returns Optional.empty()
        UniqueProjectList list = new UniqueProjectList();
        list.add(project("Alpha"));
        assertEquals(Optional.empty(), list.findByName(null));
    }

    @Test
    public void add_duplicate_throwsDuplicateProjectException() {
        // tests: add() should reject duplicates by equals()
        UniqueProjectList list = new UniqueProjectList();
        Project p = project("Alpha");
        list.add(p);
        assertThrows(DuplicateProjectException.class, () -> list.add(project("Alpha")));
    }

    @Test
    public void setProject_existingProject_replaces() {
        // tests: setProject() replaces project at same index
        UniqueProjectList list = new UniqueProjectList();
        Project p1 = project("Alpha");
        list.add(p1);

        Project updated = project("Alpha"); // same identity via equals
        list.setProject(updated);

        assertTrue(list.contains(updated));
    }

    @Test
    public void setProject_nonExisting_throwsProjectNotFound() {
        // tests: setProject() on non-existing throws
        UniqueProjectList list = new UniqueProjectList();
        assertThrows(ProjectNotFoundException.class, () -> list.setProject(project("Unknown")));
    }

    @Test
    public void setProjects_withDuplicates_throwsDuplicateProjectException() {
        // tests: setProjects() validates uniqueness
        UniqueProjectList list = new UniqueProjectList();
        List<Project> withDupes = Arrays.asList(project("Alpha"), project("Alpha"));
        assertThrows(DuplicateProjectException.class, () -> list.setProjects(withDupes));
    }

    @Test
    public void setProjects_valid_replacesAll() {
        // tests: setProjects() replaces
        UniqueProjectList list = new UniqueProjectList();
        list.add(project("Old"));

        List<Project> newOnes = Arrays.asList(project("A"), project("B"));
        list.setProjects(newOnes);

        assertTrue(list.contains(project("A")));
        assertTrue(list.contains(project("B")));
        assertEquals(2, list.asUnmodifiableObservableList().size());
    }

    @Test
    public void remove_existing_succeeds() {
        // tests: remove(Project) removes exactly that project
        UniqueProjectList list = new UniqueProjectList();
        Project p = project("Alpha");
        list.add(p);

        list.remove(p);
        assertFalse(list.contains(p));
    }

    @Test
    public void remove_nonExisting_throwsProjectNotFound() {
        // tests: remove(Project) throws when project not present
        UniqueProjectList list = new UniqueProjectList();
        assertThrows(ProjectNotFoundException.class, () -> list.remove(project("Nope")));
    }

    @Test
    public void removeByName_existing_succeeds() {
        // tests: removeByName() removes project by normalized name
        UniqueProjectList list = new UniqueProjectList();
        Project p = project("Website Revamp");
        list.add(p);

        list.removeByName("  website  revamp ");
        assertFalse(list.contains(p));
    }

    @Test
    public void removeByName_notFound_throwsProjectNotFound() {
        // tests: removeByName() throws when no match
        UniqueProjectList list = new UniqueProjectList();
        list.add(project("Alpha"));
        assertThrows(ProjectNotFoundException.class, () -> list.removeByName("Beta"));
    }

    @Test
    public void asUnmodifiableObservableList_isUnmodifiable() {
        // tests: returned list is unmodifiable
        UniqueProjectList list = new UniqueProjectList();
        list.add(project("Alpha"));
        assertThrows(UnsupportedOperationException.class, () ->
                list.asUnmodifiableObservableList().remove(0)
        );
    }

    // ----------------------------------------------------------------------
    // minimal stand-in for Project + ProjectName to make tests compile
    // (adjust to your real Project class)
    // ----------------------------------------------------------------------
    private static final class Project {
        private final ProjectName name;

        Project(ProjectName name) {
            this.name = name;
        }

        public ProjectName getName() {
            return name;
        }

        @Override
        public boolean equals(Object other) {
            return other == this
                    || (other instanceof Project && name.equals(((Project) other).name));
        }

        @Override
        public String toString() {
            return name.toString();
        }
    }

    private static final class ProjectName {
        private final String value;

        ProjectName(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object other) {
            return other == this
                    || (other instanceof ProjectName && value.equals(((ProjectName) other).value));
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
