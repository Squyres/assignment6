package com.meritamerica.assignment5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CDAccount")
public class SavingsAccount extends BankAccount {

	public static final double INTEREST_RATE = 0.01;

	@ManyToOne
	@JoinColumn(name = "accountholder_id", nullable = false)
	private AccountHolder ah;

	public SavingsAccount() {
		this.balance = 0;
		this.interestRate = INTEREST_RATE;
	}

	public SavingsAccount(double openBalance, double interestRate) {
		super(openBalance, interestRate);
	}
	public void addAccountHolder(AccountHolder ah){
		this.ah = ah;
	}
	public SavingsAccount(long accountNumber, double openBalance, double interestRate, Date accountOpenedOn) {
		super(accountNumber, openBalance, interestRate, accountOpenedOn);
	}

	public String toString() {
		return "Savings Account Balance: $" + balance + "\n" + "Savings Account Interest Rate: " + INTEREST_RATE + "\n"
				+ "Savings Account Balance in 3 years: $" + futureValue(3);

	}

	public static SavingsAccount readFromString(String accountData) throws ParseException, NumberFormatException {
		String[] holding = accountData.split(",");
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		long accountNumber = Long.parseLong(holding[0]);
		double balance = Double.parseDouble(holding[1]);
		double interestRate = Double.parseDouble(holding[2]);
		Date accountOpenedOn = date.parse(holding[3]);

		return new SavingsAccount(accountNumber, balance, interestRate, accountOpenedOn);
	}
}
