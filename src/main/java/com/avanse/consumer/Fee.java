package com.avanse.consumer;

public class Fee {
	
private String feeCode;
private long feeAmount;
private long waiverAmount;
private long paidAmount;
private String feeMethod;
private int scheduleTerms;
private Payment payment;

public String getFeeCode() {
	return feeCode;
}
public void setFeeCode(String feeCode) {
	this.feeCode = feeCode;
}
public long getPaidAmount() {
	return paidAmount;
}
public void setPaidAmount(long paidAmount) {
	this.paidAmount = paidAmount;
}
public long getFeeAmount() {
	return feeAmount;
}
public void setFeeAmount(long feeAmount) {
	this.feeAmount = feeAmount;
}
public long getWaiverAmount() {
	return waiverAmount;
}
public void setWaiverAmount(long waiverAmount) {
	this.waiverAmount = waiverAmount;
}

}
