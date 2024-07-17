package com.avanse.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="TrnPaymentRequestDetails")
public class TrnPaymentRequestDetails {

	@Id
	@Column(name="PaymentRequestDetID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int paymentRequestDetId;
	
	@Column(name="ChargeType")
	private String chargeType;
	
	@Column(name="ChargeAmount")
	private double chargeAmount;
	
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="PaymentRequestID",nullable=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JsonIgnore
	private TrnPaymentRequest trnPaymentRequest;

	public TrnPaymentRequestDetails(String chargeType, double chargeAmount,TrnPaymentRequest trnPaymentRequest) {
		super();
		this.chargeType = chargeType;
		this.chargeAmount = chargeAmount;
		this.trnPaymentRequest=trnPaymentRequest;
	}

	public TrnPaymentRequestDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getPaymentRequestDetId() {
		return paymentRequestDetId;
	}

	public void setPaymentRequestDetId(int paymentRequestDetId) {
		this.paymentRequestDetId = paymentRequestDetId;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public TrnPaymentRequest getTrnPaymentRequest() {
		return trnPaymentRequest;
	}

	public void setTrnPaymentRequest(TrnPaymentRequest trnPaymentRequest) {
		this.trnPaymentRequest = trnPaymentRequest;
	}
 	
	
}
