package loopin.projectbook.storage;

import static loopin.projectbook.testutil.Assert.assertThrows;
import static loopin.projectbook.testutil.TypicalPersons.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import loopin.projectbook.commons.exceptions.DataLoadingException;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.ReadOnlyProjectBook;

public class JsonProjectBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonProjectBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readProjectBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readProjectBook(null));
    }

    private java.util.Optional<ReadOnlyProjectBook> readProjectBook(String filePath) throws Exception {
        return new JsonProjectBookStorage(Paths.get(filePath)).readProjectBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readProjectBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readProjectBook("notJsonFormatProjectBook.json"));
    }

    @Test
    public void readProjectBook_invalidPersonProjectBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readProjectBook("invalidPersonProjectBook.json"));
    }

    @Test
    public void readProjectBook_invalidAndValidPersonProjectBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readProjectBook("invalidAndValidPersonProjectBook.json"));
    }

    @Test
    public void readAndSaveProjectBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempProjectBook.json");
        ProjectBook original = getTypicalProjectBook();
        JsonProjectBookStorage jsonProjectBookStorage = new JsonProjectBookStorage(filePath);

        // Save in new file and read back
        jsonProjectBookStorage.saveProjectBook(original, filePath);
        ReadOnlyProjectBook readBack = jsonProjectBookStorage.readProjectBook(filePath).get();
        assertEquals(original, new ProjectBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonProjectBookStorage.saveProjectBook(original, filePath);
        readBack = jsonProjectBookStorage.readProjectBook(filePath).get();
        assertEquals(original, new ProjectBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonProjectBookStorage.saveProjectBook(original); // file path not specified
        readBack = jsonProjectBookStorage.readProjectBook().get(); // file path not specified
        assertEquals(original, new ProjectBook(readBack));

    }

    @Test
    public void saveProjectBook_nullProjectBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveProjectBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code projectBook} at the specified {@code filePath}.
     */
    private void saveProjectBook(ReadOnlyProjectBook projectBook, String filePath) {
        try {
            new JsonProjectBookStorage(Paths.get(filePath))
                    .saveProjectBook(projectBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveProjectBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveProjectBook(new ProjectBook(), null));
    }
}
