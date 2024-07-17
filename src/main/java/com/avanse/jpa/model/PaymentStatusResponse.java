package com.avanse.jpa.model;

import java.util.Date;

public class PaymentStatusResponse {

	private String PaymentRequestID;
	private String LoanNumber;
	private String Userid;
	private String Uniquerefereceno;
	private String LoanType;
	private String AccountId;
	private String SourceApplicationName;
	private String RequestPurpose;
	private String Mode;
	private String PaymentPurpose;
	private String MerchantID;
	private double Amount;
	private String RequestStatus;
	private String PaymentRef;
	private String RefTransaction;
	private String OrderId;
	private String PaymentId;
	private String FundingAccount;
	private String BankCode;
	private String Currency;
	private String FavourName;
	private Date RequestDate;
	private Date ReceiptDate;
	private String PennantReceiptNo;
	private String AuthCode;
	private String Remark;
	private String PaymentRequestError;
	private String ReceiptError;
	private String WebhookError;
	private String SMSStatus;
	private String EmailStatus;

	public String getPaymentRequestID() {
		return PaymentRequestID;
	}

	public void setPaymentRequestID(String paymentRequestID) {
		PaymentRequestID = paymentRequestID;
	}

	public String getLoanNumber() {
		return LoanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		LoanNumber = loanNumber;
	}

	public String getUserid() {
		return Userid;
	}

	public void setUserid(String userid) {
		Userid = userid;
	}

	public String getUniquerefereceno() {
		return Uniquerefereceno;
	}

	public void setUniquerefereceno(String uniquerefereceno) {
		Uniquerefereceno = uniquerefereceno;
	}

	public String getLoanType() {
		return LoanType;
	}

	public void setLoanType(String loanType) {
		LoanType = loanType;
	}

	public String getAccountId() {
		return AccountId;
	}

	public void setAccountId(String accountId) {
		AccountId = accountId;
	}

	public String getSourceApplicationName() {
		return SourceApplicationName;
	}

	public void setSourceApplicationName(String sourceApplicationName) {
		SourceApplicationName = sourceApplicationName;
	}

	public String getRequestPurpose() {
		return RequestPurpose;
	}

	public void setRequestPurpose(String requestPurpose) {
		RequestPurpose = requestPurpose;
	}

	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
	}

	public String getPaymentPurpose() {
		return PaymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		PaymentPurpose = paymentPurpose;
	}

	public String getMerchantID() {
		return MerchantID;
	}

	public void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public String getRequestStatus() {
		return RequestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		RequestStatus = requestStatus;
	}

	public String getPaymentRef() {
		return PaymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		PaymentRef = paymentRef;
	}

	public String getRefTransaction() {
		return RefTransaction;
	}

	public void setRefTransaction(String refTransaction) {
		RefTransaction = refTransaction;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getPaymentId() {
		return PaymentId;
	}

	public void setPaymentId(String paymentId) {
		PaymentId = paymentId;
	}

	public String getFundingAccount() {
		return FundingAccount;
	}

	public void setFundingAccount(String fundingAccount) {
		FundingAccount = fundingAccount;
	}

	public String getBankCode() {
		return BankCode;
	}

	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getFavourName() {
		return FavourName;
	}

	public void setFavourName(String favourName) {
		FavourName = favourName;
	}

	public Date getRequestDate() {
		return RequestDate;
	}

	public void setRequestDate(Date requestDate) {
		RequestDate = requestDate;
	}

	public Date getReceiptDate() {
		return ReceiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		ReceiptDate = receiptDate;
	}

	public String getPennantReceiptNo() {
		return PennantReceiptNo;
	}

	public void setPennantReceiptNo(String pennantReceiptNo) {
		PennantReceiptNo = pennantReceiptNo;
	}

	public String getAuthCode() {
		return AuthCode;
	}

	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getPaymentRequestError() {
		return PaymentRequestError;
	}

	public void setPaymentRequestError(String paymentRequestError) {
		PaymentRequestError = paymentRequestError;
	}

	public String getReceiptError() {
		return ReceiptError;
	}

	public void setReceiptError(String receiptError) {
		ReceiptError = receiptError;
	}

	public String getWebhookError() {
		return WebhookError;
	}

	public void setWebhookError(String webhookError) {
		WebhookError = webhookError;
	}

	public String getSMSStatus() {
		return SMSStatus;
	}

	public void setSMSStatus(String sMSStatus) {
		SMSStatus = sMSStatus;
	}

	public String getEmailStatus() {
		return EmailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		EmailStatus = emailStatus;
	}

	@Override
	public String toString() {
		return "GetPaymentStatusResponse [PaymentRequestID=" + PaymentRequestID + ", LoanNumber=" + LoanNumber
				+ ", Userid=" + Userid + ", Uniquerefereceno=" + Uniquerefereceno + ", LoanType=" + LoanType
				+ ", AccountId=" + AccountId + ", SourceApplicationName=" + SourceApplicationName + ", RequestPurpose="
				+ RequestPurpose + ", Mode=" + Mode + ", PaymentPurpose=" + PaymentPurpose + ", MerchantID="
				+ MerchantID + ", Amount=" + Amount + ", RequestStatus=" + RequestStatus + ", PaymentRef=" + PaymentRef
				+ ", RefTransaction=" + RefTransaction + ", OrderId=" + OrderId + ", PaymentId=" + PaymentId
				+ ", FundingAccount=" + FundingAccount + ", BankCode=" + BankCode + ", Currency=" + Currency
				+ ", FavourName=" + FavourName + ", RequestDate=" + RequestDate + ", ReceiptDate=" + ReceiptDate
				+ ", PennantReceiptNo=" + PennantReceiptNo + ", AuthCode=" + AuthCode + ", Remark=" + Remark
				+ ", PaymentRequestError=" + PaymentRequestError + ", ReceiptError=" + ReceiptError + ", WebhookError="
				+ WebhookError + ", SMSStatus=" + SMSStatus + ", EmailStatus=" + EmailStatus + "]";
	}

}
