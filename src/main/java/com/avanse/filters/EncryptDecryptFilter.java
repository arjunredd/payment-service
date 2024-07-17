
package com.avanse.filters;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.avanse.jpa.model.TrnPaymentRequestHistory;
import com.avanse.jpa.repository.TrnPaymentRequestHistoryRepository;

@Component
@Order(1)
public class EncryptDecryptFilter implements Filter{

	public static final String ENCRYPTION_KEY="encryptedData";
	
	@Autowired
	TrnPaymentRequestHistoryRepository trnPaymentRequestHistoryRepository;

	@Override public void init(FilterConfig filterConfig) throws
	ServletException{

	}

	@Override public void destroy() {

	}

	@Override public void doFilter(ServletRequest request, ServletResponse
			response, FilterChain chain) throws IOException, ServletException {

		InputRequestWrapper wrappedRequest = new InputRequestWrapper((HttpServletRequest) request);
		OutputResponseWrapper bufferedResponse = new OutputResponseWrapper( (HttpServletResponse)response);
		//System.out.println("in do filter....");
		String body=IOUtils.toString(wrappedRequest.getReader());
		//System.out.println("body...  "+body);
		if(!"".equals(body)){

			JSONObject oldJsonObject = new JSONObject(body);

			String encryptedInputData= oldJsonObject.get(ENCRYPTION_KEY).toString();
			//System.out.println(" JSON DATA : "+encryptedInputData);

			String decryptedData=EncryptUtil.decryptString(encryptedInputData,"");
			//System.out.println(" DECRYPTED DATA : "+decryptedData);
			Object requestObj1= new JSONTokener(decryptedData).nextValue();
			if(requestObj1 instanceof JSONObject) { 

				//System.out.println(" INSIDE JSON OBJECT!!!!!!!!!!!!!!!!!!!!!!");
				JSONObject newResponseObject = new JSONObject(decryptedData);
				decryptedData=newResponseObject.toString();
				//System.out.println(" ---------- "+ decryptedData);

			}else if (requestObj1 instanceof JSONArray) {
				//System.out.println(" INSIDE JSON ARRAY !!!!!!!!!!!!!!!");
				JSONArray jsonArray= new JSONArray(decryptedData);
				//System.out.println(" JSON ARRAY : "+ jsonArray);
				decryptedData=jsonArray.toString();
				//System.out.println(" DECRYPTED ARRAY : "+decryptedData);
			}

			System.out.println("decryptedData:"+decryptedData);
			
			wrappedRequest.resetInputStream(decryptedData.getBytes());
			chain.doFilter(wrappedRequest, bufferedResponse);

			String responseData = bufferedResponse.getResponseData();
			System.out.println("responseData:"+responseData);
			
			TrnPaymentRequestHistory trh=new TrnPaymentRequestHistory(decryptedData, responseData, "PAYMENT_REQUEST");
			trnPaymentRequestHistoryRepository.saveAndFlush(trh);
			
			//save decryptedData and responseData here
			
			if(!"".equals(responseData)) {

				//System.out.println("Response Data: "+responseData);
				Object obj= new JSONTokener(responseData).nextValue();
				if(obj instanceof JSONObject) { 
					//System.out.println(" INSIDE JSON RESPONSE OBJECT");
					JSONObject oldResponseObject=new JSONObject(responseData);
					//System.out.println(" OLD RESPONSE : "+oldResponseObject);
					String encryptedData=EncryptUtil.encryptString(oldResponseObject.toString(),"");
					//System.out.println(" ENCRYPTED JSON : "+encryptedData);
					JSONObject	newObj= new JSONObject();
					newObj.put(ENCRYPTION_KEY, encryptedData);
					//System.out.println(" NEW OBJECT TO BE RESPONDED : "+ newObj);
					OutputStream outputStream =response.getOutputStream();
					outputStream.write(newObj.toString().getBytes());
					outputStream.flush();
					outputStream.close();

				}else if (obj instanceof JSONArray) {
					JSONArray jsonArray= new JSONArray(responseData);
					JSONArray returnArray= new JSONArray(); 
					JSONObject jsonObjectEncoded;
					for(int i=0;i<jsonArray.length();i++) {
						jsonObjectEncoded= new JSONObject();
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						for(String key:jsonObject.keySet()) { 
							jsonObjectEncoded.put(key,EncryptUtil.encryptString(jsonObject.get(key).toString(), ""));
						}
						returnArray.put(jsonObjectEncoded); 
					}

					String encryptedData=EncryptUtil.encryptString(responseData.toString(), "");
					JSONObject	newObj= new JSONObject();
					newObj.put(ENCRYPTION_KEY, encryptedData);
					OutputStream outputStream =response.getOutputStream();
					outputStream.write(newObj.toString().getBytes());
					outputStream.flush();
					outputStream.close(); 
				}
			}else {
				System.out.println(" Response Data is Null!!!");
			}

		}
		else { 
			chain.doFilter(wrappedRequest, bufferedResponse);
			String responseData = bufferedResponse.getResponseData();

			if(!"".equals(responseData)) {
				//System.out.println("Response Data: "+responseData);
				Object obj= new JSONTokener(responseData).nextValue();
				if(obj instanceof JSONObject) { 
					//System.out.println(" INSIDE JSON RESPONSE OBJECT");
					JSONObject oldResponseObject=new JSONObject(responseData);
					//System.out.println(" OLD RESPONSE : "+oldResponseObject);
					String encryptedData=EncryptUtil.encryptString(oldResponseObject.toString(),"");
					//System.out.println(" ENCRYPTED JSON : "+encryptedData);
					JSONObject	newObj= new JSONObject();
					newObj.put(ENCRYPTION_KEY, encryptedData);
					//System.out.println(" NEW OBJECT TO BE RESPONDED : "+ newObj);
					OutputStream outputStream =response.getOutputStream();
					outputStream.write(newObj.toString().getBytes());
					outputStream.flush();
					outputStream.close();

				}else if (obj instanceof JSONArray) {
					JSONArray jsonArray= new JSONArray(responseData);
					JSONArray returnArray= new JSONArray(); 
					JSONObject jsonObjectEncoded;
					for(int i=0;i<jsonArray.length();i++) {
						jsonObjectEncoded= new JSONObject();
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						for(String key:jsonObject.keySet()) { 
							jsonObjectEncoded.put(key,EncryptUtil.encryptString(jsonObject.get(key).toString(), ""));
						}
						returnArray.put(jsonObjectEncoded); 
					}

					String encryptedData=EncryptUtil.encryptString(responseData.toString(), "");
					JSONObject	newObj= new JSONObject();
					newObj.put(ENCRYPTION_KEY, encryptedData);
					OutputStream outputStream =response.getOutputStream();
					outputStream.write(newObj.toString().getBytes());
					outputStream.flush();
					outputStream.close(); 
				}
			}else {
			}

		}

	} }
