package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TrnSMSStatusDetailsHistory")
public class TrnSMSStatusDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SMSId")
	private long smsId;

	@Column(name = "PaymentRequestID")
	private long paymentRequestId;

	@Column(name = "CustomerName")
	private String customerName;

	@Column(name = "ContactNo")
	private String contactNo;

	@Column(name = "message")
	private String message;

	@Column(name = "SMSStatus")
	private String smsStatus;

	@Column(name = "SMSPurpose")
	private String smsPurpose;

	@Column(name = "SMSSentDate")
	private Date smsSentDate = new Date();

	@Column(name = "errorCode")
	private String errorCode;

	@Column(name = "errorDesc")
	private String errorDesc;

	@Column(name = "ApplicantType")
	private String applicantType;

	public TrnSMSStatusDetails() {

	}

	public TrnSMSStatusDetails(long paymentRequestId, String contactNo, String message, String smsStatus, String smsPurpose, Date smsSentDate, String errorCode, String errorDesc, String applicantType) {
		super();
		this.paymentRequestId = paymentRequestId;
		this.contactNo = contactNo;
		this.message = message;
		this.smsStatus = smsStatus;
		this.smsPurpose = smsPurpose;
		this.smsSentDate = smsSentDate;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.applicantType = applicantType;
	}

	public long getSmsId() {
		return smsId;
	}

	public void setSmsId(long smsId) {
		this.smsId = smsId;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	public String getSmsPurpose() {
		return smsPurpose;
	}

	public void setSmsPurpose(String smsPurpose) {
		this.smsPurpose = smsPurpose;
	}

	public Date getSmsSentDate() {
		return smsSentDate;
	}

	public void setSmsSentDate(Date smsSentDate) {
		this.smsSentDate = smsSentDate;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}