/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import java.sql.*;
import java.util.ArrayList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author TSBob
 */
public class DatabaseTest {
    
    @Test(expected= MySQLSyntaxErrorException.class)
    @SuppressWarnings({"CallToPrintStackTrace"})
    public void Test() throws Exception
    {
       
        Database db = new Database("test");
        Connection conn = db.getConnection("test");

        //connection
        assertTrue(conn != null);

        Statement stat = conn.createStatement();

        //adding to database
        stat.executeUpdate("CREATE TABLE Test (Message CHAR(20))");
        stat.executeUpdate("INSERT INTO Test VALUES ('Hello, World')");


        try (ResultSet result = stat.executeQuery("SELECT * FROM Test"))
        {
            if(result.next())
                assertEquals(result.getString(1), "Hello, World");
        }

        //removing from database
        stat.executeUpdate("DROP TABLE Test");

        //should create an exception because no such table exists
        stat.executeQuery("SELECT * From Test");        
    }
    @Test
    public void TestWriteToDatabase() throws Exception
    {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new CheckingAccount(200,"DarylTest", "password"));
        
        Database test = new Database("test");
        Connection link = test.getConnection("test");
        Statement stat = link.createStatement();
        
        stat.executeUpdate("CREATE table Bank(" 
                            + "holder varchar(45)," 
                            + "password varchar(45),"
                            + "balance float,"
                            + "accounttype varchar(10)," 
                            + "rate float," 
                            + "withdrawals int," 
                            + "minbalance double);");
        
        test.writeToDatabase(accounts);
              
        ResultSet result = stat.executeQuery("SELECT * from Bank");
        result.next();
        assertEquals(result.getString("holder"), "DarylTest");
        assertEquals(result.getDouble("balance"), 200.0, 0);
        assertEquals(result.getString("password"), "password");
        
        stat.executeUpdate("DROP TABLE Bank");
    }
    
    @Test
    public void TestReadFromDatabase() throws Exception
    {
        ArrayList<Account> accounts = new ArrayList<>();
        Database test = new Database("test");
                
        Connection link = test.getConnection("test");
        Statement stat = link.createStatement();
        
        stat.executeUpdate("CREATE table Bank(" 
                            + "holder varchar(45)," 
                            + "password varchar(45),"
                            + "balance float,"
                            + "accounttype varchar(10)," 
                            + "rate float," 
                            + "withdrawals int," 
                            + "minbalance double);");
        
        stat.executeUpdate("INSERT INTO Bank "
                            + "(holder, balance, password, accounttype)"
                + " Value('DarylTest', 400, 'pass', 'Checking')");
        
        accounts = test.readFromDatabase(accounts);
        
        for(Account a : accounts)
        {
            assertEquals(a.holder, "DarylTest");
            assertEquals(a.balance, 400, 0);
            assertEquals(a.password, "pass");
            assertTrue(a.getClass()== CheckingAccount.class);
        }
        
        stat.executeUpdate("DROP TABLE Bank");
    }
}
