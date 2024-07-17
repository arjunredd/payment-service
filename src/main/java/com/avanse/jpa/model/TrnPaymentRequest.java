package com.avanse.jpa.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import com.avanse.consumer.Fee;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "TrnPaymentRequest")
public class TrnPaymentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PaymentRequestID")
	private int paymentRequestId;

	@Transient
	private String micr;

	@Transient
	private String ifsc;

	@Transient
	private String uniqueReferenceNumber;

	@Column(name = "UserId")
	private String userId;

	@Transient
	private String cif;

	@Transient
	private String sourceAppName;

	@NotNull(message = "Source ID is mandatory")
	@Type(type = "uuid-char")
	@Column(name = "SourceID", updatable = false, nullable = false, columnDefinition = "uniqueidentifier")
	private UUID sourceId;// still is not define in database

	@NotBlank(message = "Loan application number is mandatory")
	@Column(name = "LANumber")
	private String laNumber;

	// @NotBlank(message = "Loan type is mandatory")
	@Column(name = "LoanType")
	private String loanType;

	@Column(name = "Currency")
	private String currency;

	@Column(name = "Amount")
	private Double amount;

	@Column(name = "TotalAmount")
	private Double totalAmount;

	@Transient
	private Double allowedAmt;

	@Transient
	private String customerName;

	@Transient
	private String loanBranch;

	@Transient
	private String mobileNo;

	@Transient
	private String emailId;

	@Transient
	private String address;

	@Transient
	private Double emi_si_pi_amt;

	@Transient
	private Double overdue_emi_amt;

	@Transient
	private Double CURRENT_MONTH_INST;

	@Transient
	private String merchantId;

	@Transient
	private String razorPayKey;

	// @NotBlank(message = "Session ID is mandatory")
	@Column(name = "SessionID")
	private String sessionId;

	@NotBlank(message = "Payment purpose is mandatory")
	@Column(name = "PaymentPurpose")
	private String paymentPurpose;

	// @Temporal(TemporalType.DATE)
	// @Column(name = "PaymentRequestDateTime")
	@Column(name = "PaymentRequestDateTime", updatable = false, nullable = false)
	@CreatedDate
	// 2012-11-16T00:00:00.000
	// @JsonFormat(pattern = "dd-MM-yyyy hh:mm:sss", timezone = "IST")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "IST")
	private Date paymentRequestDateTime = new Date();

	@Column(name = "RequestStatus")
	private String requestStatus;

	@Column(name = "successURL")
	private String successUrl;

	@Column(name = "failureURL")
	private String failureUrl;

	@OneToOne(mappedBy = "paymentRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	private TrnPaymentTransaction trnPaymentTransaction;

	@Column(name = "ErrorDescription")
	private String errorDescription;

	@Transient
	private List<TrnPaymentRequestDetails> charges;

	@NotBlank(message = "Mode is mandatory")
	@Column(name = "Mode")
	private String mode;

	@NotBlank(message = "Request purpose is mandatory")
	@Column(name = "RequestPurpose")
	private String requestPurpose;

	@Column(name = "RefTransaction")
	private String refTransaction;

	@Column(name = "paymentRef")
	private String paymentRef;

	@Column(name = "bankCode")
	private String bankCode;
	
	@Column(name = "feeType")
	private String feeType;

	@Column(name = "valueDate", updatable = false, nullable = false)
	// @JsonFormat(pattern = "dd-MM-yyyy hh:mm:sss", timezone = "IST")
	// @Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "IST")
	private Date valueDate;

	@Column(name = "favourName")
	private String favourName;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "bounceCharge")
	private Double bounceCharge;

	@Column(name = "penalCharge")
	private Double penalCharge;

	@JsonIgnore
	@Column(name = "PaymentRequestJson")
	private String paymentRequestJson;

	@JsonIgnore
	@Column(name = "PaymentResponseJson")
	private String paymentResponseJson;

	@JsonIgnore
	@Column(name = "OverdueAPIRequestJson")
	private String overdueAPIRequestJson;

	@JsonIgnore
	@Column(name = "OverdueAPIResponseJson")
	private String overdueAPIResponseJson;

	@JsonIgnore
	@Column(name = "createdOn", updatable = false, nullable = false)
	private Date createdOn = new Date();

	@Column(name = "CRMSrNo")
	private String crmSrNo;
	
	@Column(name="IsAgentRequest", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean agentRequest;

	// below two parameters added for getPaymentStatusAPI
	@Transient
	private Date startDate;
	@Transient
	private Date endDate;

	@Transient
	private String shortURL;

	@Transient
	private String paymentName;

	@Transient
	List<TrnFeeDetails> trnFeeDetails;

	public TrnPaymentRequest() {
	}

	public TrnPaymentRequest(UUID sourceId, String laNumber, String loanType, String currency, Double amount, String sessionId, String paymentPurpose, Date paymentRequestDateTime, String requestStatus, String successUrl, String failureUrl) {
		super();
		this.sourceId = sourceId;
		this.laNumber = laNumber;
		this.loanType = loanType;
		this.currency = currency;
		this.amount = amount;
		this.sessionId = sessionId;
		this.paymentPurpose = paymentPurpose;
		this.paymentRequestDateTime = paymentRequestDateTime;
		this.requestStatus = requestStatus;
		this.successUrl = successUrl;
		this.failureUrl = failureUrl;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getPaymentRequestJson() {
		return paymentRequestJson;
	}

	public void setPaymentRequestJson(String paymentRequestJson) {
		this.paymentRequestJson = paymentRequestJson;
	}

	public String getPaymentResponseJson() {
		return paymentResponseJson;
	}

	public void setPaymentResponseJson(String paymentResponseJson) {
		this.paymentResponseJson = paymentResponseJson;
	}

	public String getOverdueAPIRequestJson() {
		return overdueAPIRequestJson;
	}

	public void setOverdueAPIRequestJson(String overdueAPIRequestJson) {
		this.overdueAPIRequestJson = overdueAPIRequestJson;
	}

	public String getOverdueAPIResponseJson() {
		return overdueAPIResponseJson;
	}

	public void setOverdueAPIResponseJson(String overdueAPIResponseJson) {
		this.overdueAPIResponseJson = overdueAPIResponseJson;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getBounceCharge() {
		return bounceCharge;
	}

	public void setBounceCharge(Double bounceCharge) {
		this.bounceCharge = bounceCharge;
	}

	public Double getPenalCharge() {
		return penalCharge;
	}

	public void setPenalCharge(Double penalCharge) {
		this.penalCharge = penalCharge;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getRazorPayKey() {
		return razorPayKey;
	}

	public void setRazorPayKey(String razorPayKey) {
		this.razorPayKey = razorPayKey;
	}

	public Double getCURRENT_MONTH_INST() {
		return CURRENT_MONTH_INST;
	}

	public void setCURRENT_MONTH_INST(Double cURRENT_MONTH_INST) {
		CURRENT_MONTH_INST = cURRENT_MONTH_INST;
	}

	public String getRequestPurpose() {
		return requestPurpose;
	}

	public void setRequestPurpose(String requestPurpose) {
		this.requestPurpose = requestPurpose;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRefTransaction() {
		return refTransaction;
	}

	public void setRefTransaction(String refTransaction) {
		this.refTransaction = refTransaction;
	}

	public Double getAllowedAmt() {
		return allowedAmt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLoanBranch() {
		return loanBranch;
	}

	public void setLoanBranch(String loanBranch) {
		this.loanBranch = loanBranch;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getEmi_si_pi_amt() {
		return emi_si_pi_amt;
	}

	public void setEmi_si_pi_amt(Double emi_si_pi_amt) {
		this.emi_si_pi_amt = emi_si_pi_amt;
	}

	public Double getOverdue_emi_amt() {
		return overdue_emi_amt;
	}

	public void setOverdue_emi_amt(Double overdue_emi_amt) {
		this.overdue_emi_amt = overdue_emi_amt;
	}

	public void setAllowedAmt(Double allowedAmt) {
		this.allowedAmt = allowedAmt;
	}

	public TrnPaymentTransaction getTrnPaymentTransaction() {
		return trnPaymentTransaction;
	}

	public void setTrnPaymentTransaction(TrnPaymentTransaction trnPaymentTransaction) {
		this.trnPaymentTransaction = trnPaymentTransaction;
	}

	public List<TrnPaymentRequestDetails> getCharges() {
		return charges;
	}

	public void setCharges(List<TrnPaymentRequestDetails> charges) {
		this.charges = charges;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public int getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(int paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public UUID getSourceId() {
		return sourceId;
	}

	public void setSourceId(UUID sourceId) {
		this.sourceId = sourceId;
	}

	public String getLaNumber() {
		return laNumber;
	}

	public void setLaNumber(String laNumber) {
		this.laNumber = laNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getPaymentRequestDateTime() {
		return paymentRequestDateTime;
	}

	public void setPaymentRequestDateTime(Date paymentRequestDateTime) {
		this.paymentRequestDateTime = paymentRequestDateTime;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public String getFavourName() {
		return favourName;
	}

	public void setFavourName(String favourName) {
		this.favourName = favourName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setMicr(String micr) {
		this.micr = micr;
	}

	public String getMicr() {
		return micr;
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

	public void setTrnFeeDetails(List<TrnFeeDetails> trnFeeDetails) {
		this.trnFeeDetails = trnFeeDetails;
	}

	public List<TrnFeeDetails> getTrnFeeDetails() {
		return trnFeeDetails;
	}

	public String getUniqueReferenceNumber() {
		return uniqueReferenceNumber;
	}

	public void setUniqueReferenceNumber(String uniqueReferenceNumber) {
		this.uniqueReferenceNumber = uniqueReferenceNumber;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShortURL() {
		return shortURL;
	}

	public void setShortURL(String shortURL) {
		this.shortURL = shortURL;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public String getCrmSrNo() {
		return crmSrNo;
	}

	public void setCrmSrNo(String crmSrNo) {
		this.crmSrNo = crmSrNo;
	}

	public String getSourceAppName() {
		return sourceAppName;
	}

	public void setSourceAppName(String sourceAppName) {
		this.sourceAppName = sourceAppName;
	}
	
	public Boolean isAgentRequest() {
		return agentRequest;
	}

	public void setAgentRequest(Boolean agentRequest) {
		this.agentRequest = agentRequest;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	public String getFeeType() {
		return feeType;
	}
	
	@Override
	public String toString() {
		return "TrnPaymentRequest [paymentRequestId=" + paymentRequestId + ", micr=" + micr + ", ifsc=" + ifsc + ", uniqueReferenceNumber=" + uniqueReferenceNumber 
				+ ", userId=" + userId + ", cif=" + cif + ", sourceAppName=" + sourceAppName + ", sourceId=" + sourceId + ", laNumber=" + laNumber + ", loanType=" 
				+ loanType + ", currency=" + currency + ", amount=" + amount + ", totalAmount=" + totalAmount + ", allowedAmt=" + allowedAmt + ", customerName=" 
				+ customerName + ", loanBranch=" + loanBranch + ", mobileNo=" + mobileNo + ", emailId=" + emailId + ", address=" + address + ", emi_si_pi_amt=" 
				+ emi_si_pi_amt + ", overdue_emi_amt=" + overdue_emi_amt + ", CURRENT_MONTH_INST=" + CURRENT_MONTH_INST + ", merchantId=" + merchantId + ", razorPayKey=" 
				+ razorPayKey + ", sessionId=" + sessionId + ", paymentPurpose=" + paymentPurpose + ", paymentRequestDateTime=" + paymentRequestDateTime + ", requestStatus=" 
				+ requestStatus + ", successUrl=" + successUrl + ", failureUrl=" + failureUrl + ", trnPaymentTransaction=" + trnPaymentTransaction + ", errorDescription=" 
				+ errorDescription + ", charges=" + charges + ", mode=" + mode + ", requestPurpose=" + requestPurpose + ", refTransaction="
				+ refTransaction + ", paymentRef=" + paymentRef + ", bankCode=" + bankCode + ", valueDate=" + valueDate + ", favourName=" + favourName + ", remarks=" 
				+ remarks + ", bounceCharge=" + bounceCharge + ", penalCharge=" + penalCharge + ", paymentRequestJson=" + paymentRequestJson + ", paymentResponseJson=" 
				+ paymentResponseJson + ", overdueAPIRequestJson=" + overdueAPIRequestJson + ", overdueAPIResponseJson=" + overdueAPIResponseJson + ", createdOn=" 
				+ createdOn + ", crmSrNo=" + crmSrNo + ", startDate=" + startDate + ", endDate=" + endDate + ", shortURL=" + shortURL + ", paymentName=" + paymentName 
				+ ", trnFeeDetails=" + trnFeeDetails + "]";
	}

}
