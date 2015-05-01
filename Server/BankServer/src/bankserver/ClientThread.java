/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bankserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import banking.*;
import banking.Account.CompoundResult;
import Encryption.*;
import java.security.Key;

/**
 *
 * @author Josh
 */
public class ClientThread extends Thread{
    
    Bank bank;
    Socket csocket;
    ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;
    ServerDecryptor sd;
    EncryptionUtility encryptor;
    
    public ClientThread(Socket csocket)
    {
        this.csocket = csocket;
        bank = new Bank();
    }
    
    @Override
    public void run()
    {
        try {
            init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void init() throws IOException, ClassNotFoundException
    {
        this.outToClient = new ObjectOutputStream(csocket.getOutputStream());
        this.inFromClient = new ObjectInputStream(csocket.getInputStream());
        this.sd = new ServerDecryptor();
        
        outToClient.writeObject("Hello and welcome to the Bank Socket Server!");
        outToClient.writeObject(sd.getPublic());
        
        sd.initCipher((byte[]) inFromClient.readObject());
        
        //create an encryptor and set the public key;
        encryptor = new EncryptionUtility((Key) inFromClient.readObject());
        outToClient.writeObject(encryptor.wrapSymmetricKey());
        begin();
    }
    
    public void begin() throws IOException, ClassNotFoundException
    {
        try {
            while (!csocket.isClosed())
            {
                Integer todo;
                todo = Integer.parseInt(sd.decrypt((byte[])inFromClient.readObject()));

                boolean complete;

                switch(todo)
                {
                    // Add a user
                    case 0: complete = add();
                        outToClient.writeObject(encryptor.Encrypt(((Boolean)complete).toString()));
                        break;

                    // Remove a user
                    case 1: complete = remove();
                        outToClient.writeObject(encryptor.Encrypt(((Boolean)complete).toString()));
                        break;

                    // Authenticate login
                    case 2: complete = authenticate();
                        outToClient.writeObject(encryptor.Encrypt(((Boolean)complete).toString()));
                        break;

                    // Withdraw or deposit
                    case 3: complete = accountAction();
                        outToClient.writeObject(encryptor.Encrypt(((Boolean)complete).toString()));
                        break;

                    // Interest
                    case 4: complete = interest();
                        outToClient.writeObject(encryptor.Encrypt(((Boolean)complete).toString()));
                        break;

                    // Reset account list
                    case 5: complete = accountInit();
                        outToClient.writeObject(encryptor.Encrypt(((Boolean)complete).toString()));
                        break;
                        
                    // Get balance of account
                    case 6: complete = getBalance();
                        break;
                        
                    // Get number of withdrawals left
                    case 7: complete = withdrawals();
                        break;
                        
                    default: break;
                }
            }
        } catch (SocketException e)
        { System.out.println("Connection abandoned");}
    }
    
    public boolean add()
    {
        try
        {
            System.out.println("Add account");
            String name = sd.decrypt((byte[])inFromClient.readObject());
            String password = sd.decrypt((byte[])inFromClient.readObject());
            String bal = sd.decrypt((byte[])inFromClient.readObject());
            Double balance = Double.parseDouble(bal);
            String acc = sd.decrypt((byte[])inFromClient.readObject());
            Integer accType = Integer.parseInt(acc);
            
            if(accType == 2)
                bank.addAccount(new SavingsAccount(balance, name, password));
            else
                bank.addAccount(new CheckingAccount(balance, name, password));
            
            System.out.println("Name: " + name + "\tPassword: " + password + "\tBalance: " + balance);
            if (accType == 2) System.out.println("Savings Account");
            else System.out.println("Checking Account");
            
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
            return false;
        }
    }
    
    public boolean remove()
    {
        try
        {
            String name = sd.decrypt((byte[])inFromClient.readObject());
            
            bank.removeAccount(name);
            System.out.println("Account of " + name + " deleted.");
            
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
            return false;
        }
    }
    
    public boolean authenticate()
    {
        try
        {
            boolean validated = true;
            String name = sd.decrypt((byte[])inFromClient.readObject());
            String password = sd.decrypt((byte[])inFromClient.readObject());

            bank.authenticateAccount(name, password);
            System.out.println("Account authenticated");
            
            return validated;
        } catch (IOException e) {
            System.out.println("IOException");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
            return false;
        }
    }
    
    public boolean accountAction()
    {
        try
        {
            String name = sd.decrypt((byte[])inFromClient.readObject());
            String am = sd.decrypt((byte[])inFromClient.readObject());
            Double amount = Double.parseDouble(am);
            String password = sd.decrypt((byte[])inFromClient.readObject());
            
            if(amount < 0)
            {
                bank.withdraw(amount, name, password);
            }
            else
                bank.deposit(amount, name, password);
            
            System.out.println("Account action performed on " + name + "'s account");
            
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
        }
        return false;
    }
    
    public boolean interest()
    {
        try
        {
            System.out.println("Perform interest operation");
            
            ArrayList<byte[]> encryptedResults= new ArrayList<>();
            
            ArrayList<CompoundResult> cmp = bank.compoundAll();
            
            for(int i =0; i < bank.size(); i++)
            {
                encryptedResults.add(encryptor.Encrypt(
                        bank.getHolder(i) + "  " + cmp.get(i).toString()));
            }
                        
            outToClient.writeObject(encryptedResults);
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return false;
    }
    
    public boolean accountInit() throws IOException, ClassNotFoundException
    {
        // Get the accounts from the database
        ArrayList<String> accounts = bank.getLabels();
        ArrayList<byte[]> encryptedNames = new ArrayList<>();
        
        for(String name: accounts)
        {
            encryptedNames.add(encryptor.Encrypt(name));
        }
        
        System.out.println("Reset account list.");
        
        outToClient.writeObject(encryptedNames);
        return true;
    }
    
    public boolean getBalance()
    {
        try
        {
            Double balance = 0.0;
            String name = sd.decrypt((byte[])inFromClient.readObject());
            String password = sd.decrypt((byte[])inFromClient.readObject());
            
            balance = bank.getBalance(name, password);
            System.out.println("Getting account balance of " + name);
            
            outToClient.writeObject(encryptor.Encrypt(balance.toString()));
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
        }
        return false;
    }
    
    public boolean withdrawals()
    {
        try
        {
            // Should be in form of "Withdraw (#)" if checking or "Withdraw"
            String text = "Withdraw"; 
            String name = sd.decrypt((byte[])inFromClient.readObject());
            
            // Get balance
            int index = bank.getIndex(name);
            if (!bank.isChecking(index))
                text += " " + bank.remainingWithdrawals(index);
            System.out.println("Getting account withdrawals of " + name);
            
            outToClient.writeObject(encryptor.Encrypt(text));
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
        }
        return false;
    }
}
