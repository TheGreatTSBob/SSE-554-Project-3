/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package encryptiontest;

import java.util.Arrays;

/**
 *
 * @author Josh
 */
public class EncryptionTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ServerDecryptor sd = new ServerDecryptor();
        ClientEncryptor ce = new ClientEncryptor(sd.getPublic());
        sd.initCipher(ce.getKey());
        
        String test = "Let us now do this with something a bit longer and see how it works.";
        System.out.println(test);
        byte[] encrypted = ce.encrypt(test);
        System.out.println(encrypted);
        System.out.println(sd.decrypt(encrypted));
    }
    
}
