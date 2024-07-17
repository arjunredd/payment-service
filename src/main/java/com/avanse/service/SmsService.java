package com.avanse.service;

public interface SmsService {
	public String message(String message, String mobileNo, long transactionId, String userType);

	public String messageFee(String message, String mobileNo, long transactionId, String userType);

	String sendSMS(String message, String mobileNo, String name, long transactionId, String smsPurpose, String applicatntType);

}
