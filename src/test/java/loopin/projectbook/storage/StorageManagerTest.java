package loopin.projectbook.storage;

import static loopin.projectbook.testutil.TypicalPersons.getTypicalProjectBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonProjectBookStorage projectBookStorage = new JsonProjectBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(projectBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void projectBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonProjectBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonProjectBookStorageTest} class.
         */
        ProjectBook original = getTypicalProjectBook();
        storageManager.saveProjectBook(original);
        ReadOnlyProjectBook retrieved = storageManager.readProjectBook().get();
        assertEquals(original, new ProjectBook(retrieved));
    }

    @Test
    public void getProjectBookFilePath() {
        assertNotNull(storageManager.getProjectBookFilePath());
    }

}
