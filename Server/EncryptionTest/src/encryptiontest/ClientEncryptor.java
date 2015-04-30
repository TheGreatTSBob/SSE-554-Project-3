/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package encryptiontest;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Josh
 */
public class ClientEncryptor {
    
    private Cipher cipher;
    byte[] wrappedKey;
    SecretKey key;
    
    public ClientEncryptor(Key publicKey) {
        try {
            // Generate a random symmetric key
            System.out.println("Generate AES key");
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keygen.init(random);
            key = keygen.generateKey();
            System.out.println("AES key generated");
            
            // Wrap the symmetric key in the public key and send
            // it back to the server
            Cipher publiccipher = Cipher.getInstance("RSA");
            publiccipher.init(Cipher.WRAP_MODE, publicKey);
            wrappedKey = publiccipher.wrap(key);
            System.out.println("Wrapped AES key");
            
            // Create the symmetric cipher for client side
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            System.out.println("Client AES cipher created");
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ClientEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(ClientEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ClientEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public byte[] encrypt(String input)
    {
        try {
            byte[] in = input.getBytes(Charset.forName("UTF-8"));
            System.out.println(in);
            byte[] output = cipher.doFinal(in);
            
            return output;
            
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ClientEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(ClientEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public byte[] getKey()
    {
        return wrappedKey;
    }
    
    public Key getClientKey()
    {
        return key;
    }
}
