package aboodZaidLibrary;

public class clsEncryption {

    public static String encryptText(String text, int encryptionKey) {
    	if (encryptionKey < 0) {
            throw new IllegalArgumentException("Encryption key must not be negative. Given: " + encryptionKey);
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = (char) (text.charAt(i) + encryptionKey);
            result.append(c);
        }

        return result.toString();
    }
    public static String decryptText(String text, int encryptionKey) {
    	if (encryptionKey < 0) {
            throw new IllegalArgumentException("Encryption key must not be negative. Given: " + encryptionKey);
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = (char) (text.charAt(i) - encryptionKey);
            result.append(c);
        }

        return result.toString();
    }
}