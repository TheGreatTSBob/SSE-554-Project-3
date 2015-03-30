/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bankserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Josh
 */
public class BankServer {

    static int portNumber = 4444;
    
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Starting the socket server:");
            
            getClients(serverSocket);
        } 
        catch (IOException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void getClients(ServerSocket serverSocket)
    {
        System.out.println("Listening for clients...");
        
        while(true)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection successful.");
                ClientThread ct = new ClientThread(clientSocket);
                ct.start();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
