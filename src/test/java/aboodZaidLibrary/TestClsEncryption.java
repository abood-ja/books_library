package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestClsEncryption {

    @Test
    void testEncryptText() {
        String original = "abcd";
        String expected = "bcde";
        String encrypted = clsEncryption.encryptText(original, 1);
        assertEquals(expected, encrypted);
    }

    @Test
    void testEncryptText_InvalidEncryptionKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsEncryption.encryptText("1234", -1);
        });
        assertTrue(exception.getMessage().contains("Encryption key must not be negative."));
    }

    @Test
    void testDecryptText() {
        String original = "1234";
        String encrypted = clsEncryption.encryptText(original, 1);
        String decrypted = clsEncryption.decryptText(encrypted, 1);
        assertEquals(original, decrypted);
    }

    @Test
    void testDecryptText_InvalidEncryptionKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsEncryption.decryptText("1234", -1);
        });
        assertTrue(exception.getMessage().contains("Encryption key must not be negative."));
    }

    @Test
    void testEmptyStringEncryptionDecryption() {
        String empty = "";
        String encrypted = clsEncryption.encryptText(empty, 5);
        String decrypted = clsEncryption.decryptText(encrypted, 5);
        assertEquals("", encrypted);
        assertEquals("", decrypted);
    }

    @Test
    void testZeroKeyEncryptionDecryption() {
        String text = "Hello World!";
        String encrypted = clsEncryption.encryptText(text, 0);
        String decrypted = clsEncryption.decryptText(encrypted, 0);
        assertEquals(text, encrypted, "Encryption with key 0 should not change text");
        assertEquals(text, decrypted, "Decryption with key 0 should not change text");
    }

    @Test
    void testUnicodeCharacters() {
        String text = "áçß字😊";
        int key = 3;
        String encrypted = clsEncryption.encryptText(text, key);
        String decrypted = clsEncryption.decryptText(encrypted, key);
        assertEquals(text, decrypted, "Decrypting Unicode text should return original text");
    }

    @Test
    void testLongStringEncryptionDecryption() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) sb.append("A");
        String text = sb.toString();
        int key = 7;
        String encrypted = clsEncryption.encryptText(text, key);
        String decrypted = clsEncryption.decryptText(encrypted, key);
        assertEquals(text, decrypted, "Decrypting long string should return original text");
    }
}
