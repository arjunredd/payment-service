package com.avanse.consumer;

import org.springframework.http.HttpMethod;



public interface PaymentConsumer {
	//public ResponseEntity<FinanceResponse> callRestTemplateForFeesPayment(String url,FinanceRequest reqDto);
	//public ResponseEntity<Object> callRestTemplateForManualPayment(String url, FinanceRequest reqDto);
	//public ResponseEntity<Object> callRestTemplateForEarlySettlement(String url, FinanceRequest reqDto);
	//public ResponseEntity<Object> callRestTemplatePartialSettlement(String url, FinanceRequest reqDto);
	
	public FinanceResponse callRestTemplateForReceipting(String url, FinanceRequest reqDto,
			String serviceName,HttpMethod httpMethod);
}