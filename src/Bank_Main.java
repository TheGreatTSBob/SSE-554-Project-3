import banking.*;
import java.io.IOException;
import java.sql.SQLException;

public class Bank_Main {

	public static void main(String[] args) {
		
            Bank bank = new Bank("sse554");
		
            BankClient socket = new BankClient();
            socket.connect();
                
            BankGUI main = new BankGUI(bank, socket);
		
            main.showHome();
	}

}
