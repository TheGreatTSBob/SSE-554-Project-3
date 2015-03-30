
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BankClient {
    
    static int portNumber = 4444;
    static String computerName = "localhost";
    ObjectOutputStream outToServer;
    ObjectInputStream inFromServer;
    
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
            outToServer.writeObject(i);
            outToServer.writeObject(name);
            outToServer.writeObject(password);
            outToServer.writeObject(balance);
            outToServer.writeObject(accountType);
            boolean complete = (boolean)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
    }
    
    public void removeAccount(String name)
    {
        try
        {
            Integer i = 1;
            outToServer.writeObject(i);
            outToServer.writeObject(name);
            boolean complete = (boolean)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
    }
    
    public boolean authenticate(String name, String password)
    {
        try
        {
            Integer i = 2;
            outToServer.writeObject(i);
            outToServer.writeObject(name);
            outToServer.writeObject(password);
            return (boolean)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return false;
    }
    
    public boolean accountAction(String name, double amount)
    {
        try
        {
            Integer i = 3;
            outToServer.writeObject(i);
            outToServer.writeObject(name);
            outToServer.writeObject(amount);
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
            outToServer.writeObject(i);
            ArrayList<String> account = (ArrayList<String>)inFromServer.readObject();
            boolean complete = (boolean)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return null;
    }
    
    public ArrayList<String> initAccounts()
    {
        try
        {
            Integer i = 5;
            outToServer.writeObject(i);
            ArrayList<String> accounts = (ArrayList<String>)inFromServer.readObject();
            boolean complete = (boolean)inFromServer.readObject();
            return accounts;
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return null;
    }
    
    public Double getBalance(String name)
    {
        Double balance = 0.0;
        
        try
        {
            Integer i = 6;
            outToServer.writeObject(i);
            outToServer.writeObject(name);
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
            outToServer.writeObject(i);
            outToServer.writeObject(name);
            text = (String)inFromServer.readObject();
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        
        return text;
    }
}
