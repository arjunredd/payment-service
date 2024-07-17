package com.avanse.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TrnFeeDetails")
public class TrnFeeDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long feeId;
	private int feeTransactionId;
	private String feeCode;
	private double feeAmount;
	@Column(name = "PaymentRequestID")
	private long paymentRequestId;

	public long getFeeId() {
		return feeId;
	}

	public void setFeeId(long feeId) {
		this.feeId = feeId;
	}

	public int getFeeTransactionId() {
		return feeTransactionId;
	}

	public void setFeeTransactionId(int feeTransactionId) {
		this.feeTransactionId = feeTransactionId;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

}
