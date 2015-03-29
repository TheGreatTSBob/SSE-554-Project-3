import banking.*;

public class Bank_Main {

	public static void main(String[] args) {
		
		Bank bank = new Bank();
		bank.addAccount(new SavingsAccount(200, "Daryl", "pass"));
		
		BankGUI main = new BankGUI(bank);
		
		main.showHome();

	}

}
