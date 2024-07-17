package com.avanse.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Order{

	private String orderId;
	private long amount;
	private long amount_paid;
	private long amount_due;
	private String currency;
	private String receipt;
	private String status;
	private Integer attempts;
	private boolean payment_capture;
	private long created_at;
	private String entity;
	private String merchantId;
	private String razorPayKey;
	
	
	public Order(Long amount, String currency) {
		super();
		this.amount = amount;
		this.currency = currency;
	}
	//private JSONObject notes;
	
	/*public JSONObject getNotes() {
		return notes;
	}
	public void setNotes(JSONObject notes) {
		this.notes = notes;
	}*/
	
	public String getOrderId() {
		return orderId;
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

	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Long getAmount_paid() {
		return amount_paid;
	}
	public void setAmount_paid(Long amount_paid) {
		this.amount_paid = amount_paid;
	}
	public Long getAmount_due() {
		return amount_due;
	}
	public void setAmount_due(Long amount_due) {
		this.amount_due = amount_due;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getReceipt() {
		return receipt;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	public boolean isPayment_capture() {
		return payment_capture;
	}
	public void setPayment_capture(boolean payment_capture) {
		this.payment_capture = payment_capture;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	
}
