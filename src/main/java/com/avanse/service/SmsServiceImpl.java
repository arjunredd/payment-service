package com.avanse.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avanse.jpa.model.TrnFeeSMSHistory;
import com.avanse.jpa.model.TrnPaymentSMSHistory;
import com.avanse.jpa.model.TrnSMSStatusDetails;
import com.avanse.jpa.repository.TrnFeeSMSHistoryRepository;
import com.avanse.jpa.repository.TrnPaymentSMSHistoryRepository;
import com.avanse.jpa.repository.TrnSMSStatusDetailsRepository;

@Service
public class SmsServiceImpl implements SmsService {

	@Value("${userId}")
	private String userId;

	@Value("${password}")
	// private String password="q9wqUYsyM";
	private String password;

	@Value("${smsUrl}")
	// private String smsUrl="https://enterprise.smsgupshup.com/GatewayAPI/rest?";
	private String smsUrl;

	@Value("${proxy.ip}")
	private String proxy_ip;

	@Value("${proxy.port}")
	private String proxy_port;

	@Autowired
	TrnPaymentSMSHistoryRepository trnPaymentSMSHistoryRepository;

	@Autowired
	TrnFeeSMSHistoryRepository trnFeeSMSHistoryRepository;
	
	@Autowired
	private TrnSMSStatusDetailsRepository trnSMSStatusDetailsRepository; 

