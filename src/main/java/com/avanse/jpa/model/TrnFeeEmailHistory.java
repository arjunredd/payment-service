package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TrnFeeEmailHistory")
public class TrnFeeEmailHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PaymentEmailDetID")
	private int paymentEmailDetID;

	@Column(name = "PaymentFeeID")
	private int paymentFeeID;

	@Column(name = "UserType")
	private String userType;

	@Column(name = "FromEmailID")
	private String fromEmailID;
	
	@Column(name="EmailID")
	private String emailID;
	
	@Column(name="Subject")
	private String subject;

	@Column(name = "Body")
	private String body;
	
	@Column(name="EmailStatus")
	private String emailStatus;
	
	@Column(name="EmailSentDate")
	private Date emailSentDate;
	
	@Column(name="attemptNum")
	private int attemptNum;
	
	@Column(name="processingFlag")
	private String processingFlag;
	
	@Column(name="errorCode")
	private String errorCode;

	@Column(name="errorDesc")
	private String errorDesc;

	public TrnFeeEmailHistory(int paymentFeeID, String userType, String fromEmailID, String emailID,
			String subject, String body, String emailStatus, Date emailSentDate, int attemptNum, String processingFlag,
			String errorCode, String errorDesc) {
		super();
		this.paymentFeeID = paymentFeeID;
		this.userType = userType;
		this.fromEmailID = fromEmailID;
		this.emailID = emailID;
		this.subject = subject;
		this.body = body;
		this.emailStatus = emailStatus;
		this.emailSentDate = emailSentDate;
		this.attemptNum = attemptNum;
		this.processingFlag = processingFlag;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public TrnFeeEmailHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getPaymentEmailDetID() {
		return paymentEmailDetID;
	}

	public void setPaymentEmailDetID(int paymentEmailDetID) {
		this.paymentEmailDetID = paymentEmailDetID;
	}

	public int getPaymentFeeID() {
		return paymentFeeID;
	}

	public void setPaymentFeeID(int paymentFeeID) {
		this.paymentFeeID = paymentFeeID;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFromEmailID() {
		return fromEmailID;
	}

	public void setFromEmailID(String fromEmailID) {
		this.fromEmailID = fromEmailID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
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

	public int getAttemptNum() {
		return attemptNum;
	}

	public void setAttemptNum(int attemptNum) {
		this.attemptNum = attemptNum;
	}

	public String getProcessingFlag() {
		return processingFlag;
	}

	public void setProcessingFlag(String processingFlag) {
		this.processingFlag = processingFlag;
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

	
}
