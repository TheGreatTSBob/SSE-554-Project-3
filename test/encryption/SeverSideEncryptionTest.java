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
public class SeverSideEncryptionTest {
    
    public SeverSideEncryptionTest() {
    }

     @Test
     public void KeyGeneratorTest() throws NoSuchAlgorithmException {
     }
    
     @Test
     public void EncryptionTest() {
         
         String original = "If you want to increase your sucess rate,"
                 + "double your failure rate.";
         
         ServerSideEncryption decrypt = new ServerSideEncryption();
         ServerSideEncryption encrypt = new ServerSideEncryption(decrypt.getPublicKey());
         
         byte[] wrappedKey = encrypt.wrapSymmetricKey();
         byte[] encrypted = encrypt.Encrypt(original);
         
         assertFalse(original.getBytes() == encrypted);
         
         String decrypted = decrypt.Decrypt(wrappedKey, encrypted);
         
         assertEquals(original, decrypted);
     }
}
