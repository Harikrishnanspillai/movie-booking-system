public class PassHash {
    static int key = 3;
    public static String encode(String password) {
        if (password == null) {
            return null;
        }
        StringBuilder encoded = new StringBuilder();
        for (char ch : password.toCharArray()) {
            // Shift character by key
            char shifted = (char)(ch + key);
            encoded.append(shifted);
        }
        return encoded.toString();
    }

    public static String decode(String encodedPassword) {
        if (encodedPassword == null) {
            return null;
        }
        StringBuilder decoded = new StringBuilder();
        for (char ch : encodedPassword.toCharArray()) {
            // Shift character back by key
            char shifted = (char)(ch - key);
            decoded.append(shifted);
        }
        return decoded.toString();
    }
}
