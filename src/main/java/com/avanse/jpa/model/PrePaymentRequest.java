package com.avanse.jpa.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PrePaymentRequest {
	private String laNumber;
	private String loanType;
	private String mode;
	private String requestPurpose;
	private String paymentPurpose;
	private Double amount;
	private Double totalAmount;
	private String crmSrNo;
	private String currency;
	private String customerName;
	private String mobileNo;
	private String emailId;
	private String feeType;
	private UUID sourceId;// still is not define in database
	@CreatedDate
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "IST")
	private Date paymentRequestDateTime = new Date();
	private List<AccountHolderDetails> receipents;
	private String userId;
	private String sessionId;
	private String cif;
	List<TrnFeeDetails> feesList;

	public String getLaNumber() {
		return laNumber;
	}

	public void setLaNumber(String laNumber) {
		this.laNumber = laNumber;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCrmSrNo() {
		return crmSrNo;
	}

	public void setCrmSrNo(String crmSrNo) {
		this.crmSrNo = crmSrNo;
	}

	public List<AccountHolderDetails> getReceipents() {
		return receipents;
	}

	public void setReceipents(List<AccountHolderDetails> receipents) {
		this.receipents = receipents;
	}

	public UUID getSourceId() {
		return sourceId;
	}

	public void setSourceId(UUID sourceId) {
		this.sourceId = sourceId;
	}

	public Date getPaymentRequestDateTime() {
		return paymentRequestDateTime;
	}

	public void setPaymentRequestDateTime(Date paymentRequestDateTime) {
		this.paymentRequestDateTime = paymentRequestDateTime;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getRequestPurpose() {
		return requestPurpose;
	}

	public void setRequestPurpose(String requestPurpose) {
		this.requestPurpose = requestPurpose;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public List<TrnFeeDetails> getFeesList() {
		return feesList;
	}

	public void setFeesList(List<TrnFeeDetails> feesList) {
		this.feesList = feesList;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	@Override
	public String toString() {
		return "PrePaymentRequest [laNumber=" + laNumber + ", loanType=" + loanType + ", mode=" + mode
				+ ", requestPurpose=" + requestPurpose + ", paymentPurpose=" + paymentPurpose + ", amount=" + amount
				+ ", totalAmount=" + totalAmount + ", crmSrNo=" + crmSrNo + ", currency=" + currency + ", customerName="
				+ customerName + ", mobileNo=" + mobileNo + ", emailId=" + emailId + ", sourceId=" + sourceId
				+ ", paymentRequestDateTime=" + paymentRequestDateTime + ", receipents=" + receipents + ", userId="
				+ userId + ", sessionId=" + sessionId + ", cif=" + cif + ", feesList=" + feesList + ", getLaNumber()="
				+ getLaNumber() + ", getPaymentPurpose()=" + getPaymentPurpose() + ", getAmount()=" + getAmount()
				+ ", getCrmSrNo()=" + getCrmSrNo() + ", getReceipents()=" + getReceipents() + ", getSourceId()="
				+ getSourceId() + ", getPaymentRequestDateTime()=" + getPaymentRequestDateTime() + ", getMode()="
				+ getMode() + ", getLoanType()=" + getLoanType() + ", getRequestPurpose()=" + getRequestPurpose()
				+ ", getTotalAmount()=" + getTotalAmount() + ", getUserId()=" + getUserId() + ", getSessionId()="
				+ getSessionId() + ", getCurrency()=" + getCurrency() + ", getCustomerName()=" + getCustomerName()
				+ ", getMobileNo()=" + getMobileNo() + ", getEmailId()=" + getEmailId() + ", getCif()=" + getCif()
				+ ", getFeesList()=" + getFeesList() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
