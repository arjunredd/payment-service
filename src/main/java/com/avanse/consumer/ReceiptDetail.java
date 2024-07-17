package com.avanse.consumer;

public class ReceiptDetail {
	
private String paymentRef;
private String	transactionRef;
private String	fundingAccount;
private String	receivedDate;
private String valueDate;
private String bankCode;
private String favourNumber;
private String favourName;
private String chequeAcNo;
private String	remarks;

public String getPaymentRef() {
	return paymentRef;
}
public void setPaymentRef(String paymentRef) {
	this.paymentRef = paymentRef;
}
public String getTransactionRef() {
	return transactionRef;
}
public void setTransactionRef(String transactionRef) {
	this.transactionRef = transactionRef;
}
public String getFundingAccount() {
	return fundingAccount;
}
public void setFundingAccount(String fundingAccount) {
	this.fundingAccount = fundingAccount;
}
public String getReceivedDate() {
	return receivedDate;
}
public void setReceivedDate(String receivedDate) {
	this.receivedDate = receivedDate;
}
public String getRemarks() {
	return remarks;
}
public void setRemarks(String remarks) {
	this.remarks = remarks;
}
public String getValueDate() {
	return valueDate;
}
public void setValueDate(String valueDate) {
	this.valueDate = valueDate;
}
public String getBankCode() {
	return bankCode;
}
public void setBankCode(String bankCode) {
	this.bankCode = bankCode;
}
public String getFavourNumber() {
	return favourNumber;
}
public void setFavourNumber(String favourNumber) {
	this.favourNumber = favourNumber;
}
public String getFavourName() {
	return favourName;
}
public void setFavourName(String favourName) {
	this.favourName = favourName;
}
public String getChequeAcNo() {
	return chequeAcNo;
}
public void setChequeAcNo(String chequeAcNo) {
	this.chequeAcNo = chequeAcNo;
}

public ReceiptDetail(String paymentRef, String transactionRef, String fundingAccount, String receivedDate,
		String valueDate, String bankCode, String favourNumber, String favourName, String chequeAcNo, String remarks) {
	super();
	this.paymentRef = paymentRef;
	this.transactionRef = transactionRef;
	this.fundingAccount = fundingAccount;
	this.receivedDate = receivedDate;
	this.valueDate = valueDate;
	this.bankCode = bankCode;
	this.favourNumber = favourNumber;
	this.favourName = favourName;
	this.chequeAcNo = chequeAcNo;
	this.remarks = remarks;
}
public ReceiptDetail() {
	super();
	// TODO Auto-generated constructor stub
}

}
