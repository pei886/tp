package loopin.projectbook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static loopin.projectbook.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.commons.util.JsonUtil;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.testutil.TypicalPersons;

public class JsonSerializableProjectBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableProjectBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsProjectBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonProjectBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonProjectBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableProjectBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableProjectBook.class).get();
        ProjectBook projectBookFromFile = dataFromFile.toModelType();
        ProjectBook typicalPersonsProjectBook = TypicalPersons.getTypicalProjectBook();
        assertEquals(projectBookFromFile, typicalPersonsProjectBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableProjectBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableProjectBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableProjectBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableProjectBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableProjectBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

}
