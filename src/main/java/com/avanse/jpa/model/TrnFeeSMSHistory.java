package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TrnFeeSMSHistory")
public class TrnFeeSMSHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PaymentSMSDetID")
	private int paymentSMSDetID;

	@Column(name = "PaymentFeeId")
	private int paymentFeeId;

	@Column(name = "UserType")
	private String userType;

	@Column(name = "ContactNo")
	private String contactNo;

	@Column(name = "message")
	private String message;
	
	@Column(name="SMSStatus")
	private String smsStatus;
	
	@Column(name="SMSSentDate")
	private Date smsSentDate=new Date();
	
	@Column(name="attemptNum")
	private int attemptNum;
	
	@Column(name="processingFlag")
	private String processingFlag;
	
	@Column(name="errorCode")
	private String errorCode;

	@Column(name="errorDesc")
	private String errorDesc;

	public TrnFeeSMSHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrnFeeSMSHistory(int paymentFeeId, String userType, String contactNo,
			String message, String smsStatus, Date smsSentDate, int attemptNum, String processingFlag, String errorCode,
			String errorDesc) {
		super();
		this.paymentFeeId = paymentFeeId;
		this.userType = userType;
		this.contactNo = contactNo;
		this.message = message;
		this.smsStatus = smsStatus;
		this.smsSentDate = smsSentDate;
		this.attemptNum = attemptNum;
		this.processingFlag = processingFlag;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}



	public int getPaymentSMSDetID() {
		return paymentSMSDetID;
	}

	public void setPaymentSMSDetID(int paymentSMSDetID) {
		this.paymentSMSDetID = paymentSMSDetID;
	}


	public int getPaymentFeeId() {
		return paymentFeeId;
	}

	public void setPaymentFeeId(int paymentFeeId) {
		this.paymentFeeId = paymentFeeId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public Date getSmsSentDate() {
		return smsSentDate;
	}

	public void setSmsSentDate(Date smsSentDate) {
		this.smsSentDate = smsSentDate;
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
