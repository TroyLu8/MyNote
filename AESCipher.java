import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class AESCipher {

        private static Cipher cipher;
        static SecretKeySpec secretKey;
    
        static void init(char[] pass) { 
            try {

                KeySpec keySpec = new PBEKeySpec(pass, "some salt".getBytes(StandardCharsets.UTF_8), 10000, 256);
                SecretKey secretKey1 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(keySpec);

                secretKey = new SecretKeySpec(secretKey1.getEncoded(), "AES");

                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    
            } 
            catch (Exception e) { e.printStackTrace(); }
            
        }
    
        static byte[] encrypt(byte[] plaintext, byte[] iv){
    
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
                
                return cipher.doFinal(plaintext);
            } 
            catch (Exception e) { e.printStackTrace(); }
    
            return null;
        }
    
        static String decrypt(byte[] ciphertext, byte[] iv){
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    
                return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
            } 
            catch (Exception e) { e.printStackTrace(); }
    
            return null;
        }
    }