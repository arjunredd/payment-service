package com.avanse.jpa.model;

import java.util.Date;

public class PaymentStatusRequest {

	private String laNumber;
	private int paymentRequestId;
	private Date startDate;
	private Date endDate;

	public String getLaNumber() {
		return laNumber;
	}

	public void setLaNumber(String laNumber) {
		this.laNumber = laNumber;
	}

	public int getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(int paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "PaymentStatusRequest [laNumber=" + laNumber + ", paymentRequestId=" + paymentRequestId + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}

}
