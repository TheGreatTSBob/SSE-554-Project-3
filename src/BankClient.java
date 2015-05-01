
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import encryption.*;
import java.security.Key;

public class BankClient {
    
    static int portNumber = 4444;
    static String computerName = "localhost";
    ObjectOutputStream outToServer;
    ObjectInputStream inFromServer;
    ClientEncryptor ce;
    EncryptionUtility decrypter;
    
    public BankClient()
    {
        
    }
    
    public void connect() {
        System.out.println("Attempting to connect to " + computerName + ":" + portNumber);
        try {
            Socket csocket = new Socket(computerName, portNumber);
            System.out.println("Connection successful!");
            
            outToServer = new ObjectOutputStream(csocket.getOutputStream());
            inFromServer = new ObjectInputStream(csocket.getInputStream());
            
            System.out.println((String)inFromServer.readObject());
            
            ce = new ClientEncryptor((Key)inFromServer.readObject());
            outToServer.writeObject(ce.getKey());
            
            decrypter = new EncryptionUtility(); 
            outToServer.writeObject(decrypter.getPublicKey());
            decrypter.unwrapKey((byte[]) inFromServer.readObject());
            
            // work with server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> init()
    {
        try
        {
            return (ArrayList<String>)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        return null;
    }
    
    public void addAccount(String name, String password, double balance, Integer accountType)
    {
        try
        {
            Integer i = 0;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            outToServer.writeObject(ce.encrypt(name));
            outToServer.writeObject(ce.encrypt(password));
            outToServer.writeObject(ce.encrypt(Double.toString(balance)));
            outToServer.writeObject(ce.encrypt(Integer.toString(accountType)));
            
            boolean complete = Boolean.parseBoolean(
                    decrypter.Decrypt( (byte []) inFromServer.readObject()));
        
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
    }
    
    public void removeAccount(String name)
    {
        try
        {
            Integer i = 1;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            outToServer.writeObject(ce.encrypt(name));
            boolean complete = Boolean.parseBoolean(
                    decrypter.Decrypt( (byte []) inFromServer.readObject()));
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
    }
    
    public boolean authenticate(String name, String password)
    {
        try
        {
            Integer i = 2;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            outToServer.writeObject(ce.encrypt(name));
            outToServer.writeObject(ce.encrypt(password));
            return (boolean)Boolean.parseBoolean(
                    decrypter.Decrypt( (byte []) inFromServer.readObject()));
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return false;
    }
    
    public boolean accountAction(String name, String password,double amount)
    {
        try
        {
            Integer i = 3;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            outToServer.writeObject(ce.encrypt(name));
            outToServer.writeObject(ce.encrypt(Double.toString(amount)));
            outToServer.writeObject(ce.encrypt(password));
            boolean complete = Boolean.parseBoolean(
                    decrypter.Decrypt( (byte []) inFromServer.readObject()));;
            return complete;
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        return false;
    }
    
    public ArrayList<String> interest()
    {
        
        try
        {
            Integer i = 4;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            ArrayList<byte[]> encryptedResults = (ArrayList<byte[]>)inFromServer.readObject();
            ArrayList<String> results = new ArrayList<>();
            for(byte[] encryptedName: encryptedResults)
            {
                results.add(decrypter.Decrypt(encryptedName));
            }
            
            boolean complete = Boolean.parseBoolean(
                    decrypter.Decrypt( (byte []) inFromServer.readObject()));;
            
            return results;
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
                
        return null;
    }
    
    public ArrayList<String> initAccounts()
    {
        try
        {
            Integer i = 5;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            ArrayList<byte[]> encryptedNames = (ArrayList<byte[]>)inFromServer.readObject();
            ArrayList<String> accounts = new ArrayList<>();
            for(byte[] encryptedName: encryptedNames)
            {
                accounts.add(decrypter.Decrypt(encryptedName));
            }
            boolean complete = Boolean.parseBoolean(
                    decrypter.Decrypt( (byte []) inFromServer.readObject()));;
            return accounts;
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return null;
    }
    
    public Double getBalance(String name, String password)
    {
        Double balance = 0.0;
        
        try
        {
            Integer i = 6;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            outToServer.writeObject(ce.encrypt(name));
            outToServer.writeObject(ce.encrypt(password));
            String temp = decrypter.Decrypt((byte[])inFromServer.readObject());
            balance = Double.parseDouble(temp);
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return balance;
    }
    
    public String withdrawCheck(String name)
    {
        String text = "";
        try
        {
            Integer i = 7;
            outToServer.writeObject(ce.encrypt(Integer.toString(i)));
            outToServer.writeObject(ce.encrypt(name));
            text = decrypter.Decrypt((byte[])inFromServer.readObject());
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return text;
    }
}
