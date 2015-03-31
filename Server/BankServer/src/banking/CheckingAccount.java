package banking;

public class CheckingAccount extends Account {

	protected double minimumBalance = 50;
	
	public CheckingAccount(double balance, String holder, String password) {
		super(balance, holder, password);
		
		rate = .1;
	}
	
        @Override
	protected void withdraw(double amount, String password)
	{
		if(!authenticate(password))
			return;
		
		if(amount > 0)
			balance -= amount;
	}
	
    
        @Override
	protected CompoundResult compoundInterest()
	{
		if (balance == minimumBalance)
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
	
        @Override
	public String toString()
	{
		return holder + " *Checking*"; 
	}
}
