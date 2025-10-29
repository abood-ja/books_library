package aboodZaidLibrary;

public class clsEncryption {

    public static String encryptText(String text, int encryptionKey) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = (char) (text.charAt(i) + encryptionKey);
            result.append(c);
        }

        return result.toString();
    }
    public static String decryptText(String text, int encryptionKey) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = (char) (text.charAt(i) - encryptionKey);
            result.append(c);
        }

        return result.toString();
    }
}