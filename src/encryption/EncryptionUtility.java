/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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
 * @author TSBob (Daryl Ebanks)
 */
public class EncryptionUtility {
    
    public static final int KEYSIZE = 512;
    private Cipher cipher;
    private SecretKey secretKey;
    private Key publicKey;
    private Key privateKey;
    byte[] wrappedKey;
    
    public EncryptionUtility()
    {
        createKeyPair();
    }
    
    public EncryptionUtility(Key publicKey)
    {
        this.publicKey = publicKey;
        generateSymmetricKey();
    }
    
    public byte[] Encrypt(String input)
    {
        try {
                        
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] output = null;
        
        try {
            byte[] in = input.getBytes(Charset.forName("UTF-8"));
            output = cipher.doFinal(in);
            
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    public String Decrypt( byte[] input)
    {
        try {
                    
            // Create symmetric cipher from received key
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String output = null;
        
        try {
            byte[] decrypt = cipher.doFinal(input);
            output = new String(decrypt, Charset.forName("UTF-8"));
            
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    private void createKeyPair()
    {
        try {
            KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            pairgen.initialize(KEYSIZE, random);
            KeyPair keyPair = pairgen.generateKeyPair();
            
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generateSymmetricKey()
    {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keygen.init(random);
            secretKey = keygen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public byte[] wrapSymmetricKey()
    {
        try {
            Cipher pubCipher = Cipher.getInstance("RSA");
            pubCipher.init(Cipher.WRAP_MODE, publicKey);
            return pubCipher.wrap(secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public void unwrapKey(byte [] key)
    {
        try {
            this.wrappedKey = key;
            
            Cipher publicCipher = Cipher.getInstance("RSA");
            publicCipher.init(Cipher.UNWRAP_MODE, privateKey);
            secretKey = (SecretKey) publicCipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Key getPublicKey()
    {
        return publicKey;
    }
}
