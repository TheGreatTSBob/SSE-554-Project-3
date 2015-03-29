package banking;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccountTest {

	String correctPW = "correct";
	String incorrectPW = "wrong";
	
	@Test
	public void testDeposit() {	
		CheckingAccount test = new CheckingAccount(100, "Daryl",correctPW);
		CheckingAccount test2 = new CheckingAccount(100, "Daryl",correctPW);
		
		
		test.deposit(300.00, incorrectPW);
		assertEquals(100, test.balance, 0);
		
		//tests increasing balance by 100
		test.deposit(100.00,correctPW);
		assertEquals("Failure - balance should be 200", 200,  test.balance, 0);
		
		//No change as deposits cannot be negative
		test2.deposit(-100.00,correctPW);
		assertEquals(100, test2.balance, 0);
	}
	
	@Test
	public void testWithdraw() {	
		CheckingAccount test = new CheckingAccount(100, "Daryl",correctPW);
		SavingsAccount test2 = new SavingsAccount(100, "Daryl",correctPW);
		
		//if the password is incorrect nothing is done
		test.withdraw(400, incorrectPW);
		assertEquals(100,  test.balance, 0);
		
		//tests reducing the balance by 100
		test.withdraw(100, correctPW);
		assertEquals(0,  test.balance, 0);
		
		//No change as withdrawals cannot be negative
		test2.withdraw(-100.00, correctPW);
		assertEquals(100, test2.balance, 0);
		
		//Penalty as max withdraws have been reached
		test2.currentWithdrawls = test2.maxWithdrawls;
		test2.withdraw(100, correctPW);
		assertEquals(90,test2.balance,0);
		
	}
	
	@Test
	public void getBalance() {	
		CheckingAccount test = new CheckingAccount(100, "Daryl",correctPW);
		
		//tests authentication pass and correct return value
		assertEquals(100, (double) test.getBalance(correctPW), 0);
		
		//tests authentication fail
		assertNull(test.getBalance(incorrectPW));
		
	}
	
	@Test
	public void testIsOverdrawn() {	
		CheckingAccount test = new CheckingAccount(0, "Daryl",correctPW);
		
		test.balance-=10;
		assertTrue(test.isOverdrawn(correctPW));
	}
	
	@Test
	public void testCompoundInterest() {	
		Account test = new CheckingAccount(0, "Daryl",correctPW);
		Account test2 = new CheckingAccount(100, "Daryl",correctPW);
		Account test3 = new CheckingAccount( 40, "Daryl",correctPW);
		Account test4 = new CheckingAccount(-100, "Daryl",correctPW);
		
		assertTrue(test.compoundInterest() == Account.CompoundResult.NONE);
		assertEquals(test.balance, 0, 0);
				
		assertTrue(test2.compoundInterest() == Account.CompoundResult.RATE);
		assertEquals(test2.balance, 110, 0);
		
		assertTrue(test3.compoundInterest() == Account.CompoundResult.PENALTY);
		assertEquals(test3.balance, 10, 0);
		
		assertTrue(test4.compoundInterest() == Account.CompoundResult.PENALTY);
		assertEquals(test4.balance, -130, 0);
		
	}

}
