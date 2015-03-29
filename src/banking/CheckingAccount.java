package banking;

public class CheckingAccount extends Account {

	protected int minimumBalance = 50;
	
	public CheckingAccount(double balance, String holder, String password) {
		super(balance, holder, password);
		
		rate = .1;
	}
	
	protected void withdraw(double amount, String password)
	{
		if(!authenticate(password))
			return;
		
		if(amount > 0)
			balance -= amount;
	}
	
	protected CompoundResult compoundInterest()
	{
		if (balance == 0)
			return CompoundResult.NONE;	
		
		if(balance > minimumBalance)
		{
			balance += balance * rate;
			return CompoundResult.RATE;
		}
		else
		{
			//apply 30 dollar fee
			balance -= 30;
			return CompoundResult.PENALTY;
		}
	}
	
	public String toString()
	{
		return holder + " *Checking*"; 
	}
}
