
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
            boolean complete = (boolean)inFromServer.readObject();
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
            boolean complete = (boolean)inFromServer.readObject();
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
            return (boolean)inFromServer.readObject();
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
            boolean complete = (boolean)inFromServer.readObject();
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
            ArrayList<String> results = (ArrayList<String>)inFromServer.readObject();
            boolean complete = (boolean)inFromServer.readObject();
            
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
            ArrayList<String> accounts = (ArrayList<String>)inFromServer.readObject();
            boolean complete = (boolean)inFromServer.readObject();
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
            balance = (Double)inFromServer.readObject();
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
            text = (String)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return text;
    }
}
