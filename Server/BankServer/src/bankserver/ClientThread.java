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

/**
 *
 * @author Josh
 */
public class ClientThread extends Thread{
    
    Bank bank;
    Socket csocket;
    ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;
    
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
        
        outToClient.writeObject("Hello and welcome to the Bank Socket Server!");
        begin();
    }
    
    public boolean accountInit() throws IOException, ClassNotFoundException
    {
        // Get the accounts from the database
        ArrayList<String> accounts = bank.getLabels();
        
        System.out.println("Reset account list.");
        
        outToClient.writeObject(accounts);
        return true;
    }
    
    public void begin() throws IOException, ClassNotFoundException
    {
        try {
            while (!csocket.isClosed())
            {
                Integer todo;
                todo = (Integer)inFromClient.readObject();

                boolean complete;

                switch(todo)
                {
                    // Add a user
                    case 0: complete = add();
                        outToClient.writeObject(complete);
                        break;

                    // Remove a user
                    case 1: complete = remove();
                        outToClient.writeObject(complete);
                        break;

                    // Authenticate login
                    case 2: complete = authenticate();
                        outToClient.writeObject(complete);
                        break;

                    // Withdraw or deposit
                    case 3: complete = accountAction();
                        outToClient.writeObject(complete);
                        break;

                    // Interest
                    case 4: complete = interest();
                        outToClient.writeObject(complete);
                        break;

                    // Reset account list
                    case 5: complete = accountInit();
                        outToClient.writeObject(complete);
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
            String name = (String)inFromClient.readObject();
            String password = (String)inFromClient.readObject();
            Double balance = (Double)inFromClient.readObject();
            Integer accType = (Integer)inFromClient.readObject();
            
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
            String name = (String)inFromClient.readObject();
            
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
            String name = (String)inFromClient.readObject();
            String password = (String)inFromClient.readObject();

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
            String name = (String)inFromClient.readObject();
            Double amount = (Double)inFromClient.readObject();
            String password = (String) inFromClient.readObject();
            
            if(amount > 0)
            {
                bank.withdraw(amount, name, password);
            }
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
            
            ArrayList<CompoundResult> account = bank.compoundAll();
            
            outToClient.writeObject(account);
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return false;
    }
    
    public boolean getBalance()
    {
        try
        {
            Double balance = 0.0;
            String name = (String)inFromClient.readObject();
            String password = (String)inFromClient.readObject();
            
            bank.getBalance(name, password);
            System.out.println("Getting account balance of " + name);
            
            outToClient.writeObject(balance);
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
            String name = (String)inFromClient.readObject();
            
            // Get balance
            int index = bank.getIndex(name);
            if (bank.isChecking(index))
                text += " " + bank.remainingWithdrawals(index);
            System.out.println("Getting account withdrawals of " + name);
            
            outToClient.writeObject(text);
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfoundexception");
        }
        return false;
    }
}
