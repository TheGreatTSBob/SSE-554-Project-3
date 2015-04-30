/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package encryptiontest;

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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Josh
 */
public class ServerDecryptor {
    
    private Cipher cipher;
    public static final int KEYSIZE = 512;
    private Key publicKey;
    private Key privateKey;
    private SecretKey key;
    
    public ServerDecryptor ()
    {
        try {
            // Create key pair and pull out public key
            System.out.println("Begin public key generation");
            KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            pairgen.initialize(KEYSIZE, random);
            KeyPair keyPair = pairgen.generateKeyPair();
            
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            System.out.println("Created public key!");
            // Send public key to client
            
            // Receive symmetric key from client and unwrap
            // byte[] a = new byte[8];
            // initCipher(a);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initCipher(byte[] wrappedkey)
    {
        try {
            // Receive symmetric key from client and unwrap
            Cipher publiccipher = Cipher.getInstance("RSA");
            publiccipher.init(Cipher.UNWRAP_MODE, privateKey);
            key = (SecretKey) publiccipher.unwrap(wrappedkey, "AES", Cipher.SECRET_KEY);
            System.out.println("Received AES key!");
            
            // Create symmetric cipher from received key
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            System.out.println("Server AES cipher created");
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(ServerDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ServerDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String decrypt(byte[] input)
    {
        try {
            byte[] decrypt = cipher.doFinal(input);
            System.out.println(decrypt);
            String output = new String(decrypt, Charset.forName("UTF-8"));
            
            return output;
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(ServerDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(ServerDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public Key getPublic()
    {
        return publicKey;
    }
    
    public Key getServerKey()
    {
        return key;
    }
}
