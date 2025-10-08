package loopin.projectbook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import loopin.projectbook.commons.exceptions.DataLoadingException;
import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.ReadOnlyProjectBook;

/**
 * Represents a storage for {@link ProjectBook}.
 */
public interface ProjectBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getProjectBookFilePath();

    /**
     * Returns ProjectBook data as a {@link ReadOnlyProjectBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyProjectBook> readProjectBook() throws DataLoadingException;

    /**
     * @see #getProjectBookFilePath()
     */
    Optional<ReadOnlyProjectBook> readProjectBook(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyProjectBook} to the storage.
     * @param projectBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveProjectBook(ReadOnlyProjectBook projectBook) throws IOException;

    /**
     * @see #saveProjectBook(ReadOnlyProjectBook)
     */
    void saveProjectBook(ReadOnlyProjectBook projectBook, Path filePath) throws IOException;

}
