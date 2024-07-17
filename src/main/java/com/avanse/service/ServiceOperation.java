package com.avanse.service;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.avanse.model.TinyurlData;

@Service
public class ServiceOperation {
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${tinyUrl}")
	private String tinyUrl;
	
	@Value("${tiny.expirytime}")
	private int tinyExpiry;
	
	@Value("${tiny.username}")
	private String tinyUsername;
	
	@Value("${tiny.password}")
	private String tinyPassword;

	public String generateTinyURL(String bigURL, String LANumber) {
		Date dt = new Date();
		TinyurlData resp = null;
		try {

			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.DATE, tinyExpiry);
			Date expiry = c.getTime();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String auth = tinyUsername + ":" + tinyPassword;
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.add("Authorization", authHeader);
			
			TinyurlData tinyURLRequestData = new TinyurlData();
			tinyURLRequestData.setRedirectUrl(bigURL);
			tinyURLRequestData.setExpiryDate(new SimpleDateFormat("dd/MM/yyyy HH:mma").format(expiry));
			tinyURLRequestData.setRefCode(LANumber);

			JSONObject inputObject = new JSONObject();
			inputObject.put("RedirectUrl", tinyURLRequestData.getRedirectUrl());
			inputObject.put("ExpiryDate", tinyURLRequestData.getExpiryDate());
			inputObject.put("RefCode", tinyURLRequestData.getRefCode());

			HttpEntity<String> request = new HttpEntity<String>(inputObject.toString(), headers);
			ResponseEntity<TinyurlData> response = restTemplate.postForEntity(tinyUrl, request, TinyurlData.class);
			resp = response.getBody();

		} catch (Exception e) {
			System.out.println("Exception in generateTinyUrl data method:" + e.getMessage());
			e.printStackTrace();
		}

		// elmsList.parallelStream().forEach();

		return resp != null ? resp.getLinkUrl() : "";

	}

}