	@Override
	public String message(String message, String mobileNo, long transactionId, String userType) {

		BufferedReader rd = null;
		HttpsURLConnection conn = null;
		StringBuffer buffer = new StringBuffer();
		String errorDesc = null;
		String errorCode = null;
		try {
			System.out.println("Inside message service...");
			String data = "";
			data += "method=sendMessage";
			data += "&userid=" + userId; // your loginId
			data += "&password=" + URLEncoder.encode(password, "UTF-8"); // your password
			data += "&msg=" + URLEncoder.encode(message, "UTF-8");
			data += "&send_to=" + URLEncoder.encode(mobileNo, "UTF-8"); // a valid 10 digit phone no.
			data += "&v=1.1";
			data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
			data += "&auth_scheme=PLAIN";

			/* Proxy Code */
			
			/*
			 * System.setProperty("https.proxyHost", proxy_ip);
			 * System.setProperty("https.proxyPort", proxy_port);
			 * 
			 * SSLContext sslContext = SSLContext.getInstance("SSL");
			 * 
			 * sslContext.init(null, new TrustManager[] { new X509TrustManager() { public
			 * X509Certificate[] getAcceptedIssuers() {
			 * System.out.println("getAcceptedIssuers ============="); return null; }
			 * 
			 * public void checkClientTrusted(X509Certificate[] certs, String authType) {
			 * System.out.println("checkClientTrusted ============="); }
			 * 
			 * public void checkServerTrusted(X509Certificate[] certs, String authType) {
			 * System.out.println("checkServerTrusted ============="); } } }, new
			 * SecureRandom());
			 * 
			 * HttpsURLConnection.setDefaultSSLSocketFactory(
			 * sslContext.getSocketFactory());
			 * 
			 * HttpsURLConnection .setDefaultHostnameVerifier(new HostnameVerifier() {
			 * public boolean verify(String arg0, SSLSession arg1) {
			 * System.out.println("hostnameVerifier ============="); return true; } });
			 */
			 

			URL url = new URL(smsUrl + data);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;

			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			System.out.println("sms resp:" + buffer.toString());
			
			
			/*
			 * System.clearProperty("https.proxyHost");
			 * System.clearProperty("https.proxyPort");
			 */ 
			 

		} catch (Exception e) {
			e.printStackTrace();
			errorCode = "Exception";
			errorDesc = "error " + e.getLocalizedMessage();
			return "error " + e.getLocalizedMessage();
		} finally {
			try {

				String smsStatus = "ERROR";

				if (buffer.length() > 0) {
					smsStatus = "SUCCESS";

					if (buffer.toString().contains("errror")) {
						errorDesc = buffer.toString();
						errorCode = buffer.substring(0, 13);
					}

				}

				TrnPaymentSMSHistory hist = new TrnPaymentSMSHistory((int) transactionId, userType, mobileNo, message, smsStatus, new Date(), 1, null, errorCode, errorDesc);
				trnPaymentSMSHistoryRepository.saveAndFlush(hist);

				rd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn.disconnect();
		}
		System.out.println("out side message service.........");
		return buffer.toString();
	}

	@Override
	public String sendSMS(String message, String mobileNo, String name, long requestId, String smsPurpose, String applicantType) {

		BufferedReader rd = null;
		HttpsURLConnection conn = null;
		StringBuffer buffer = new StringBuffer();
		String errorDesc = null;
		String errorCode = null;
		try {
			System.out.println("Inside message service...");
			StringBuffer sb = new StringBuffer();
			sb.append("method=sendMessage")
			.append("&userid=" + userId)
			.append("&password=" + URLEncoder.encode(password, "UTF-8"))
			.append("&msg=" + URLEncoder.encode(message, "UTF-8"))
			.append("&send_to=" + URLEncoder.encode(mobileNo, "UTF-8"))
			.append("&v=1.1")
			.append("&msg_type=TEXT")
			.append("&auth_scheme=PLAIN");
			
			/*
			 * System.setProperty("https.proxyHost", proxy_ip);
			 * System.setProperty("https.proxyPort", proxy_port); SSLContext sslContext =
			 * SSLContext.getInstance("SSL"); sslContext.init(null, new TrustManager[] { new
			 * X509TrustManager() { public X509Certificate[] getAcceptedIssuers() {
			 * System.out.println("getAcceptedIssuers ============="); return null; }
			 * 
			 * public void checkClientTrusted(X509Certificate[] certs, String authType) {
			 * System.out.println("checkClientTrusted ============="); }
			 * 
			 * public void checkServerTrusted(X509Certificate[] certs, String authType) {
			 * System.out.println("checkServerTrusted ============="); } } }, new
			 * SecureRandom());
			 * 
			 * HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			 * HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { public
			 * boolean verify(String arg0, SSLSession arg1) {
			 * System.out.println("hostnameVerifier ============="); return true; } });
			 */

			URL url = new URL(smsUrl + sb.toString());
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;

			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			System.out.println("sms resp:" + buffer.toString());
			
			
			/*
			 * System.clearProperty("https.proxyHost");
			 * System.clearProperty("https.proxyPort");
			 */			 
			 
		} catch (Exception e) {
			e.printStackTrace();
			errorCode = "Exception";
			errorDesc = "error " + e.getLocalizedMessage();
			return "error " + e.getLocalizedMessage();
		} finally {
			try {
				String smsStatus = "ERROR";
				if (buffer.length() > 0) {
					smsStatus = "SUCCESS";
					if (buffer.toString().contains("errror")) {
						errorDesc = buffer.toString();
						errorCode = buffer.substring(0, 13);
					}
				}
				TrnSMSStatusDetails trnSMSStatusDetails = new TrnSMSStatusDetails(requestId, mobileNo, message, smsStatus
						, smsPurpose, new Date(), errorCode, errorDesc, applicantType);
				trnSMSStatusDetails.setCustomerName(name);
				trnSMSStatusDetailsRepository.save(trnSMSStatusDetails);
				rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		System.out.println("out side message service.........");
		return buffer.toString();
	}

	@Override
	public String messageFee(String message, String mobileNo, long transactionId, String userType) {

		BufferedReader rd = null;
		HttpsURLConnection conn = null;
		StringBuffer buffer = new StringBuffer();
		String errorDesc = null;
		String errorCode = null;
		try {
			System.out.println("Inside message service...");
			String data = "";
			data += "method=sendMessage";
			data += "&userid=" + userId; // your loginId
			data += "&password=" + URLEncoder.encode(password, "UTF-8"); // your password
			data += "&msg=" + URLEncoder.encode(message, "UTF-8");
			data += "&send_to=" + URLEncoder.encode(mobileNo, "UTF-8"); // a valid 10 digit phone no.
			data += "&v=1.1";
			data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
			data += "&auth_scheme=PLAIN";

			/* Proxy Setting */

			/*
			 * System.setProperty("https.proxyHost", proxy_ip);
			 * System.setProperty("https.proxyPort", proxy_port);
			 * 
			 * SSLContext sslContext = SSLContext.getInstance("SSL");
			 * 
			 * 
			 * sslContext.init(null, new TrustManager[] { new X509TrustManager() { public
			 * X509Certificate[] getAcceptedIssuers() {
			 * System.out.println("getAcceptedIssuers ============="); return null; }
			 * 
			 * public void checkClientTrusted(X509Certificate[] certs, String authType) {
			 * System.out.println("checkClientTrusted ============="); }
			 * 
			 * public void checkServerTrusted(X509Certificate[] certs, String authType) {
			 * System.out.println("checkServerTrusted ============="); } } }, new
			 * SecureRandom());
			 * 
			 * HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			 * 
			 * HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { public
			 * boolean verify(String arg0, SSLSession arg1) {
			 * System.out.println("hostnameVerifier ============="); return true; } });
			 * 
			 */			 

			URL url = new URL(smsUrl + data);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;

			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			System.out.println("sms resp:" + buffer.toString());
			
			
			/*
			 * System.clearProperty("https.proxyHost");
			 * System.clearProperty("https.proxyPort");
			 */
			 
			 

		} catch (Exception e) {
			e.printStackTrace();
			errorCode = "Exception";
			errorDesc = "error " + e.getLocalizedMessage();
			return "error " + e.getLocalizedMessage();
		} finally {
			try {

				String smsStatus = "ERROR";

				if (buffer.length() > 0) {
					smsStatus = "SUCCESS";

					if (buffer.toString().contains("errror")) {
						errorDesc = buffer.toString();
						errorCode = buffer.substring(0, 13);
					}

				}

				TrnFeeSMSHistory hist = new TrnFeeSMSHistory((int) transactionId, userType, mobileNo, message, smsStatus, new Date(), 1, null, errorCode, errorDesc);
				trnFeeSMSHistoryRepository.saveAndFlush(hist);

				rd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn.disconnect();
		}
		System.out.println("out side message service.........");
		return buffer.toString();
	}

}
