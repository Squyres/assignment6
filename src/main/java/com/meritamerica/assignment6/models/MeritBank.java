package com.meritamerica.assignment6.models;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import com.meritamerica.assignment6.exceptions.ExceedsAvailableBalanceException;
import com.meritamerica.assignment6.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment6.exceptions.ExceedsFraudSuspicionLimitException;

import java.io.BufferedReader;

class MeritBank {

	private static long nextAccountNumber;
	private static AccountHolder AccountHoldersArray[] = new AccountHolder[0];
	private static CDOffering CDOfferingsArray[] = new CDOffering[0];

	public static void addAccountHolder(AccountHolder accountHolder) {
		AccountHolder[] newAccountHolderArray = new AccountHolder[AccountHoldersArray.length + 1];
		for (int i = 0; i < newAccountHolderArray.length - 1; i++) {
			newAccountHolderArray[i] = AccountHoldersArray[i];
		}
		AccountHoldersArray = newAccountHolderArray;
		AccountHoldersArray[AccountHoldersArray.length - 1] = accountHolder;
	}

	public static AccountHolder[] getAccountHolders() {
		return AccountHoldersArray;
	}

	public static CDOffering[] getCDOfferings() {
		return CDOfferingsArray;
	}

	public static CDOffering getBestCDOffering(double depositAmount) {
		double best = 0.0;
		CDOffering bestOffering = null;
		if (CDOfferingsArray == null) {
			return null;
		}
		for (CDOffering offering : CDOfferingsArray) {
			if (futureValue(depositAmount, offering.getInterestRate(), offering.getTerm()) > best) {
				bestOffering = offering;
				best = futureValue(depositAmount, offering.getInterestRate(), offering.getTerm());
			}
		}
		return bestOffering;
	}

	public static void clearCDOfferings() {
		CDOfferingsArray = null;
	}

	public static void setCDOfferings(CDOffering[] offerings) {
		CDOfferingsArray = offerings;
	}

	public static long getNextAccountNumber() {
		return nextAccountNumber++;
	}

	public static double totalBalances() {
		double total = 0.0;
		for (AccountHolder accounts : AccountHoldersArray) {
			total += accounts.getCombinedBalance();
		}
		System.out.println("Total aggregate account balance: $" + total);
		return total;

	}

	public static double futureValue(double presentValue, double interestRate, int term) {
		interestRate += 1;
		return presentValue * recursiveFutureValue(presentValue, term, interestRate);
	}

	static AccountHolder[] sortAccountHolders() {
		Arrays.sort(AccountHoldersArray);
		for (AccountHolder a : AccountHoldersArray) {
			System.out.println(a);
		}
		return AccountHoldersArray;
	}

	static void setNextAccountNumber(long accountNumber) {
		nextAccountNumber = accountNumber;

	}

	public static double recursiveFutureValue(double amount, int years, double interestRate) {
		if (years != 0) {
			return (interestRate * recursiveFutureValue(amount, years - 1, interestRate));
		} else
			return 1;
	}

	public static BankAccount getBankAccount(long accountId) {
		for (int i = 0; i < AccountHoldersArray.length; i++) {
			for (int j = 0; i < AccountHoldersArray[i].checkingArray.length; j++) {
				if (AccountHoldersArray[i].checkingArray[j].accountNumber == accountId) {
					return AccountHoldersArray[i].checkingArray[j];
				}
			}
			for (int j = 0; i < AccountHoldersArray[i].savingsArray.length; j++) {
				if (AccountHoldersArray[i].savingsArray[j].accountNumber == accountId) {
					return AccountHoldersArray[i].savingsArray[j];
				}
			}
			for (int j = 0; i < AccountHoldersArray[i].cdAccountArray.length; j++) {
				if (AccountHoldersArray[i].cdAccountArray[j].accountNumber == accountId) {
					return AccountHoldersArray[i].cdAccountArray[j];
				}
			}
		}
		return null;
	}
}