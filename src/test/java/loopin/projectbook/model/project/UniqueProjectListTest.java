package loopin.projectbook.model.project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import loopin.projectbook.model.project.exceptions.DuplicateProjectException;
import loopin.projectbook.model.project.exceptions.ProjectNotFoundException;

/**
 * Tests for {@link UniqueProjectList}.
 */
public class UniqueProjectListTest {

    /** real factory: Project(ProjectName, Description) */
    private static Project proj(String name) {
        return new Project(new ProjectName(name), new Description("desc"));
    }

    // contains(Project) true when equal project exists
    @Test
    public void contains_existing_returnsTrue() {
        UniqueProjectList list = new UniqueProjectList();
        Project p = proj("Alpha");
        list.add(p);
        assertTrue(list.contains(p));
    }

    // contains(Project) false when not present
    @Test
    public void contains_missing_returnsFalse() {
        UniqueProjectList list = new UniqueProjectList();
        list.add(proj("Alpha"));
        assertFalse(list.contains(proj("Beta")));
    }

    // findByName normalizes whitespace + case
    @Test
    public void findByName_normalization_works() {
        UniqueProjectList list = new UniqueProjectList();
        Project p = proj("Website   Revamp");
        list.add(p);

        Optional<Project> found = list.findByName("  Website Revamp  ");
        assertTrue(found.isPresent());
        assertEquals(p, found.get());
    }

    // setProject replaces existing equal project
    @Test
    public void setProject_existing_replaced() {
        UniqueProjectList list = new UniqueProjectList();
        Project p1 = proj("Alpha");
        list.add(p1);

        Project updated = proj("Alpha"); // equals() must consider same identity
        list.setProject(updated);

        assertTrue(list.contains(updated));
    }

    // setProject throws when target not found
    @Test
    public void setProject_missing_throws() {
        UniqueProjectList list = new UniqueProjectList();
        assertThrows(ProjectNotFoundException.class, () -> list.setProject(proj("Unknown")));
    }

    // setProjects rejects duplicates
    @Test
    public void setProjects_duplicates_throws() {
        UniqueProjectList list = new UniqueProjectList();
        Project a1 = proj("Alpha");
        Project a2 = proj("Alpha");
        List<Project> withDupes = java.util.Arrays.asList(a1, a2);
        assertThrows(DuplicateProjectException.class, () -> list.setProjects(withDupes));
    }

    // setProjects replaces contents
    @Test
    public void setProjects_replaces() {
        UniqueProjectList list = new UniqueProjectList();
        list.add(proj("Old"));

        List<Project> newOnes = java.util.Arrays.asList(proj("A"), proj("B"));
        list.setProjects(newOnes);

        assertTrue(list.contains(proj("A")));
        assertTrue(list.contains(proj("B")));
    }

    // remove(Project) removes when present
    @Test
    public void remove_existing_succeeds() {
        UniqueProjectList list = new UniqueProjectList();
        Project p = proj("Alpha");
        list.add(p);
        list.remove(p);
        assertFalse(list.contains(p));
    }

    // remove(Project) missing throws
    @Test
    public void remove_missing_throws() {
        UniqueProjectList list = new UniqueProjectList();
        assertThrows(ProjectNotFoundException.class, () -> list.remove(proj("Nope")));
    }

    // removeByName uses normalization
    @Test
    public void removeByName_normalizedName_succeeds() {
        UniqueProjectList list = new UniqueProjectList();
        list.add(proj("Alpha"));

        list.removeByName("   Alpha   ");
        assertFalse(list.contains(proj("Alpha")));
    }

    // equals/hashCode cover internal list equality
    @Test
    public void equals_and_hashCode() {
        UniqueProjectList a = new UniqueProjectList();
        UniqueProjectList b = new UniqueProjectList();
        a.setProjects(java.util.Arrays.asList(proj("A"), proj("B")));
        b.setProjects(java.util.Arrays.asList(proj("A"), proj("B")));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
