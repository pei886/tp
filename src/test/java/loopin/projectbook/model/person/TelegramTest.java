package loopin.projectbook.model.person;

import static loopin.projectbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TelegramTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Telegram(null));
    }

    @Test
    public void constructor_invalidTelegram_throwsIllegalArgumentException() {
        String invalidTelegram = "";
        assertThrows(IllegalArgumentException.class, () -> new Telegram(invalidTelegram));
    }

    @Test
    public void isValidTelegram() {
        // null telegram
        assertThrows(NullPointerException.class, () -> Telegram.isValidTelegram(null));

        // invalid telegrams
        assertFalse(Telegram.isValidTelegram("")); // empty string
        assertFalse(Telegram.isValidTelegram("John Doe")); // space present
        assertFalse(Telegram.isValidTelegram("John")); // 4 characters
        assertFalse(Telegram.isValidTelegram("123456789012345678901234567890123")); // 33 characters
        assertFalse(Telegram.isValidTelegram("John-Doe")); // non alphanumeric and not underscore character present

        // valid telegrams
        assertTrue(Telegram.isValidTelegram("John_Doe123"));
        assertTrue(Telegram.isValidTelegram("JohnD")); // 5 characters
        assertTrue(Telegram.isValidTelegram("1234567890123456789012356789012")); // 32 characters
    }

    @Test
    public void equals() {
        Telegram telegram = new Telegram("Valid_Telegram");

        // same values -> returns true
        assertTrue(telegram.equals(new Telegram("Valid_Telegram")));

        // same object -> returns true
        assertTrue(telegram.equals(telegram));

        // null -> returns false
        assertFalse(telegram.equals(null));

        // different types -> returns false
        assertFalse(telegram.equals(5.0f));

        // different values -> returns false
        assertFalse(telegram.equals(new Telegram("Other_Valid_Telegram")));
    }
}
