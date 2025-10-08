package loopin.projectbook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.commons.exceptions.DataLoadingException;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.ReadOnlyUserPrefs;
import loopin.projectbook.model.UserPrefs;

/**
 * Manages storage of ProjectBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ProjectBookStorage projectBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code ProjectBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(ProjectBookStorage projectBookStorage, UserPrefsStorage userPrefsStorage) {
        this.projectBookStorage = projectBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ ProjectBook methods ==============================

    @Override
    public Path getProjectBookFilePath() {
        return projectBookStorage.getProjectBookFilePath();
    }

    @Override
    public Optional<ReadOnlyProjectBook> readProjectBook() throws DataLoadingException {
        return readProjectBook(projectBookStorage.getProjectBookFilePath());
    }

    @Override
    public Optional<ReadOnlyProjectBook> readProjectBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return projectBookStorage.readProjectBook(filePath);
    }

    @Override
    public void saveProjectBook(ReadOnlyProjectBook projectBook) throws IOException {
        saveProjectBook(projectBook, projectBookStorage.getProjectBookFilePath());
    }

    @Override
    public void saveProjectBook(ReadOnlyProjectBook projectBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        projectBookStorage.saveProjectBook(projectBook, filePath);
    }

}
