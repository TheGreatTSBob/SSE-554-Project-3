import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import banking.*;
import banking.Account.CompoundResult;

public class BankGUI {
	
	HomeGUI home;
	AddGUI add;
	AuthGUI auth;
	UseGUI use;
	CmpGUI cmp;
	Bank bank;

	public BankGUI(Bank bank)
	{
		home = new HomeGUI();
		add = new AddGUI();
		auth = new AuthGUI();
		use = new UseGUI();
		cmp = new CmpGUI();
		this.bank = bank;
	}
	
	
	public void showHome()
	{
		home.show();
	}
	
	private class HomeGUI
	{
		JFrame frame;
		JList<String> accountList;
		DefaultListModel<String> listModel;
		
		private void show()
		{
			frame = new JFrame();
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			listModel = new DefaultListModel<String>();
			
			JButton addButton = new JButton("ADD");
			JButton removeButton = new JButton("REMOVE");
			JButton useButton = new JButton("USE");
			JButton cmpButton = new JButton("INTEREST");
			
			JPanel buttonPanel = new JPanel();
			JPanel centerPanel = new JPanel();
			
			frame.setTitle("Bank 1.0");
			accountList = new JList<String>(listModel);
			
			ImageIcon addIcon = new ImageIcon(getClass().getResource("/images/client add.png"));
			ImageIcon removeIcon = new ImageIcon(getClass().getResource("/images/client delete.png"));
			ImageIcon useIcon = new ImageIcon(getClass().getResource("/images/client edit.png"));
			ImageIcon cmpIcon = new ImageIcon(getClass().getResource("/images/payment.png"));
			
			addButton.setIcon(addIcon);
			addButton.addActionListener(new AddListener());
			removeButton.setIcon(removeIcon);
			removeButton.addActionListener(new RemoveListener());
			useButton.setIcon(useIcon);
			useButton.addActionListener(new UseListener());
			cmpButton.setIcon(cmpIcon);
			cmpButton.addActionListener(new CmpListener());
			
			buttonPanel.add(addButton);
			buttonPanel.add(removeButton);
			buttonPanel.add(useButton);
			buttonPanel.add(cmpButton);
			
			JScrollPane scroller = new JScrollPane(accountList);
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			centerPanel.add(scroller);
			frame.getContentPane().add(BorderLayout.NORTH, buttonPanel);
			frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
			frame.setSize(500, 360);
			frame.setResizable(false);
			frame.setVisible(true);
			updateList();
			
		}
		public void updateList()
		{
			listModel.clear();
			ArrayList<String> labels = bank.getLabels();
			
			for(String s: labels)
			{
				listModel.addElement(s.trim());
			}
		}
		
