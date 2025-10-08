package loopin.projectbook.model;

import java.nio.file.Path;

import loopin.projectbook.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getProjectBookFilePath();

}
