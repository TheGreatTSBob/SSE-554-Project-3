package banking;

import java.util.ArrayList;

import banking.Account.CompoundResult;

public class Bank {
	
	protected ArrayList<Account> accounts;
	
	public Bank()
	{
		accounts = new ArrayList<Account>();
	}
	
	public void addAccount(Account acc)
	{
		accounts.add(acc);
	}
	
	public void removeAccount(String holder)
	{
		int index = getIndex(holder);
		
		if( index < 0)
			return;
		
		accounts.remove(index);
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
		ArrayList<CompoundResult> value = new ArrayList<CompoundResult>();
		
		for (Account a : accounts)
		{
			value.add(a.compoundInterest());
		}
		
		return value;
	}
	
	public CompoundResult compoundAccount(String holder)
	{
		int index = getIndex(holder);
		
		return accounts.get(index).compoundInterest();
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
	}
	
	public void deposit(double amount, String holder, String password)
	{
		int index = getIndex(holder);
		
		accounts.get(index).deposit(amount, password);
	}
	
	public ArrayList<String> getLabels()
	{
		ArrayList<String> labels = new ArrayList<String>();
		
		for(Account a : accounts)
		{
			labels.add(a.toString());
		}
		
		return labels;
	}
	
	public boolean isChecking(int index)
	{
		if(accounts.get(index).getClass() == CheckingAccount.class)
			return true;
		
		return false;
	}
	
	public int remainingWithdrawals(int index)
	{
		return ((SavingsAccount) accounts.get(index)).maxWithdrawls - ((SavingsAccount) accounts.get(index)).currentWithdrawls;
	}
	
	public int size()
	{
		return accounts.size();
	}
}
