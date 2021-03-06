package com.meritamerica.assignment6.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment6.exceptions.AccountHolderIdNotFoundException;
import com.meritamerica.assignment6.exceptions.AccountHolderNotFoundException;
import com.meritamerica.assignment6.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment6.models.AccountHolder;
import com.meritamerica.assignment6.models.AccountHolderContactDetails;
import com.meritamerica.assignment6.models.CDAccount;
import com.meritamerica.assignment6.models.CDOffering;
import com.meritamerica.assignment6.models.CheckingAccount;
import com.meritamerica.assignment6.models.SavingsAccount;
import com.meritamerica.assignment6.repositories.AccountHolderContactDetailsRepository;
import com.meritamerica.assignment6.repositories.AccountHolderRepository;
import com.meritamerica.assignment6.repositories.CDAccountRepository;
import com.meritamerica.assignment6.repositories.CDOfferingRepository;
import com.meritamerica.assignment6.repositories.CheckingAccountRepository;
import com.meritamerica.assignment6.repositories.SavingsAccountRepository;

@RestController
public class MeritBankController {

	@Autowired
	private AccountHolderRepository accountHolderRepository;
	@Autowired
	private AccountHolderContactDetailsRepository accountHolderContactDetailsRepository;
	@Autowired
	private CDAccountRepository cdAccountRepository;
	@Autowired
	private CDOfferingRepository cdOfferingRepository;
	@Autowired
	private CheckingAccountRepository checkingAccountRepository;
	@Autowired
	private SavingsAccountRepository savingsAccountRepository;

	@PostMapping("/AccountHolders")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccountHolder(@RequestBody @Valid AccountHolder newAct) {
		accountHolderRepository.save(newAct);
		return newAct;
	}

	@PostMapping("/AccountHolders/{id}/CDAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CDAccount addCDAccount(@PathVariable int id, @RequestBody @Valid CDAccount newAct)
			throws ExceedsCombinedBalanceLimitException, AccountHolderIdNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		act.addCDAccount(newAct);
		cdAccountRepository.save(newAct);
		return newAct;
	}

	@PostMapping("/CDOfferings")
	@ResponseStatus(HttpStatus.CREATED)
	public CDOffering addCDOffering(@RequestBody @Valid CDOffering newOffer) {
		cdOfferingRepository.save(newOffer);
		return newOffer;

	}

	@PostMapping("/AccountHolders/{id}/CheckingAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CheckingAccount addCheckingAccount(@PathVariable int id, @RequestBody @Valid CheckingAccount newAct)
			throws ExceedsCombinedBalanceLimitException, AccountHolderIdNotFoundException,
			AccountHolderNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		if (newAct.getBalance() + act.getCombinedBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException();
		}
		act.addCheckingAccount(newAct);
		checkingAccountRepository.save(newAct);
		return newAct;
	}

	@PostMapping("/AccountHolders/{id}/ContactDetails")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolderContactDetails addContactDetails(@PathVariable int id,
			@RequestBody AccountHolderContactDetails contactDetails) {
		AccountHolder act = accountHolderRepository.findById(id);
		act.setAccountHolderContactDetails(contactDetails);
		accountHolderContactDetailsRepository.save(contactDetails);
		return contactDetails;
	}

	@PostMapping("/AccountHolders/{id}/SavingsAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public SavingsAccount addSavingsAccount(@PathVariable int id, @RequestBody @Valid SavingsAccount newAct)
			throws ExceedsCombinedBalanceLimitException, AccountHolderIdNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		if (newAct.getBalance() + act.getCombinedBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException();
		}
		act.addSavingsAccount(newAct);
		savingsAccountRepository.save(newAct);
		return newAct;
	}

	@GetMapping("/AccountHolders/{id}")
	public AccountHolder getAccountHolderByID(@PathVariable int id) throws AccountHolderIdNotFoundException {
		return accountHolderRepository.findById(id);
	}

	@GetMapping("/AccountHolders")
	public List<AccountHolder> getAccountHolders() {
		return accountHolderRepository.findAll();
	}

	@GetMapping("/AccountHolders/{id}/CDAccounts")
	public List<CDAccount> getCDAccountsByID(@PathVariable int id) throws AccountHolderIdNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		return cdAccountRepository.findByAccountHolder(act.getId());
	}

	@GetMapping("/CDOfferings")
	List<CDOffering> getCDOfferings() {
		return cdOfferingRepository.findAll();
	}

	@GetMapping("/AccountHolders/{id}/CheckingAccounts")
	public List<CheckingAccount> getCheckingAccountsByID(@PathVariable int id) throws AccountHolderIdNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		if (act == null) {
			throw new AccountHolderIdNotFoundException(id);
		}
		return checkingAccountRepository.findByAccountHolder(act.getId());
	}

	@GetMapping("/AccountHolders/{id}/ContactDetails")
	public AccountHolderContactDetails getContactDetails(@PathVariable int id) throws AccountHolderIdNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		if (act == null) {
			throw new AccountHolderIdNotFoundException(id);
		}
		return accountHolderContactDetailsRepository.findByAccountHolder(act.getId());
	}

	@GetMapping("/AccountHolders/{id}/SavingsAccounts")
	public List<SavingsAccount> getSavingsAccountsByID(@PathVariable int id) throws AccountHolderIdNotFoundException {
		AccountHolder act = accountHolderRepository.findById(id);
		if (act == null) {
			throw new AccountHolderIdNotFoundException(id);
		}
		return savingsAccountRepository.findByAccountHolder(act.getId());
	}

}
