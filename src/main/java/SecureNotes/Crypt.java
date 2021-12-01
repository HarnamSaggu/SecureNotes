package SecureNotes;

import com.lambdaworks.crypto.SCryptUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

class Crypt {
   static String hash(String password) {
      return SCryptUtil.scrypt(password, 16, 16, 16);
   }

   static boolean hashMatches(String newPassword, String hashPassword) {
      return SCryptUtil.check(newPassword, hashPassword);
   }

   static String encrypt(String strToEncrypt, char[] secret) {
      try {
         SecretKeySpec secretKey = null;
         byte[] key;
         MessageDigest sha;
         try {
            key = new String(secret).getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
         } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
         }
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, secretKey);
         return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
      } catch (Exception e) {
         System.out.println("Error while encrypting: " + e);
      }
      return null;
   }

   static String decrypt(String strToDecrypt, String secret) {
      try {
         SecretKeySpec secretKey = null;
         byte[] key;
         MessageDigest sha;
         try {
            key = secret.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
         } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
         }
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
         cipher.init(Cipher.DECRYPT_MODE, secretKey);
         return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
      } catch (Exception e) {
         System.out.println("Error while decrypting: " + e);
      }
      return null;
   }
}
