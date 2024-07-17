package com.avanse.jpa.model;

import java.util.List;

public class ApplicantDetailsResponse {
	private List<AccountHolderDetails> accountHolderDetailsList;
	private double amount;
	private String loanType;

	public List<AccountHolderDetails> getAccountHolderDetailsList() {
		return accountHolderDetailsList;
	}

	public void setAccountHolderDetailsList(List<AccountHolderDetails> accountHolderDetailsList) {
		this.accountHolderDetailsList = accountHolderDetailsList;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

}
