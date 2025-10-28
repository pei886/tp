package loopin.projectbook.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import loopin.projectbook.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label telegram;
    @FXML
    private Label role;
    @FXML
    private Label email;
    @FXML
    private Label numberOfProjects;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().map(phone -> phone.value).orElse("Phone: nil"));
        role.setText(person.getRole().fullRole);
        telegram.setText(person.getTelegram().map(telegram -> "@" + telegram.value).orElse("Telegram: nil"));
        email.setText(person.getEmail().value);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        person.getRemarks()
                .forEach(remark -> {
                    Label remarkLabel = new Label(remark.content);
                    // Assign a CSS class for the orange background (e.g., 'remark_tag')
                    remarkLabel.getStyleClass().add("remark_tag");
                    tags.getChildren().add(remarkLabel);
                });

        String projectNames = person.getProjects().stream()
                .map(project -> project.getName().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        numberOfProjects.setText(Integer.toString(person.getNumberOfProjects()) + " project(s): " + projectNames);
    }
}
