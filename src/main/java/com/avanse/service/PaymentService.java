package com.avanse.service;

import org.springframework.http.HttpEntity;

import com.avanse.jpa.model.ApplicantDetailsResponse;
import com.avanse.jpa.model.MstSourceMapping;
import com.avanse.jpa.model.PrePaymentRequest;
import com.avanse.jpa.model.TrnFeePayment;
import com.avanse.jpa.model.TrnPaymentRequest;
import com.avanse.model.PennantView;
import com.avanse.model.PennantViewOther;

public interface PaymentService {

	ApplicantDetailsResponse getDetailsAgainstLoanNumber(TrnPaymentRequest trnPaymentRequest);

	TrnPaymentRequest paymentProcessing(TrnPaymentRequest trnPaymentRequest, PrePaymentRequest prePaymentRequest);

	String prepareShortURLAndSmsEmail(TrnPaymentRequest trnPaymentRequest, PrePaymentRequest prePaymentRequest, Long requestId);

	TrnPaymentRequest getRequestDetailsPartPayments(TrnPaymentRequest trnPaymentRequest);

	TrnPaymentRequest paymentProcessingForFees(TrnPaymentRequest trnPaymentRequest, PrePaymentRequest prePaymentRequest);

	TrnFeePayment saveTrnFeePaymentDetails(TrnPaymentRequest trnPaymentRequest, int requestId, String bigURL, String shortURL);

	void saveFeeCodeDetails(TrnPaymentRequest trnPaymentRequest, TrnFeePayment trnFeePayment);

	void sendSMSandEmailForFess(TrnPaymentRequest trnPaymentRequest, TrnPaymentRequest tpr, String shortURL, TrnFeePayment trnFeePayment);

	ApplicantDetailsResponse getDetailsAgainstLoanNumberForFee(TrnPaymentRequest trnPaymentRequest);

	PennantViewOther[] fetchFeeDetailsFromPennant(TrnPaymentRequest dbTrnPaymenyRequest, HttpEntity<String> pennantRequest);

	PennantView[] fetchPennantArray(TrnPaymentRequest trnPaymentRequest, HttpEntity<String> request);

	String fetchCustomerName(TrnPaymentRequest trnPaymentRequest, MstSourceMapping source);
}
