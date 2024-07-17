package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TrnEmailStatusDetailsHistory")
public class TrnEmailStatusDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emailStatusId")
	private int emailStatusId;

	@Column(name = "PaymentRequestID")
	private long paymentRequestId;

	@Column(name = "CustomerName")
	private String customerName;

	@Column(name = "FromEmailID")
	private String fromEmailID;

	@Column(name = "EmailID")
	private String toEmailId;

	@Column(name = "ApplicantType")
	private String applicantType;

	@Column(name = "Subject")
	private String subject;

	@Column(name = "Body")
	private String body;

	@Column(name = "EmailStatus")
	private String emailStatus;

	@Column(name = "EmailSentDate")
	private Date emailSentDate;

	@Column(name = "errorCode")
	private String errorCode;

	@Column(name = "errorDesc")
	private String errorDesc;

	@Column(name = "Email_Purpose")
	private String emailPurpose;

	public TrnEmailStatusDetails() {

	}

	public TrnEmailStatusDetails(String fromEmailID, String toEmailId, String subject, String body, String emailStatus, Date emailSentDate, String errorCode, String errorDesc, String emailPurpose, String applicantType) {
		super();
		this.fromEmailID = fromEmailID;
		this.toEmailId = toEmailId;
		this.subject = subject;
		this.body = body;
		this.emailStatus = emailStatus;
		this.emailSentDate = emailSentDate;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.emailPurpose = emailPurpose;
		this.applicantType = applicantType;
	}

	public int getEmailStatusId() {
		return emailStatusId;
	}

	public void setEmailStatusId(int emailStatusId) {
		this.emailStatusId = emailStatusId;
	}

	public String getFromEmailID() {
		return fromEmailID;
	}

	public void setFromEmailID(String fromEmailID) {
		this.fromEmailID = fromEmailID;
	}

	public String getToEmailId() {
		return toEmailId;
	}

	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public Date getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
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

	public String getEmailPurpose() {
		return emailPurpose;
	}

	public void setEmailPurpose(String emailPurpose) {
		this.emailPurpose = emailPurpose;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
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