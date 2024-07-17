package com.avanse.consumer;

public class FinanceResponse {
	private String finReference;
	private boolean stp;
	private ReturnStatus returnStatus;
	private int receiptId;
	private String appDate;
	private String valueDate;
	
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public boolean isStp() {
		return stp;
	}
	public void setStp(boolean stp) {
		this.stp = stp;
	}
	public ReturnStatus getReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(ReturnStatus returnStatus) {
		this.returnStatus = returnStatus;
	}
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	
	public String getFinReference() {
		return finReference;
	}
	public void setFinReference(String finReference) {
		this.finReference = finReference;
	}
	@Override
	public String toString() {
		return "FeesPaymentResponse [stp=" + stp + ", returnStatus=" + returnStatus + ", receiptId=" + receiptId
				+ ", isStp()=" + isStp() + ", getReturnStatus()=" + getReturnStatus() + ", getReceiptId()="
				+ getReceiptId() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
