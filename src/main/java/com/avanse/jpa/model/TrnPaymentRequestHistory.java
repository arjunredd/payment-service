package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "TrnPaymentRequestHistory")
public class TrnPaymentRequestHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RequestID")
	private int requestID;

	@Column(name = "PaymentRequestJson")
	private String paymentRequestJson;

	@Column(name = "PaymentResponseJson")
	private String paymentResponseJson;
	
	@Column(name = "RequestType")
	private String requestType;
	
	@CreatedDate
	@Column(name = "requestDate", updatable = false, nullable = false)
	private Date requestDate=new Date();

	public TrnPaymentRequestHistory(String paymentRequestJson, String paymentResponseJson, String requestType) {
		super();
		this.paymentRequestJson = paymentRequestJson;
		this.paymentResponseJson = paymentResponseJson;
		this.requestType = requestType;
	}

	public TrnPaymentRequestHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
}
