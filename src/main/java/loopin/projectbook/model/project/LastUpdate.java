package loopin.projectbook.model.project;

import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the last update made to a Project in the project book.
 * Guarantees: immutable; is always valid
 */
public class LastUpdate {

    public static final String DEFAULT_MESSAGE = "No updates yet";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    private final String updateMessage;
    private final LocalDateTime timestamp;

    /**
     * Constructs a default {@code LastUpdate} with no updates.
     */
    public LastUpdate() {
        this.updateMessage = DEFAULT_MESSAGE;
        this.timestamp = null;
    }

    /**
     * Constructs a {@code LastUpdate} with the specified message and current timestamp.
     *
     * @param updateMessage A description of the update.
     */
    public LastUpdate(String updateMessage) {
        requireNonNull(updateMessage);
        this.updateMessage = updateMessage;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs a {@code LastUpdate} with the specified message and timestamp.
     * Used for reconstructing from storage.
     *
     * @param updateMessage A description of the update.
     * @param timestamp The time when the update occurred.
     */
    public LastUpdate(String updateMessage, LocalDateTime timestamp) {
        requireNonNull(updateMessage);
        requireNonNull(timestamp);
        this.updateMessage = updateMessage;
        this.timestamp = timestamp;
    }

    /**
     * Creates a LastUpdate for adding a remark to a member.
     */
    public static LastUpdate remarkAdded(Name personName, String remark) {
        String message = String.format("Added remark to %s: \"%s\"", personName, remark);
        return new LastUpdate(message);
    }

    /**
     * Creates a LastUpdate for completing a remark of a member.
     */
    public static LastUpdate remarkCompleted(String personName, String remark) {
        String message = String.format("Completed remark for %s: \"%s\"", personName, remark);
        return new LastUpdate(message);
    }

    /**
     * Creates a LastUpdate for adding a new member to the project.
     */
    public static LastUpdate memberAdded(Person person) {
        String message = String.format("Added new member: %s", person.getName());
        return new LastUpdate(message);
    }

    /**
     * Creates a LastUpdate for removing a member from the project.
     */
    public static LastUpdate memberRemoved(Person person) {
        String message = String.format("Removed member: %s", person.getName());
        return new LastUpdate(message);
    }

    /**
     * Creates a LastUpdate for a custom message.
     */
    public static LastUpdate custom(String message) {
        return new LastUpdate(message);
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean hasUpdate() {
        return timestamp != null;
    }

    @Override
    public String toString() {
        if (!hasUpdate()) {
            return DEFAULT_MESSAGE;
        }
        return String.format("[%s] %s", timestamp.format(FORMATTER), updateMessage);
    }

    /**
     * Returns a short summary without timestamp (useful for UI).
     */
    public String toShortString() {
        return updateMessage;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LastUpdate)) {
            return false;
        }

        LastUpdate otherUpdate = (LastUpdate) other;
        return updateMessage.equals(otherUpdate.updateMessage)
                && ((timestamp == null && otherUpdate.timestamp == null)
                || (timestamp != null && timestamp.equals(otherUpdate.timestamp)));
    }

    @Override
    public int hashCode() {
        return timestamp != null
                ? updateMessage.hashCode() + timestamp.hashCode()
                : updateMessage.hashCode();
    }
}