package loopin.projectbook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import loopin.projectbook.commons.exceptions.DataLoadingException;
import loopin.projectbook.model.ReadOnlyProjectBook;
import loopin.projectbook.model.ReadOnlyUserPrefs;
import loopin.projectbook.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends ProjectBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getProjectBookFilePath();

    @Override
    Optional<ReadOnlyProjectBook> readProjectBook() throws DataLoadingException;

    @Override
    void saveProjectBook(ReadOnlyProjectBook addressBook) throws IOException;

}
