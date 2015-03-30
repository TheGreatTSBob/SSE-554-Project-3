/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import banking.Account;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author TSBob (Daryl Ebanks)
 */
public class Database {
    
    
    protected Connection getConnection() throws SQLException, IOException
    {
     
       String url = "jdbc:mysql://localhost:3306/sse554";
       String username = "root";
       String password = "password";
       
       return DriverManager.getConnection(url, username, password);
    }
    
    public void writeToDatabase(ArrayList<Account> accounts)
    {
        /*
        Do some cool stuff here
        
        Remove old table
        Create new table with account's data
        
        */
    }
    
    public ArrayList<Account> readFromDatabase()
    {
        ArrayList<Account> accounts = new ArrayList<>();
        
        /*
        Do some cool stuff here too
        
        Select * from bank database
        Add data for each account as an insert command
        */
        
        return accounts;
    }
}
