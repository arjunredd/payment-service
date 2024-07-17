package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "TrnPaymentTransaction")
public class TrnPaymentTransaction{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PaymentTransactionID")
	private int paymentTransactionId;
	
	@Column(name = "razorpay_order_id")
	private String razorPayOrderId;
	
	@Column(name = "razorpay_payment_id")
	private String razorPayPaymentId;
	
	@Column(name = "razorpay_signature")
	private String razorPaySignature;
	/*
	 * @Column(name="TransactionStatus") private String transactionStatus;
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "razorpay_order_Datetime")
	private Date razorPayOrderDateTime=new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "razorpay_transaction_Datetime")
	private Date razorPayTransactionDateTime=new Date();
	
	@OneToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "PaymentRequestID",nullable=false)
	@JsonBackReference
	private TrnPaymentRequest paymentRequest;
	
	@Column(name="auth_code")
	private String authCode;

	public TrnPaymentTransaction() {
	
	}

	public TrnPaymentTransaction(String razorPayOrderId, String razorPayPaymentId, String razorPaySignature,
			Date razorPayOrderDateTime, Date razorPayTransactionDateTime) {
		super();
		this.razorPayOrderId = razorPayOrderId;
		this.razorPayPaymentId = razorPayPaymentId;
		this.razorPaySignature = razorPaySignature;
		this.razorPayOrderDateTime = razorPayOrderDateTime;
		this.razorPayTransactionDateTime = razorPayTransactionDateTime;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public int getPaymentTransactionId() {
		return paymentTransactionId;
	}

	public void setPaymentTransactionId(int paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	public String getRazorPayOrderId() {
		return razorPayOrderId;
	}

	public void setRazorPayOrderId(String razorPayOrderId) {
		this.razorPayOrderId = razorPayOrderId;
	}

	public String getRazorPayPaymentId() {
		return razorPayPaymentId;
	}

	public void setRazorPayPaymentId(String razorPayPaymentId) {
		this.razorPayPaymentId = razorPayPaymentId;
	}

	public String getRazorPaySignature() {
		return razorPaySignature;
	}

	public void setRazorPaySignature(String razorPaySignature) {
		this.razorPaySignature = razorPaySignature;
	}

	/*
	 * public String getTransactionStatus() { return transactionStatus; } public
	 * void setTransactionStatus(String transactionStatus) { this.transactionStatus
	 * = transactionStatus; }
	 */
	public Date getRazorPayOrderDateTime() {
		return razorPayOrderDateTime;
	}

	public void setRazorPayOrderDateTime(Date razorPayOrderDateTime) {
		this.razorPayOrderDateTime = razorPayOrderDateTime;
	}

	public Date getRazorPayTransactionDateTime() {
		return razorPayTransactionDateTime;
	}

	public void setRazorPayTransactionDateTime(Date razorPayTransactionDateTime) {
		this.razorPayTransactionDateTime = razorPayTransactionDateTime;
	}

	public TrnPaymentRequest getPaymentRequest() {
		return paymentRequest;
	}

	public void setPaymentRequest(TrnPaymentRequest paymentRequest) {
		//paymentRequest.setPaymentRequestId(this.PaymentRequestId);
		this.paymentRequest = paymentRequest;
	}

}
