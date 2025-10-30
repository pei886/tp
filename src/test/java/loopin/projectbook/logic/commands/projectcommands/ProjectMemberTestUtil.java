package loopin.projectbook.logic.commands.projectcommands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.ReadOnlyUserPrefs;
import loopin.projectbook.model.UserPrefs;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.model.project.Description;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

final class ProjectMemberTestUtil {

    private ProjectMemberTestUtil() {}

    static Project mkProject(String n) {
        return new Project(new ProjectName(n), new Description("desc"));
    }

    static Person mkVolunteer(String fullName) {
        return new Volunteer(
                new Name(fullName),
                Optional.<Phone>empty(),
                new Email(fullName.replace(" ", ".").toLowerCase() + "@example.com"),
                Optional.<Telegram>empty(),
                new HashSet<Remark>(),
                new ArrayList<Project>()
        );
    }

    static ObservableList<Person> peopleList(Person... ps) {
        ObservableList<Person> list = FXCollections.observableArrayList();
        if (ps != null) {
            list.addAll(ps);
        }
        return list;
    }

    static ObservableList<Project> projectList(Project... ps) {
        ObservableList<Project> list = FXCollections.observableArrayList();
        if (ps != null) {
            list.addAll(ps);
        }
        return list;
    }

    /**
     * Minimal read-only ProjectBook stub that just exposes the live observable lists
     * we pass into the ModelStub. This prevents NPEs when production code reads
     * model.getProjectBook().
     */
    private static final class ReadOnlyProjectBookStub implements ReadOnlyProjectBook {
        private final ObservableList<Person> persons;
        private final ObservableList<Project> projects;

        ReadOnlyProjectBookStub(ObservableList<Person> persons, ObservableList<Project> projects) {
            this.persons = persons;
            this.projects = projects;
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return FXCollections.unmodifiableObservableList(persons);
        }

        @Override
        public ObservableList<Project> getProjectList() {
            return FXCollections.unmodifiableObservableList(projects);
        }
    }

    static class ModelStub implements Model {
        private final ObservableList<Person> persons;
        private final ObservableList<Project> projects;
        private final ReadOnlyProjectBook roBookView;
        private Path projectBookFilePath = Path.of("test.json");
        private final UserPrefs prefs = new UserPrefs();

        ModelStub(ObservableList<Person> persons, ObservableList<Project> projects) {
            this.persons = persons;
            this.projects = projects;
            this.roBookView = new ReadOnlyProjectBookStub(persons, projects);
        }

        private static String normalize(String s) {
            return s == null ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase();
        }

        @Override public Optional<Project> findProjectByName(String name) {
            String needle = normalize(name);
            for (Project p : projects) {
                if (normalize(p.getName().fullName).equals(needle)) {
                    return Optional.of(p);
                }
            }
            return Optional.empty();
        }

        @Override public void setProject(Project project) {
            String n = normalize(project.getName().fullName);
            for (int i = 0; i < projects.size(); i++) {
                if (normalize(projects.get(i).getName().fullName).equals(n)) {
                    projects.set(i, project);
                    return;
                }
            }
            projects.add(project);
        }

        // ----- Lists & filters -----
        @Override public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override public ObservableList<Project> getFilteredProjectList() {
            return projects;
        }

        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {}
        @Override public void updateFilteredProjectList(Predicate<Project> predicate) {}

        // ----- Prefs & file path -----
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {}
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return prefs;
        }

        @Override public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override public void setGuiSettings(GuiSettings guiSettings) {}
        @Override public Path getProjectBookFilePath() {
            return projectBookFilePath;
        }

        @Override public void setProjectBookFilePath(Path projectBookFilePath) {
            this.projectBookFilePath = projectBookFilePath;
        }

        // ----- ProjectBook -----
        @Override public void setProjectBook(ReadOnlyProjectBook projectBook) {}
        @Override public ReadOnlyProjectBook getProjectBook() {
            return roBookView;
        }

        // ----- People ops (unused in these tests) -----
        @Override public boolean hasPerson(Person person) {
            return false;
        }

        @Override public void deletePerson(Person target) {}
        @Override public void addPerson(Person person) {
            persons.add(person);
        }

        @Override public void setPerson(Person target, Person editedPerson) {}
        @Override public void setPersonInPlace(Person person) {}

        // ----- Project ops (unused in these tests) -----
        @Override public boolean hasProject(Project project) {
            return false;
        }

        @Override public void addProject(Project project) {
            projects.add(project);
        }

        @Override public void deleteProject(Project project) {
            projects.remove(project);
        }
    }
}
