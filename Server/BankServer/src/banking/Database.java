/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author TSBob (Daryl Ebanks)
 */
public class Database {
    
    Connection link;
    
    public Database() throws SQLException, IOException
    {
        link = getConnection();
    }
    
    protected final Connection getConnection() throws SQLException, IOException
    {
     
       String url = "jdbc:mysql://localhost:3306/sse554";
       String username = "root";
       String password = "password";
       
       return DriverManager.getConnection(url, username, password);
    }
    
    public void writeToDatabase(ArrayList<Account> accounts) throws SQLException
    {
        /*
        Do some cool stuff here
        
        Remove old table
        Create new table with account's data
        */
        
        Statement stat = link.createStatement();
        stat.executeUpdate("DROP TABLE Bank");
        stat.executeUpdate("CREATE table Bank(" 
                            + "holder varchar(45)," 
                            + "password varchar(45),"
                            + "balance float,"
                            + "accounttype varchar(10)," 
                            + "rate float," 
                            + "withdrawals int," 
                            + "minbalance double);");
        
        for(Account a : accounts)
        {
            if(CheckingAccount.class == a.getClass())
            {
                stat.executeUpdate("INSERT INTO Bank (" 
                               + "holder, password, balance, accounttype, rate,"
                               + "minbalance) "
                               + "VALUES ("
                               + "'" + a.holder + "'" + "," 
                               + "'" + a.password + "'" + ","
                               + a.balance + ","
                               + "'Checking', " 
                               + a.rate + ", "
                               + ((CheckingAccount) a).minimumBalance + ");" );
            }
            else
            {
                stat.executeUpdate("INSERT INTO Bank (" 
                               + "holder, password, balance, accounttype, rate,"
                               + "withdrawals)"
                               + "VALUES ("
                               + "'" + a.holder + "'" + "," 
                               + "'" + a.password + "'" + ","
                               + a.balance + ","
                               + "'Savings', " 
                               + a.rate + ", "
                               + ((SavingsAccount) a).currentWithdrawals 
                               + ");");
            }
        }    
    }
    
    public ArrayList<Account> readFromDatabase(ArrayList<Account> accounts) 
            throws SQLException
    {        
        /*
        Do some cool stuff here too
        
        Select * from bank database
        Add data for each account as an insert command
        */
        Statement stat = link.createStatement();
        ResultSet res = stat.executeQuery("SELECT * from Bank");
        
        accounts.clear();
        while(res.next())
        {
            String holder = res.getString("holder");
            String password = res.getString("password");  
            String type = res.getString("accounttype");
            Double balance = res.getDouble("balance");
            Double rate = res.getDouble("rate");
            if(type.equals("Checking"))
            {
                Double minbalance = res.getDouble("minbalance");
            
                CheckingAccount a = new CheckingAccount(balance, holder, 
                        password);
                a.minimumBalance = minbalance;
                a.rate = rate;
                accounts.add(a);
                System.out.println(a.toString());
            }
            else
            {
                int withdrawals = res.getInt("minbalance");
                SavingsAccount a = new SavingsAccount(balance, holder, 
                        password);
                a.currentWithdrawals = withdrawals;
                a.rate = rate;
                accounts.add(a);
                System.out.println(a.toString());
            }
        }
        return accounts;
    }
}
