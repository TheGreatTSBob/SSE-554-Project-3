/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TSBob
 */
public class EncryptionTest {
    
    public EncryptionTest() {
    }
    
     @Test
     public void EncryptionTest() {
         
         String original = "If you want to increase your sucess rate,"
                 + "double your failure rate.";
         
         EncryptionUtility decrypter = new EncryptionUtility();
         EncryptionUtility encryptor = new EncryptionUtility(decrypter.getPublicKey());
         
         byte[] wrappedKey = encryptor.wrapSymmetricKey();
         byte[] encryptedMessage = encryptor.Encrypt(original);
         
         assertFalse(original.getBytes() == encryptedMessage);
         
         decrypter.unwrapKey(wrappedKey);
         String decryptedMessage = decrypter.Decrypt(encryptedMessage);
         
         assertEquals(original, decryptedMessage);
     }
}
