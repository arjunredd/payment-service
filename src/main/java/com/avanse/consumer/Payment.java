package com.avanse.consumer;

import java.util.Date;

public class Payment {

	private int paymentID;
	private String reference;
	private String type;
	private long amount;
	private Date date;
	private String instrumentReference;
	private String favourof;
	private String bankCode;
	private String remarks;
	private String addlDetail1;
	private String addlDetail2;
	private String addlDetail3;
	private String addlDetail4;
	public int getPaymentID() {
		return paymentID;
	}
	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getInstrumentReference() {
		return instrumentReference;
	}
	public void setInstrumentReference(String instrumentReference) {
		this.instrumentReference = instrumentReference;
	}
	public String getFavourof() {
		return favourof;
	}
	public void setFavourof(String favourof) {
		this.favourof = favourof;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAddlDetail1() {
		return addlDetail1;
	}
	public void setAddlDetail1(String addlDetail1) {
		this.addlDetail1 = addlDetail1;
	}
	public String getAddlDetail2() {
		return addlDetail2;
	}
	public void setAddlDetail2(String addlDetail2) {
		this.addlDetail2 = addlDetail2;
	}
	public String getAddlDetail3() {
		return addlDetail3;
	}
	public void setAddlDetail3(String addlDetail3) {
		this.addlDetail3 = addlDetail3;
	}
	public String getAddlDetail4() {
		return addlDetail4;
	}
	public void setAddlDetail4(String addlDetail4) {
		this.addlDetail4 = addlDetail4;
	}
	
	
}