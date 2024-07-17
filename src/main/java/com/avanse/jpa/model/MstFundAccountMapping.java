package com.avanse.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
@Entity
public class MstFundAccountMapping
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "FundAccMapID")
	private int fundAccMapID;
	
	@Column(name = "LoanType")
	private String loanType;
	
	@Column(name="PaymentPurpose")
	private String paymentPurpose;
	
	@Column(name = "ChargesBearBy")
	private String chargesBearBy;
	
	@Column(name = "MerchantID")
	private String merchantID;
	
	@Column(name = "FundingAccount")
	private String fundingAccount;
	
	@Column(name = "ReceiptService")
	private String receiptService;
	
	@Column(name = "IsActive")
	private boolean isActive;
	
	@Column(name = "CreatedOn")
	private java.sql.Date createdOn;
	
	@Column(name = "CreatedBy")
	private String createdBy;
	
	@Column(name="IsDeleted")
	private boolean isDeleted;
	
	@Column(name="RazorPayKey")
	private String razorPayKey;
	
	@Column(name="RazorSecret")
	private String razorSecret;
	
	public String getRazorPayKey() {
		return razorPayKey;
	}
	public void setRazorPayKey(String razorPayKey) {
		this.razorPayKey = razorPayKey;
	}
	public String getRazorSecret() {
		return razorSecret;
	}
	public void setRazorSecret(String razorSecret) {
		this.razorSecret = razorSecret;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public String getPaymentPurpose() {
		return paymentPurpose;
	}
	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}
	public int getFundAccMapID() {
		return fundAccMapID;
	}
	public void setFundAccMapID(int fundAccMapID) {
		this.fundAccMapID = fundAccMapID;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
		public String getChargesBearBy() {
		return chargesBearBy;
	}
	public void setChargesBearBy(String chargesBearBy) {
		this.chargesBearBy = chargesBearBy;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getFundingAccount() {
		return fundingAccount;
	}
	public void setFundingAccount(String fundingAccount) {
		this.fundingAccount = fundingAccount;
	}
	public String getReceiptService() {
		return receiptService;
	}
	public void setReceiptService(String receiptService) {
		this.receiptService = receiptService;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public java.sql.Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(java.sql.Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
			
}