		class AddListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				add.show();	
			}
		}
		
		class RemoveListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String text = home.accountList.getSelectedValue();
				String holder = text.substring(0, text.indexOf('*')).trim();
				
				bank.removeAccount(holder);
				home.updateList();
			}
			
		}
		class UseListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				auth.show();
				
			}
			
		}
		class CmpListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cmp.show();				
			}
		}
	}
	
	private class AuthGUI
	{
		JTextField nameField, passField;
		JDialog frame;
		
		private void show()
		{
			JPanel centerPanel = new JPanel();
			
			
			frame = new JDialog();
			frame.setTitle("Login to Account");;
			
			JPanel buttonPanel = new JPanel();
			
			nameField = new JTextField(18);
			passField = new JTextField(18);
			JButton loginButton = new JButton("Login");
			loginButton.addActionListener(new LoginListener());
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new CancelListener());
			
			centerPanel.add(new JLabel("Login: "));
			centerPanel.add(nameField);
			centerPanel.add(new JLabel("Password: "));
			centerPanel.add(passField);
			buttonPanel.add(loginButton);
			buttonPanel.add(cancelButton);
			
			frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
			frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
			frame.setModal(true);
			frame.setSize(300, 130);
			frame.setResizable(false);
			frame.setVisible(true);
		}
		
		class LoginListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
							
				if(bank.authenticateAccount(nameField.getText(), passField.getText()))
				{
					auth.frame.dispose();
					use.show(bank.getBalance(nameField.getText(), passField.getText()),
							nameField.getText(), passField.getText());
				}
			}
		}
		class CancelListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				auth.frame.dispose();
			}
		}
	}
	
	private class UseGUI
	{
		
		JTextField deltaField, balField;
		JButton witButton;
		JDialog frame;
		String password;
		String holder;
		
		private void show(double balance, String holder, String password)
		{
			JPanel centerPanel = new JPanel();
			this.password = password;
			this.holder =  holder;
			
			frame = new JDialog();
			frame.setTitle("What would you like to do?");
			
			JPanel buttonPanel = new JPanel();
			
			deltaField = new JTextField(15);
			balField  = new JTextField(10);
			witButton = new JButton("Withdraw");
			witButton.addActionListener(new witListener());
			JButton depButton = new JButton("Deposit");
			depButton.addActionListener(new depListener());
			
			buttonPanel.add(witButton);
			buttonPanel.add(depButton);
			centerPanel.add(deltaField);
			centerPanel.add(new JLabel("Current Balance"));
			centerPanel.add(balField);
			
			balField.setText("" + balance);
			
			int index = bank.getIndex(holder);
			
			if(!bank.isChecking(index))
			{
				witButton.setText("Withdraw ("+ bank.remainingWithdrawals(index) + ")");
			}
			
			frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
			frame.getContentPane().add(BorderLayout.NORTH, buttonPanel);
			frame.setModal(true);
			frame.setSize(250, 130);
			frame.setResizable(false);
			frame.setVisible(true);
		}
		
		class witListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				double amount = Double.parseDouble(deltaField.getText());
				
				bank.withdraw(amount, holder, password);
				balField.setText("" + bank.getBalance(holder, password));
				
				int index = bank.getIndex(holder);
				
				if(!bank.isChecking(index))
				{
					witButton.setText("Withdraw ("+ bank.remainingWithdrawals(index) + ")");
				}
			}
		}
		
		class depListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				double amount = Double.parseDouble(deltaField.getText());
				
				bank.deposit(amount, holder, password);
				balField.setText("" + bank.getBalance(holder, password));
			}	
		}
	}
	
	private class CmpGUI
	{
		JDialog frame;
		JList<String> compoundList;
		DefaultListModel<String> listModel;
		
		private void show()
		{
			frame = new JDialog();
			frame.setTitle("Interest Report");
			listModel = new DefaultListModel<String>();
			
			JPanel centerPanel = new JPanel();
			
			compoundList = new JList<String>(listModel);
			
			JScrollPane scroller = new JScrollPane(compoundList);
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			centerPanel.add(scroller);
			frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
			frame.setSize(300, 200);
			frame.setResizable(false);
			frame.setVisible(true);
		
			ArrayList<CompoundResult> result = bank.compoundAll();
			
			for(int i =0; i < bank.size(); i++)
			{
				listModel.addElement(bank.getHolder(i) + "   " + result.get(i));
			}
		}
	}
	
	private class AddGUI
	{

		JTextField nameField, passField, balField;
		JDialog frame;
		JRadioButton check;
		JRadioButton save;
		ButtonGroup group;
		
		private void show()
		{
			JPanel centerPanel = new JPanel();
			
			
			frame = new JDialog();
			frame.setTitle("Add Account");
			
			JPanel buttonPanel = new JPanel();
			
			nameField = new JTextField(18);
			passField = new JTextField(18);
			balField = new JTextField(18);
			JButton saveButton = new JButton("Save");
			saveButton.addActionListener(new SaveListener());
			JButton cancelButton = new JButton("Cancel");
			
			check = new JRadioButton();
			check.addActionListener(new RadioListener());
			save = new JRadioButton();
			save.addActionListener(new RadioListener());
			
			group = new ButtonGroup();
			
			group.add(check);
			group.add(save);
			
			check.setSelected(true);
			
			centerPanel.add(new JLabel("Name: "));
			centerPanel.add(nameField);
			centerPanel.add(new JLabel("Password: "));
			centerPanel.add(passField);
			centerPanel.add(new JLabel("Balance: "));
			centerPanel.add(balField);
			centerPanel.add(new JLabel("Checking: "));
			centerPanel.add(check);
			centerPanel.add(new JLabel("Savings: "));
			centerPanel.add(save);
			buttonPanel.add(saveButton);
			buttonPanel.add(cancelButton);
			
			cancelButton.addActionListener(new CancelListener());
			
			frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
			frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
			frame.setModal(true);
			frame.setSize(300, 180);
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
	
	class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
						
			String holder = add.nameField.getText();
			String pass = add.passField.getText();
			Double bal = Double.parseDouble(add.balField.getText());
			
			if(add.check.isSelected())
			{
				CheckingAccount acc = new CheckingAccount(bal, holder, pass);
				bank.addAccount(acc);
			}
			else
			{
				SavingsAccount acc = new SavingsAccount(bal, holder, pass);
				bank.addAccount(acc);
			}
			
			home.updateList();
			add.frame.dispose();
		}
	}
	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			add.frame.dispose();
		}
		
	}
	
	class RadioListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
}
