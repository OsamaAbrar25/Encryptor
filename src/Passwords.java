// Required imports
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

class Passwords {

    // String of characters to randamly chosse salt from.
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnoppqrstuvwxyz";
    // Object to generate random numbers.
    private static Random RANDOM = new SecureRandom();
    // No. of iteration.
    private static final int ITERATION = 10000;
    // Length of the key.
    private static final int KEY_LENGTH = 256;

    // Method for generating the salt.
    static String getSalt(int length)
    {
        StringBuilder salt = new  StringBuilder(length);
        // Generating random salt with the help of the Random method and the above given characters.
        for (int i = 1; i <= length; i++)
        {
            salt.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return salt.toString();
    }


    // Method for encrypting the given password  with salt.
    private static byte[] hash(char[] password, byte[] salt)
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try{
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return secretKeyFactory.generateSecret(spec).getEncoded();
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            throw new AssertionError("Error while hashing a password"+e.getMessage(), e);
        }
        finally {
            spec.clearPassword();
        }
    }


    static String generateSecurePassword(String password, String salt) {
        String returnValue;
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        returnValue = Base64.getEncoder().encodeToString(securePassword);
        return returnValue;
    }

    // For verifying the password.
    static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt)
    {
        boolean returnValue;
        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);
        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }
}
