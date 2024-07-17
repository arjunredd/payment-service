package com.avanse.jpa.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TrnPayment")
public class TrnPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TrnPaymentId")
	private long trnPaymentId;

	@Column(name = "UniqueReferenceNumber")
	private String uniqueReferenceNumber;

	@Column(name = "PaymentRequestId")
	private long paymentRequestId;

	@Column(name = "UserId")
	private String userId;

	@Column(name = "LanNumber")
	private String lanNumber;

	@NotNull(message = "Source ID is mandatory")
	@Type(type = "uuid-char")
	@Column(name = "SourceId", updatable = false, nullable = false, columnDefinition = "uniqueidentifier")
	private UUID sourceId;

	@Column(name = "RefTransaction")
	private String refTransaction;

	@Column(name = "PaymentRef")
	private String paymentRef;

	@Column(name = "Mode")
	private String mode;

	@Column(name = "TotalAmount")
	private double totalAmount;

	@Column(name = "BigLink")
	private String bigLink;

	@Column(name = "ShortLink")
	private String shortLink;

	@Column(name = "PaymentPurpose")
	private String paymentPurpose;

	@Column(name = "EmailId")
	private String emailId;

	@Column(name = "MobileNumber")
	private String mobileNumber;

	@Column(name = "CustomerName")
	private String customerName;

	@Column(name = "Address")
	private String address;

	@Column(name = "LoanBranch")
	private String loanBranch;

	public String getUniqueReferenceNumber() {
		return uniqueReferenceNumber;
	}

	public void setUniqueReferenceNumber(String uniqueReferenceNumber) {
		this.uniqueReferenceNumber = uniqueReferenceNumber;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanNumber() {
		return lanNumber;
	}

	public void setLanNumber(String lanNumber) {
		this.lanNumber = lanNumber;
	}

	public UUID getSourceId() {
		return sourceId;
	}

	public void setSourceId(UUID sourceId) {
		this.sourceId = sourceId;
	}

	public String getRefTransaction() {
		return refTransaction;
	}

	public void setRefTransaction(String refTransaction) {
		this.refTransaction = refTransaction;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBigLink() {
		return bigLink;
	}

	public void setBigLink(String bigLink) {
		this.bigLink = bigLink;
	}

	public String getShortLink() {
		return shortLink;
	}

	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}

	public long getTrnPaymentId() {
		return trnPaymentId;
	}

	public void setTrnPaymentId(long trnPaymentId) {
		this.trnPaymentId = trnPaymentId;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

}
