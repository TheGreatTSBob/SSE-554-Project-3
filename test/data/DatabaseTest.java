/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import java.sql.*;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author TSBob
 */
public class DatabaseTest {
    
    @Test(expected= MySQLSyntaxErrorException.class)
    @SuppressWarnings({"CallToPrintStackTrace"})
    public void TestConnection() throws Exception
    {
       
        Database db = new Database();
        Connection conn = db.getConnection();

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
    
    
    //Needs tests for the methods that change/ read the database also
}
