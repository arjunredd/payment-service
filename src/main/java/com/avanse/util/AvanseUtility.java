package com.avanse.util;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.avanse.filters.EncryptDecryptFilter;
import com.avanse.filters.EncryptUtil;
import com.avanse.jpa.model.AccountHolderDetails;
import com.avanse.jpa.model.MstSourceMapping;
import com.avanse.model.PennantView;
import com.avanse.model.PennantViewOther;

public class AvanseUtility {
	public static void preparePennantHeader(HttpHeaders pennantHeaderApi, MstSourceMapping source) {
		pennantHeaderApi.setContentType(MediaType.APPLICATION_JSON);
		System.out.println("preparePennantHeader Source Id:" + source.getSourceId());
		System.out.println("preparePennantHeader Secret Key :" + source.getSecretKey());
		String auth = source.getSourceId() + ":" + source.getSecretKey();
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		pennantHeaderApi.add("Authorization", authHeader);
		System.out.println("preparePennantHeader" + 1);
	}
	
	public static HttpEntity<String> preparePennantRequest(JSONObject overdueRequest, HttpHeaders pennantHeaderApi) {
		System.out.println("preparePennantRequest"+1);
		JSONObject inputObject = new JSONObject();
		inputObject.put(EncryptDecryptFilter.ENCRYPTION_KEY, EncryptUtil.encryptString(overdueRequest.toString(), ""));
		HttpEntity<String> request = new HttpEntity<String>(inputObject.toString(), pennantHeaderApi);
		System.out.println("preparePennantRequest"+2);
		return request;
	}
	
	public static void prepareAccountHolderDetailsOther(List<AccountHolderDetails> accountHolderDetailsList, PennantViewOther[] pennantViewArray) {
		for (int i = 0; i < pennantViewArray.length; i++) {
			AccountHolderDetails accountHolderDetails = new AccountHolderDetails();
			accountHolderDetails.setName(pennantViewArray[i].getCustomer_name());
			accountHolderDetails.setAccountName(pennantViewArray[i].getTYPE());
			accountHolderDetails.setEmailId(pennantViewArray[i].getEMAIL_ID());
			accountHolderDetails.setMobileNumber(pennantViewArray[i].getMOBILE_NUMBER());
			accountHolderDetails.setLoanBranch(pennantViewArray[i].getLOAN_BRANCH());
			accountHolderDetails.setAddress(pennantViewArray[i].getADDRESS());
			accountHolderDetails.setLoanType(pennantViewArray[i].getPROD_TYPE());
			accountHolderDetailsList.add(accountHolderDetails);
		}
	}
	
	public static void prepareAccountHolderDetails(List<AccountHolderDetails> accountHolderDetailsList, PennantView[] pennantViewArray) {
		for (int i = 0; i < pennantViewArray.length; i++) {
			AccountHolderDetails accountHolderDetails = new AccountHolderDetails();
			accountHolderDetails.setName(pennantViewArray[i].getCustomer_name());
			accountHolderDetails.setAccountName(pennantViewArray[i].getTYPE());
			accountHolderDetails.setEmailId(pennantViewArray[i].getEMAIL_ID());
			accountHolderDetails.setMobileNumber(pennantViewArray[i].getMOBILE_NUMBER());
			accountHolderDetails.setLoanBranch(pennantViewArray[i].getLOAN_BRANCH());
			accountHolderDetails.setAddress(pennantViewArray[i].getADDRESS());
			accountHolderDetails.setLoanType(pennantViewArray[i].getPROD_TYPE());
			accountHolderDetailsList.add(accountHolderDetails);
		}
	}
}
