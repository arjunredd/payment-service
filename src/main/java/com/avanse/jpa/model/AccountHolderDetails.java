package com.avanse.jpa.model;

public class AccountHolderDetails {
	private String name;
	private String mobileNumber;
	private String emailId;
	private String accountName;
	private String address;
	private String loanBranch;
	private String loanType;

	public AccountHolderDetails() {

	}

	public AccountHolderDetails(String name, String mobileNumber, String emailId, String accountName) {
		super();
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.accountName = accountName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLoanBranch() {
		return loanBranch;
	}

	public void setLoanBranch(String loanBranch) {
		this.loanBranch = loanBranch;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	@Override
	public String toString() {
		return "AccountHolderDetails [name=" + name + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", accountName=" + accountName + "]";
	}

}
