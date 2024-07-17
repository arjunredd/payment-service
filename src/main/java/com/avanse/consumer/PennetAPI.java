package com.avanse.consumer;

import java.net.URI;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;





@Component
@PropertySource("classpath:application.properties")
public class PennetAPI implements PaymentConsumer {
	
	@Autowired
	private Environment env;

	@Autowired
	HttpHeaderUtility utility;

	private RestTemplate template = new RestTemplate();

	//private HttpHeaders headers = new HttpHeaders();

	ResponseEntity<Object> response = null;
	
	@Override
	public FinanceResponse callRestTemplateForReceipting(String url, FinanceRequest reqDto,String serviceName,HttpMethod httpMethod) {	
	
		FinanceResponse resp=new FinanceResponse();
		
		try {
			
			String lanNo=(reqDto!=null && reqDto.getFinReference()!=null)?reqDto.getFinReference():"systemDate";
			
			System.out.println(".......while hiting to url......"+serviceName+" service....for lanNo:"+lanNo);
			HttpHeaders headersData = utility.setAllHeader();
			headersData.set("SERVICENAME", env.getProperty(serviceName));
			HttpEntity<FinanceRequest> reqEntity = new HttpEntity<FinanceRequest>(reqDto, headersData);
		
			response = template.exchange(new URI(url), httpMethod, reqEntity, Object.class);
			
			Object respBody1 = response.getBody();
			
			System.out.println("Response body for lanNo:"+lanNo+":"+response.getBody());
			LinkedHashMap<String, LinkedHashMap<String, String>> res1 = (LinkedHashMap<String, LinkedHashMap<String, String>>) respBody1;
			
			
			for (String key : res1.keySet()) {
				
				switch(key) {
				
				case "returnStatus": 
					ReturnStatus stus=new ReturnStatus();
					LinkedHashMap<String, String> returnObj = res1.get(key); 
				
				for (String key2 : returnObj.keySet()) {
					
					switch(key2) {
					case "returnCode":
						stus.setRetrunCode(returnObj.get(key2));
						break;
					case "returnText":
						stus.setReturntext(returnObj.get(key2)); 
						break;
					}
				  
				}
				  resp.setReturnStatus(stus); 
				
				break;
				
				case "stp":
					Object stp = res1.get(key);
					resp.setStp((boolean)stp);
					
					break;
					
				case "receiptId":
					
					Object receiptId = res1.get(key);
					resp.setReceiptId((int)receiptId);
					
					break;
					
				case "appDate":
					Object appDate=res1.get(key);
					resp.setAppDate((String)appDate);
					break;
					
				case "valueDate":
					Object valueDate=res1.get(key);
					resp.setAppDate((String)valueDate);
					break;
				}
			
			}
			
			} catch (Exception e) {
			System.out.println("Receipt service method exception");
//			log.info("Receipt service method exception");
			e.printStackTrace();
//			log.info(e.getMessage());
			resp.setReturnStatus(new ReturnStatus("Exception", e.getLocalizedMessage()));
			return resp;
		}
		//return response;
		return resp;
	}

}
