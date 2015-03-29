package banking;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import banking.Account.CompoundResult;

public class BankTest {

	String password = "correct";
	String notpassword = "incorrect";
	
	@Test
	public void addTest() {
		
		Bank test = new Bank();
		
		assertEquals(test.accounts.size(), 0);
		
		test.addAccount(new CheckingAccount(200, "Daryl", password));
		assertEquals(test.accounts.size(), 1);	
	}

	@Test
	public void removeTest() {
		
		Bank test = new Bank();
		test.accounts.add(new CheckingAccount(200, "Daryl", password));
		test.accounts.add(new CheckingAccount(300, "Daryl2", password));
		test.accounts.add(new CheckingAccount(400, "Daryl3", password));
		
		assertEquals(test.accounts.size(), 3);
		
		test.removeAccount("Daryl3");
		
		assertEquals(test.accounts.size(), 2);
		
		test.removeAccount("Daryl2");
		
		assertEquals(test.accounts.size(), 1);
		
		test.removeAccount("Daryl4");
		
		assertEquals(test.accounts.size(), 1);
	}
	
	@Test
	public void compoundTest() {
		Bank test = new Bank();
		test.accounts.add(new SavingsAccount(200, "Daryl", password));
		test.accounts.add(new SavingsAccount(0, "Daryl2", password));
		test.accounts.add(new SavingsAccount(-200, "Daryl3", password));
		
		assertEquals(test.compoundAccount("Daryl"),CompoundResult.RATE);
		
		ArrayList<CompoundResult> testList = test.compoundAll();
		ArrayList<CompoundResult> testList2 = new ArrayList<CompoundResult>();
		
		testList2.add(CompoundResult.RATE);
		testList2.add(CompoundResult.NONE);
		testList2.add(CompoundResult.PENALTY);
		
		for(int i =0; i < testList.size(); i++)
		{
			assertEquals(testList.get(i), testList2.get(i));
		}
	}
	
	@Test
	public void getBalanceTest() {
		Bank test = new Bank();
		test.accounts.add(new CheckingAccount(200, "Daryl", password));
		
		assertEquals(test.getBalance("Daryl", password ),200,0);
		
	}
	
	@Test
	public void withdrawTest() {
		Bank test = new Bank();
		test.accounts.add(new CheckingAccount(200, "Daryl", password));
		
		test.withdraw(200,"Daryl", password );
		
		assertEquals(test.accounts.get(0).balance,0, 0);
	}
	
	@Test
	public void depositTest() {
		Bank test = new Bank();
		test.accounts.add(new CheckingAccount(200, "Daryl", password));
		
		test.deposit(200,"Daryl", password );
		
		assertEquals(test.accounts.get(0).balance,400, 0);
	}
	
	@Test
	public void authenticateTest() {
		Bank test = new Bank();
		test.accounts.add(new CheckingAccount(200, "Daryl", password));
		
		assertTrue(test.authenticateAccount("Daryl", password));
		assertFalse(test.authenticateAccount("Daryl", notpassword));
		
	}
}
