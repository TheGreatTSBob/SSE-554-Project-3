package banking;


public abstract class Account {
	
	protected double balance;
	protected String holder;
	protected String password;
	protected double rate = .2;
	
	public enum CompoundResult{
		NONE,
		RATE,
		PENALTY
	};
	
	public Account(double balance, String holder, String password)
	{
		this.balance = balance;
		this.holder = holder;
		this.password = password;
	}
	
	protected void deposit(double amount, String password)
	{
		if(!authenticate(password))
			return;
		
		if(amount > 0)
			balance += amount;
	}
	
	protected abstract void withdraw(double amount, String password);
	
	protected Double getBalance(String password)
	{
		if(authenticate(password))
			return balance;

		return null;
	}

	protected Boolean isOverdrawn(String password)
	{
		if(authenticate(password))
			return balance < 0;
		
		return null;
	}
	
	protected abstract CompoundResult compoundInterest();
	
	
	protected Boolean authenticate(String password)
	{
		if(this.password.compareTo(password) == 0)
			return true;		
		return false;
	}
	
}
