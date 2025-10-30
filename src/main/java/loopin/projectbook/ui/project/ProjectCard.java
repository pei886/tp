package loopin.projectbook.ui.project;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.ui.UiPart;
import loopin.projectbook.ui.person.PersonCard;


/**
 * An UI component that displays information of a {@code Project}.
 */
public class ProjectCard extends UiPart<Region> {

    private static final String FXML = "ProjectListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Project project;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label createdAt;
    @FXML
    private Label lastUpdateMessage;
    @FXML
    private VBox membersContainer;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public ProjectCard(Project project, int displayedIndex) {
        super(FXML);
        this.project = project;
        id.setText(displayedIndex + ". ");
        name.setText(project.getName().fullName);
        description.setText(project.getDescription().toString());
        createdAt.setText(project.getCreatedAt()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
        lastUpdateMessage.setText(project.getLastUpdateAsString());

        populateMembersByRole();

    }
    /**
     * Populates the members section organized by their roles.
     */
    private void populateMembersByRole() {
        // Get members categorized by role
        List<Person> committee = project.getAllPeople().stream()
                .filter(person -> person.getRole().fullRole.toLowerCase().startsWith("committee"))
                .sorted(Comparator.comparing(person -> person.getName().toString()))
                .collect(Collectors.toList());

        List<Person> organisations = project.getAllPeople().stream()
                .filter(person -> person.getRole().fullRole.toLowerCase().startsWith("organisation"))
                .sorted(Comparator.comparing(person -> person.getName().toString()))
                .collect(Collectors.toList());

        List<Person> volunteers = project.getAllPeople().stream()
                .filter(person -> person.getRole().fullRole.equalsIgnoreCase("volunteer"))
                .sorted(Comparator.comparing(person -> person.getName().toString()))
                .collect(Collectors.toList());

        if (!committee.isEmpty()) {
            addRoleSection("Committee:", committee);
        }

        if (!organisations.isEmpty()) {
            addRoleSection("Organisations:", organisations);
        }

        if (!volunteers.isEmpty()) {
            addRoleSection("Volunteers:", volunteers);
        }
    }

    /**
     * Adds a section for a specific role with its members displayed as PersonCards.
     */
    private void addRoleSection(String roleLabel, List<Person> members) {
        Label roleLabelNode = new Label(roleLabel);
        membersContainer.getChildren().add(roleLabelNode);

        int index = 1;
        for (Person person : members) {
            PersonCard personCard = new PersonCard(person, index++);
            membersContainer.getChildren().add(personCard.getRoot());
        }
    }
}
