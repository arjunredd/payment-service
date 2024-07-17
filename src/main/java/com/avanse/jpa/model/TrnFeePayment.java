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
@Table(name = "TrnFeePayment")
public class TrnFeePayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="UniqueReferenceNumber")
	private String uniqueReferenceNumber;
	
	@Column(name="PaymentRequestId")
	private String paymentRequestId;
	
	@Column(name="UserId")
	private String userId;
	
	@Column(name = "LanNumber")
	private String lanNumber;
	
	@NotNull(message = "Source ID is mandatory")
	@Type(type = "uuid-char")
	@Column(name = "SourceId", updatable = false, nullable = false, columnDefinition = "uniqueidentifier")
	private UUID sourceId;
	
	@Column(name="RefTransaction")
	private String refTransaction;
	
	@Column(name="PaymentRef")
	private String paymentRef;
	
	@Column(name="Mode")
	private String mode;
	
	@Column(name="TotalAmount")
	private double totalAmount;
	
	@Column(name="EmailId")
	private String emailId;
	
	@Column(name="MobileNumber")
	private String mobileNumber;
	
	@Column(name="CustomerName")
	private String customerName;
	
	@Column(name="CIF")
	private String cif;
	
	@Column(name="BigLink")
	private String bigLink;
	
	@Column(name="ShortLink")
	private String shortLink;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUniqueReferenceNumber() {
		return uniqueReferenceNumber;
	}

	public void setUniqueReferenceNumber(String uniqueReferenceNumber) {
		this.uniqueReferenceNumber = uniqueReferenceNumber;
	}

	public String getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(String paymentRequestId) {
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

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
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

}
