package loopin.projectbook.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.commons.exceptions.DataLoadingException;
import loopin.projectbook.commons.exceptions.IllegalValueException;
import loopin.projectbook.commons.util.FileUtil;
import loopin.projectbook.commons.util.JsonUtil;
import loopin.projectbook.model.ReadOnlyProjectBook;

/**
 * A class to access ProjectBook data stored as a json file on the hard disk.
 */
public class JsonProjectBookStorage implements ProjectBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonProjectBookStorage.class);

    private Path filePath;

    public JsonProjectBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getProjectBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyProjectBook> readProjectBook() throws DataLoadingException {
        return readProjectBook(filePath);
    }

    /**
     * Similar to {@link #readProjectBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyProjectBook> readProjectBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableProjectBook> jsonProjectBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableProjectBook.class);
        if (!jsonProjectBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonProjectBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveProjectBook(ReadOnlyProjectBook projectBook) throws IOException {
        saveProjectBook(projectBook, filePath);
    }

    /**
     * Similar to {@link #saveProjectBook(ReadOnlyProjectBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveProjectBook(ReadOnlyProjectBook projectBook, Path filePath) throws IOException {
        requireNonNull(projectBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableProjectBook(projectBook), filePath);
    }

}
