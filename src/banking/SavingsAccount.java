package banking;

public class SavingsAccount extends Account {

	protected int maxWithdrawls = 5, currentWithdrawls = 0;
	
	public SavingsAccount(double balance, String holder, String password) {
		super(balance, holder, password);
		
		rate = 0.05;
	}
	
	
	protected void withdraw(double amount, String password)
	{
		
		if(!authenticate(password))
			return;
		
		currentWithdrawls++;
		
		if(currentWithdrawls >= maxWithdrawls)
		{
			//apply ten dollar fee
			balance-=10;
			return;
		}

		if(amount > 0)
			balance -= amount;
	}
	
	protected CompoundResult compoundInterest()
	{
		
		if(balance >0)
		{
			balance += balance * rate;
			return CompoundResult.RATE;
		}
		
		if(balance < 0)
		{
			//apply 30 dollar fee
			balance -= 30;
			return CompoundResult.PENALTY;
		}
		
		this.currentWithdrawls = 0;
		
		return CompoundResult.NONE;
	}
	
	public String toString()
	{
		return holder + " *Savings*"; 
	}
}
