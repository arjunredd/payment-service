package com.avanse.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.avanse.jpa.model.TrnEmailStatusDetails;
import com.avanse.jpa.model.TrnFeeEmailHistory;
import com.avanse.jpa.model.TrnPaymentEmailHistory;
import com.avanse.jpa.repository.TrnEmailStatusDetailsRepository;
import com.avanse.jpa.repository.TrnFeeEmailHistoryRepository;
import com.avanse.jpa.repository.TrnPaymentEmailHistoryRepository;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    private JavaMailSender emailSender;
	
	@Value("${mail.from}")
	private String mailFrom;
	
	@Autowired
	TrnPaymentEmailHistoryRepository trnPaymentEmailHistoryRepository;

	@Autowired
	TrnFeeEmailHistoryRepository trnFeeEmailHistoryRepository;
	
	@Autowired
	private TrnEmailStatusDetailsRepository trnEmailStatusDetailsRepository;

	@Override
	public String sendSimpleMessage(String to, String subject, String content,long transactionId,String userType) {

		MimeMessage message = emailSender.createMimeMessage();
		boolean status=false;
		String errorDesc=null;
		String errorCode=null;
		  OutputStream outputStream=null;
		  try {
		   MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		    helper.setTo(InternetAddress.parse(to));
		    helper.setSubject(subject);
		    helper.setText(content);
		    helper.setFrom(mailFrom);
		    emailSender.send(message);
		    status=true;
	      }
	      catch(MailException e)
	      {
	    	  errorCode="Exception";
	    	  e.printStackTrace();
	    	  errorDesc="error "+e.getLocalizedMessage();
	    	  return "error "+e.getLocalizedMessage();
	      }catch(MessagingException e) {
	    	  e.printStackTrace();
	    	  errorCode="Exception";
	    	  errorDesc="error "+e.getLocalizedMessage();
	    	  return "error "+e.getLocalizedMessage();
	      }
	      
	      finally {
	            try {
	            	
	            	String emailStatus="ERROR";
					
					if(status) {
						emailStatus="SUCCESS";
						
					}
					
					TrnPaymentEmailHistory hist=new TrnPaymentEmailHistory((int)transactionId,userType, 
							mailFrom, to, subject, content, emailStatus, new Date(), 1, null, errorCode, errorDesc);
					trnPaymentEmailHistoryRepository.saveAndFlush(hist);
	            	
					if (outputStream != null) {
	                	outputStream.close();
	                }
	            }
	            catch (IOException ioe) {
	                System.out.println("Error while closing stream: " + ioe);
	            }
	 
	        }
	
		  return "success";
	}

	@Override
	public String sendSimpleMessageFee(String to, String subject, String content, long transactionId, String userType) {

		MimeMessage message = emailSender.createMimeMessage();
		boolean status=false;
		String errorDesc=null;
		String errorCode=null;
		  OutputStream outputStream=null;
		  try {
		   MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		    helper.setTo(InternetAddress.parse(to));
		    helper.setSubject(subject);
		    helper.setText(content);
		    helper.setFrom(mailFrom);
		    emailSender.send(message);
		    status=true;
	      }
	      catch(MailException e)
	      {
	    	  errorCode="Exception";
	    	  e.printStackTrace();
	    	  errorDesc="error "+e.getLocalizedMessage();
	    	  return "error "+e.getLocalizedMessage();
	      }catch(MessagingException e) {
	    	  e.printStackTrace();
	    	  errorCode="Exception";
	    	  errorDesc="error "+e.getLocalizedMessage();
	    	  return "error "+e.getLocalizedMessage();
	      }
	      
	      finally {
	            try {
	            	
	            	String emailStatus="ERROR";
					
					if(status) {
						emailStatus="SUCCESS";
						
					}
					
					TrnFeeEmailHistory hist=new TrnFeeEmailHistory((int)transactionId,userType, 
							mailFrom, to, subject, content, emailStatus, new Date(), 1, null, errorCode, errorDesc);
					trnFeeEmailHistoryRepository.saveAndFlush(hist);
	            	
					if (outputStream != null) {
	                	outputStream.close();
	                }
	            }
	            catch (IOException ioe) {
	                System.out.println("Error while closing stream: " + ioe);
	            }
	 
	        }
	
		  return "success";
	}
	
	@Override
	public String sendEmail(String name, String to, String subject, String content,
			long paymentRequestId, String emailPurpose, String applicantType) {
		MimeMessage message = emailSender.createMimeMessage();
		boolean status = false;
		String errorDesc = null;
		String errorCode = null;
		OutputStream outputStream = null;
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(InternetAddress.parse(to));
			helper.setSubject(subject);
			helper.setText(content);
			helper.setFrom(mailFrom);
			emailSender.send(message);
			status = true;
		} catch (MailException e) {
			errorCode = "Exception";
			e.printStackTrace();
			errorDesc = "error " + e.getLocalizedMessage();
			return "error " + e.getLocalizedMessage();
		} catch (MessagingException e) {
			e.printStackTrace();
			errorCode = "Exception";
			errorDesc = "error " + e.getLocalizedMessage();
			return "error " + e.getLocalizedMessage();
		}

		finally {
			try {
				String emailStatus = "ERROR";
				if (status) {
					emailStatus = "SUCCESS";
				}
				TrnEmailStatusDetails trnEmailStatusDetails = new TrnEmailStatusDetails(mailFrom, to, 
						subject, content, emailStatus, new Date(), errorCode, errorDesc, emailPurpose, applicantType);
				trnEmailStatusDetails.setCustomerName(name);
				trnEmailStatusDetails.setPaymentRequestId(paymentRequestId);
				trnEmailStatusDetailsRepository.save(trnEmailStatusDetails);
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}
		}
		return "success";
	}
}
