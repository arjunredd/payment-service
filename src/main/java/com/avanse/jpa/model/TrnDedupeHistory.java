package com.avanse.jpa.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TrnDedupeHistory")
public class TrnDedupeHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DedupID")
	private int dedupID;
	
	@Column(name = "LANumber")
	private String lANumber;
	
	@Column(name = "RefTransaction")
	private String refTransaction;
	
	@Column(name = "PaymentPurpose")
	private String paymentPurpose;
	
	@Column(name="Mode")
	private String mode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RequestDateTime")
	private Date requestDateTime=new Date();
	
	@Type(type="uuid-char")
	@Column(name = "SourceID", updatable = false,columnDefinition="uniqueidentifier")
	private UUID sourceID;

	public TrnDedupeHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrnDedupeHistory(String lANumber, String refTransaction, String paymentPurpose, String mode,
			Date requestDateTime, UUID sourceID) {
		super();
		this.lANumber = lANumber;
		this.refTransaction = refTransaction;
		this.paymentPurpose = paymentPurpose;
		this.mode = mode;
		this.requestDateTime = requestDateTime;
		this.sourceID = sourceID;
	}



	public String getlANumber() {
		return lANumber;
	}

	public void setlANumber(String lANumber) {
		this.lANumber = lANumber;
	}

	public String getRefTransaction() {
		return refTransaction;
	}

	public void setRefTransaction(String refTransaction) {
		this.refTransaction = refTransaction;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Date getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(Date requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public UUID getSourceID() {
		return sourceID;
	}

	public void setSourceID(UUID sourceID) {
		this.sourceID = sourceID;
	}
	
}
