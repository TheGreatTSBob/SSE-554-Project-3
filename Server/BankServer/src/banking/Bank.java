package banking;

import java.util.ArrayList;

import banking.Account.CompoundResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank {
	
	protected ArrayList<Account> accounts;
        public Database database;
	
	public Bank()
	{
            accounts = new ArrayList<>();
            try {
                database = new Database();
                database.readFromDatabase(accounts);
                
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, 
                        ex);
            }
	}
	
	public void addAccount(Account acc)
	{
		accounts.add(acc); 
                
                /*************************************
                 * 
                 *  Consider adding a database refresh when an account is added
                 *  removed or changed
                 *
                 *
                 ***********************************/
                
                setData();
	}
	
	public void removeAccount(String holder)
	{
		int index = getIndex(holder);
		
		if( index < 0)
			return;
		
		accounts.remove(index);
                /*************************************
                 * 
                 *  Consider adding a database refresh when an account is added
                 *  removed or changed
                 *
                 *
                 ***********************************/
                
                setData();
	}
	
	public int getIndex(String holder)
	{
		for(Account a : accounts)
		{
			if(a.holder.compareTo(holder) == 0)
				return accounts.indexOf(a);
		}
		return -1;
	}
	
	public String getHolder(int index)
	{
		return accounts.get(index).holder;
	}
	
	public ArrayList<CompoundResult> compoundAll()
	{
		ArrayList<CompoundResult> value = new ArrayList<>();
		
		for (Account a : accounts)
		{
			value.add(a.compoundInterest());
		}
		
		return value;
                
	}
	
	public CompoundResult compoundAccount(String holder)
	{
		int index = getIndex(holder);
		
		CompoundResult res = accounts.get(index).compoundInterest();
                
                /*************************************
                 * 
                 *  Consider adding a database refresh when an account is added
                 *  removed or changed
                 *
                 *
                 ***********************************/
                
                setData();
                
                return res;
                
	}
	
	public Double getBalance(String holder, String password)
	{
		int index = getIndex(holder);
		
		return accounts.get(index).getBalance(password);
	}
	
	public Boolean authenticateAccount(String holder, String password)
	{
		int index = getIndex(holder);
		
		if (index < 0)
			return false;
		
		return accounts.get(index).authenticate(password);
	}
	
	public void withdraw(double amount, String holder, String password)
	{
		int index = getIndex(holder);
		
		accounts.get(index).withdraw(amount, password);
                
                /*************************************
                 * 
                 *  Consider adding a database refresh when an account is added
                 *  removed or changed
                 *
                 *
                 ***********************************/
                
                setData();
	}
	
	public void deposit(double amount, String holder, String password)
	{
		int index = getIndex(holder);
		
		accounts.get(index).deposit(amount, password);
                
                /*************************************
                 * 
                 *  Consider adding a database refresh when an account is added
                 *  removed or changed
                 *
                 *
                 ***********************************/
                
                setData();
	}
	
	public ArrayList<String> getLabels()
	{
		ArrayList<String> labels = new ArrayList<>();
		
		for(Account a : accounts)
		{
			labels.add(a.toString());
		}
		
		return labels;
	}
	
	public boolean isChecking(int index)
	{
		return accounts.get(index).getClass() == CheckingAccount.class;
	}
	
	public int remainingWithdrawals(int index)
	{
		return ((SavingsAccount) accounts.get(index)).maxWithdrawals - 
                      ((SavingsAccount) accounts.get(index)).currentWithdrawals;
	}
	
	public int size()
	{
		return accounts.size();
	}
        
        public void getData()
        {
            try {
                accounts = database.readFromDatabase(accounts);
            } catch (SQLException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, 
                        ex);
            }
        }
        
        public void setData()
        {
            try {
                database.writeToDatabase(accounts);
            } catch (SQLException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null,
                        ex);
                ex.printStackTrace();
            }
        }
}
