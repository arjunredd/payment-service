package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "TrnPaymentReceipt")
public class TrnPaymentReceipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PaymentReceiptID")
	private int paymentReceiptId;

	@OneToOne(targetEntity = TrnPaymentTransaction.class)
	@JoinColumn(name = "PaymentTransactionID")
	TrnPaymentTransaction paymentTransactionId;
	
	/*
	 * @OneToOne(fetch=FetchType.LAZY,optional=false)
	 * 
	 * @JoinColumn(name="PaymentTransactionID")
	 * 
	 * @OnDelete(action=OnDeleteAction.CASCADE)
	 * 
	 * @JsonIgnore private TrnPaymentTransaction trnPaymentTransaction;
	 */

	// @OneToOne(mappedBy="paymentRequest",fetch = FetchType.LAZY,
	// cascade = CascadeType.ALL)
	// private TrnPaymentTransaction trnPaymentTransaction;

	@Column(name = "paymentMode")
	private String paymentMode;

	@Column(name = "excessAdjustTo")
	private String excessAdjustTo;

	@Column(name = "FundingAccount")
	private String fundingAccount;

	@Column(name = "PennantReceiptNo")
	private String pennantReceiptNo;

	@Column(name = "PennantReceiptDate")
	private Date pennantReceiptDate;

	@Column(name = "PennantErrorDesc")
	private String pennantErrorDesc;

	@Column(name = "ReturnCode")
	private String returnCode;

	@Column(name = "ReturnDesc")
	private String returnDesc;
	
	@Column(name = "RequestJSON")
	private String requestJSON;
	
	@Column(name = "ResponseJSON")
	private String responseJSON;

	public TrnPaymentReceipt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrnPaymentReceipt(TrnPaymentTransaction paymentTransactionId, String paymentMode, String excessAdjustTo,
			String fundingAccount, String pennantReceiptNo, Date pennantReceiptDate, String pennantErrorDesc,
			String returnCode, String returnDesc,String requestJSON,String responseJSON) {
		super();
		this.paymentTransactionId = paymentTransactionId;
		this.paymentMode = paymentMode;
		this.excessAdjustTo = excessAdjustTo;
		this.fundingAccount = fundingAccount;
		this.pennantReceiptNo = pennantReceiptNo;
		this.pennantReceiptDate = pennantReceiptDate;
		this.pennantErrorDesc = pennantErrorDesc;
		this.returnCode = returnCode;
		this.returnDesc = returnDesc;
		this.requestJSON=requestJSON;
		this.responseJSON=responseJSON;
	}

	public String getRequestJSON() {
		return requestJSON;
	}

	public void setRequestJSON(String requestJSON) {
		this.requestJSON = requestJSON;
	}

	public String getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(String responseJSON) {
		this.responseJSON = responseJSON;
	}

	public int getPaymentReceiptId() {
		return paymentReceiptId;
	}

	public void setPaymentReceiptId(int paymentReceiptId) {
		this.paymentReceiptId = paymentReceiptId;
	}

	
	  public TrnPaymentTransaction getPaymentTransactionId() { return
	  paymentTransactionId; }
	  
	  public void setPaymentTransactionId(TrnPaymentTransaction
	  paymentTransactionId) { this.paymentTransactionId = paymentTransactionId; }
	 


	public String getPaymentMode() {
		return paymentMode;
	}

	/*
	 * public TrnPaymentTransaction getTrnPaymentTransaction() { return
	 * trnPaymentTransaction; }
	 * 
	 * public void setTrnPaymentTransaction(TrnPaymentTransaction
	 * trnPaymentTransaction) { this.trnPaymentTransaction = trnPaymentTransaction;
	 * }
	 */

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getExcessAdjustTo() {
		return excessAdjustTo;
	}

	public void setExcessAdjustTo(String excessAdjustTo) {
		this.excessAdjustTo = excessAdjustTo;
	}

	public String getFundingAccount() {
		return fundingAccount;
	}

	public void setFundingAccount(String fundingAccount) {
		this.fundingAccount = fundingAccount;
	}

	public String getPennantReceiptNo() {
		return pennantReceiptNo;
	}

	public void setPennantReceiptNo(String pennantReceiptNo) {
		this.pennantReceiptNo = pennantReceiptNo;
	}

	public Date getPennantReceiptDate() {
		return pennantReceiptDate;
	}

	public void setPennantReceiptDate(Date pennantReceiptDate) {
		this.pennantReceiptDate = pennantReceiptDate;
	}

	public String getPennantErrorDesc() {
		return pennantErrorDesc;
	}

	public void setPennantErrorDesc(String pennantErrorDesc) {
		this.pennantErrorDesc = pennantErrorDesc;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

}
