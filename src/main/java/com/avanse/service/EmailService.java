package com.avanse.service;

public interface EmailService {

	public String sendSimpleMessage(String to, String subject, String content,long transactionId,String userType);
	
	public String sendSimpleMessageFee(String to, String subject, String content,long transactionId,String userType);

	String sendEmail(String name, String to, String subject, String content, long paymentRequestId, String emailPurpose, String applicantType);
	
}
