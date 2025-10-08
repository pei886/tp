package loopin.projectbook.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.person.Person;

/**
 * An Immutable ProjectBook that is serializable to JSON format.
 */
@JsonRootName(value = "projectbook")
class JsonSerializableProjectBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableProjectBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableProjectBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyProjectBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableProjectBook}.
     */
    public JsonSerializableProjectBook(ReadOnlyProjectBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code ProjectBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ProjectBook toModelType() throws IllegalValueException {
        ProjectBook projectBook = new ProjectBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (projectBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            projectBook.addPerson(person);
        }
        return projectBook;
    }

}
