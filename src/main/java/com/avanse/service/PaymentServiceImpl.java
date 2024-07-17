package com.avanse.service;

import static com.avanse.utility.UtilityClass.isEmptyString;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.avanse.consumer.FinanceResponse;
import com.avanse.consumer.PennetAPI;
import com.avanse.exception.AppException;
import com.avanse.filters.EncryptUtil;
import com.avanse.jpa.model.AccountHolderDetails;
import com.avanse.jpa.model.ApplicantDetailsResponse;
import com.avanse.jpa.model.MstFundAccountMapping;
import com.avanse.jpa.model.MstSourceMapping;
import com.avanse.jpa.model.PrePaymentRequest;
import com.avanse.jpa.model.TrnFeeDetails;
import com.avanse.jpa.model.TrnFeePayment;
import com.avanse.jpa.model.TrnPayment;
import com.avanse.jpa.model.TrnPaymentRequest;
import com.avanse.jpa.repository.MstFundAccountMappingRepository;
import com.avanse.jpa.repository.MstSourceMappingRepository;
import com.avanse.jpa.repository.TrnFeeDetailsMappingRepository;
import com.avanse.jpa.repository.TrnFeePaymentRepository;
import com.avanse.jpa.repository.TrnPaymentRepository;
import com.avanse.jpa.repository.TrnPaymentRequestDetailsRepository;
import com.avanse.jpa.repository.TrnPaymentRequestRepository;
import com.avanse.model.EncryptDataPojo;
import com.avanse.model.PennantView;
import com.avanse.model.PennantViewOther;
import com.avanse.util.AvanseUtility;
import com.avanse.utility.UtilityClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Value("${getLoanDetails}")
	private String loanDetailsAPI;

	@Value("${getLoanDetailsOthers}")
	private String loanDetailsAPIForFee;

	@Value("${isValidLANApi}")
	private String isValidLANApi;

	@Value("${getSystemDate}")
	private String getSystemDate_url;

	@Value("${overDueApi}")
	private String overDueApi;

	@Value("${bigPaymentURL}")
	private String bigPaymentURL;

	@Value("${emailSubject}")
	private String subject;

	@Autowired
	MstSourceMappingRepository mstSourceMappingRepository;

	@Autowired
	MstFundAccountMappingRepository mstFundAccountMappingRepository;

	@Autowired
	private TrnPaymentRequestRepository trnPaymentRequestRepository;

	@Autowired
	private TrnPaymentRepository trnPaymentRepository;

	@Autowired
	private TrnPaymentRequestDetailsRepository trnPaymentRequestDetailsRepository;

	@Autowired
	private TrnFeeDetailsMappingRepository trnFeeDetailsMappingRepository;

	@Autowired
	private TrnFeePaymentRepository trnFeePaymentRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PennetAPI pennetAPI;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private ServiceOperation serviceOperation;

	private static final SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public ApplicantDetailsResponse getDetailsAgainstLoanNumber(TrnPaymentRequest trnPaymentRequest) {
		ApplicantDetailsResponse applicantDetailsResponse = new ApplicantDetailsResponse();
		List<AccountHolderDetails> accountHolderDetailsList = new ArrayList<>();
		HttpHeaders pennantHeaderApi = new HttpHeaders();
		System.out.println("getDetailsAgainstLoanNumber" + 1 + " Source id:" + trnPaymentRequest.getSourceId());
		MstSourceMapping source = fetchSourceFromDB(trnPaymentRequest);
		System.out.println("getDetailsAgainstLoanNumber" + 2);
		AvanseUtility.preparePennantHeader(pennantHeaderApi, source);
		System.out.println("getDetailsAgainstLoanNumber" + 3);
		JSONObject overdueRequest = new JSONObject();
		overdueRequest.put("loanNo", trnPaymentRequest.getLaNumber());
		HttpEntity<String> pennantRequest = AvanseUtility.preparePennantRequest(overdueRequest, pennantHeaderApi);
		validateLoanNumber(pennantRequest);
		System.out.println("getDetailsAgainstLoanNumber" + 4);
		PennantView[] pennantViewArray = fetchPennantArray(trnPaymentRequest, pennantRequest);
		System.out.println("getDetailsAgainstLoanNumber" + 5);
		AvanseUtility.prepareAccountHolderDetails(accountHolderDetailsList, pennantViewArray);
		updatePennantDetailsInResponse(applicantDetailsResponse, pennantViewArray);
		if (!trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("OVERDUE")) {
			applicantDetailsResponse.setAmount(0);
		}
		applicantDetailsResponse.setAccountHolderDetailsList(accountHolderDetailsList);
		System.out.println("getDetailsAgainstLoanNumber" + 6);
		return applicantDetailsResponse;
	}

	private void updatePennantDetailsInResponse(ApplicantDetailsResponse applicantDetailsResponse, PennantView[] pennantViewArray) {
		double amount;
		for (PennantView pennantView : pennantViewArray) {
			if (pennantView.getTYPE().equalsIgnoreCase("APPLICANT")) {
				if (pennantView.getEMI_SI_PI_AMOUNT() == 0) {
					amount = pennantView.getCURRENT_MONTH_INST() + pennantView.getBOUNCE_CHARGE() + pennantView.getPENAL_CHARGE();
				} else {
					amount = pennantView.getEMI_SI_PI_AMOUNT() + pennantView.getBOUNCE_CHARGE() + pennantView.getPENAL_CHARGE();
				}
				applicantDetailsResponse.setAmount(amount);
				applicantDetailsResponse.setLoanType(pennantView.getPROD_TYPE());
			}

		}
	}

	private MstSourceMapping fetchSourceFromDB(TrnPaymentRequest trnPaymentRequest) {
		Optional<MstSourceMapping> optional = mstSourceMappingRepository.findBySourceId(trnPaymentRequest.getSourceId());
		System.out.println("fetchSourceFromDB" + 1);
		if (!optional.isPresent()) {
			throw new AppException("Source Id Not Available");
		}
		MstSourceMapping source = optional.get();
		System.out.println("fetchSourceFromDB" + 2);
		return source;
	}

	private JSONObject validateLoanNumber(HttpEntity<String> request) {
		System.out.println("validateLoanNumber isValidaLANAPI:" + isValidLANApi);
		ResponseEntity<EncryptDataPojo> resp = restTemplate.postForEntity(isValidLANApi, request, EncryptDataPojo.class);
		System.out.println("validateLoanNumber" + 1);
		EncryptDataPojo encryptDataPojo = resp.getBody();
		System.out.println("validateLoanNumber" + 2);
		String decryptString = EncryptUtil.decryptString(encryptDataPojo.getEncryptedData(), "");
		System.out.println("validateLoanNumber" + 3);
		JSONObject jsonObject = new JSONObject(decryptString);
		System.out.println("validateLoanNumber" + 4);
		boolean isValidLAN = jsonObject.getBoolean("valid");
		System.out.println("validateLoanNumber" + 5 + " flag:" + isValidLAN);
		if (isValidLAN == false) {
			throw new AppException("Invalid LAN Number");
		}
		return jsonObject;
	}

	@Override
	public PennantView[] fetchPennantArray(TrnPaymentRequest trnPaymentRequest, HttpEntity<String> request) {
		System.out.println("preparePennantArray loan API: " + loanDetailsAPI);
		ResponseEntity<EncryptDataPojo> loanDetails = restTemplate.postForEntity(loanDetailsAPI, request, EncryptDataPojo.class);
		System.out.println("preparePennantArray" + 1);
		EncryptDataPojo pojo = loanDetails.getBody();
		System.out.println("preparePennantArray" + 2);
		String decryptedLoanDetails = EncryptUtil.decryptString(pojo.getEncryptedData(), "");
		System.out.println("preparePennantArray" + 3);
		ObjectMapper mapper = new ObjectMapper();
		PennantView[] pennantViewArray = null;
		try {
			pennantViewArray = mapper.readValue(decryptedLoanDetails, PennantView[].class);
			System.out.println("preparePennantArray" + 4);
		} catch (JsonProcessingException e) {
			System.out.println("preparePennantArray" + 5);
			e.printStackTrace();
		}

		if (pennantViewArray == null || pennantViewArray.length == 0) {
			System.out.println("preparePennantArray" + 6);
			throw new AppException("No Customr Details Found Against Loan No:" + trnPaymentRequest.getLaNumber());
		}
		return pennantViewArray;
	}

	@Override
	@Transactional
	public TrnPaymentRequest paymentProcessing(TrnPaymentRequest trnPaymentRequest, PrePaymentRequest prePaymentRequest) {
		System.out.println("paymentProcessing" + 1);
		validateRequest(prePaymentRequest);
		System.out.println("paymentProcessing" + 2);
		MstSourceMapping source = validateSourceId(prePaymentRequest);
		System.out.println("paymentProcessing" + 3);
		HttpHeaders pennantHeaderApi = new HttpHeaders();
		AvanseUtility.preparePennantHeader(pennantHeaderApi, source);
		System.out.println("paymentProcessing" + 4);
		JSONObject overdueRequest = new JSONObject();
		overdueRequest.put("loanNo", prePaymentRequest.getLaNumber());
		HttpEntity<String> pennantRequest = AvanseUtility.preparePennantRequest(overdueRequest, pennantHeaderApi);
		System.out.println("paymentProcessing" + 5);
		JSONObject validLoanResponse = validateLoanNumber(pennantRequest);
		System.out.println("paymentProcessing" + 6);
		validateRequestDateAgainstPennantSysDate(prePaymentRequest, trnPaymentRequest);
		System.out.println("paymentProcessing" + 7);
		if (isEmptyString(prePaymentRequest.getLoanType())) {
			PennantView pennantView = null;
			pennantView = fetchPennantViewfromAPI(pennantRequest, pennantView);
			if (pennantView != null) {
				System.out.println("paymentProcessing" + 8);
				trnPaymentRequest.setLoanType(pennantView.getPROD_TYPE());
			}
			System.out.println("paymentProcessing" + 9);
		}
		validateLoanType(trnPaymentRequest);
		System.out.println("paymentProcessing" + 10);
		trnPaymentRequest.setAmount(prePaymentRequest.getAmount());
		trnPaymentRequest.setTotalAmount(prePaymentRequest.getTotalAmount());
		trnPaymentRequest.setRequestStatus("NEW_REQUEST");
		trnPaymentRequest.setPaymentRequestJson(UtilityClass.convertObjectToJsonString(prePaymentRequest));
		trnPaymentRequest.setOverdueAPIRequestJson(overdueRequest.toString());
		trnPaymentRequest.setOverdueAPIResponseJson(validLoanResponse.toString());
		TrnPaymentRequest response = trnPaymentRequestRepository.save(trnPaymentRequest);
		System.out.println("paymentProcessing" + 11);

		if (response == null) {
			throw new AppException("Payment request ID is not generated");
		}
		return trnPaymentRequest;
	}

	private void sendPaymentEmail(TrnPaymentRequest trnPaymentRequest, String shortURL, TrnPayment trnPayment, AccountHolderDetails accountHolderDetails) {
		if (!isEmptyString(accountHolderDetails.getEmailId())) {
			String paymentPurpose = trnPaymentRequest.getPaymentPurpose().toUpperCase();
			String emailContent = null;
			if(paymentPurpose.equals("OVERDUE")) {
				emailContent = getOverdueEmailContent(shortURL, trnPaymentRequest);
			}else if(paymentPurpose.equals("PARTPAYMENT") || paymentPurpose.equals("FORECLOSURE")) {
				emailContent = getEmailContentForPartpaymentAndForeClosure(shortURL, trnPaymentRequest);
			}else if(paymentPurpose.equals("FEES")) {
				emailContent = getEmailContentForFees(shortURL, trnPaymentRequest);
			}
			String purpose = trnPaymentRequest.getPaymentPurpose().toUpperCase();
			purpose = purpose.startsWith("OVERDUE") ? "Overdue" : trnPaymentRequest.getPaymentPurpose();
			String subject1 = subject.replace("<PAYMENT_PURPOSE>", purpose);
			emailService.sendEmail(accountHolderDetails.getName(), accountHolderDetails.getEmailId(), subject1, emailContent, trnPaymentRequest.getPaymentRequestId(), trnPaymentRequest.getPaymentPurpose(), accountHolderDetails.getAccountName());
			System.out.println("45");
		}
	}

	private String getEmailContentForFees(String shortURL, TrnPaymentRequest trnPaymentRequest) {
		String content = "Dear Customer, Thank you for choosing Avanse Financial Services as your preferred education loan partner."
				+ " Please click on the link below to pay charges towards your loan account number "+trnPaymentRequest.getLaNumber()+". \r\n" + 
				"\r\n" + 
				shortURL+"\r\n" + 
				"\r\n" + 
				"In case of any queries regarding associated charges or issues while making the payment please reach out to your relationship manager. \r\n" + 
				"\r\n" + 
				"Regards \r\n" + 
				"Team Avanse\r\n" + 
				"";
		return content;
	}

	private String getEmailContentForPartpaymentAndForeClosure(String shortURL, TrnPaymentRequest trnPaymentRequest) {
		String content = "Dear Customer, \r\n" + 
				"We have registered your request for "+trnPaymentRequest.getRequestPurpose()+" towards you Loan No "+trnPaymentRequest.getLaNumber()
				+" with Avanse Financial Services. Make your payments in just 3 clicks, use the link below to make your payments from a wide range of digital payment options. \r\n" + 
				"\r\n" + 
				shortURL+"\r\n" + 
				"\r\n" + 
				"Please write to us with your payment reference no on wecare@avanse.com after successful completion of the payment. Please allow us 15 days to send you <revised amort chart | NOC> to your registered email address.  \r\n" + 
				"\r\n" + 
				"Regards \r\n" + 
				"Team Avanse\r\n" + 
				"";
		return content;
	}

	private String getOverdueEmailContent(String shortURL, TrnPaymentRequest trnPaymentRequest) {
		String content = "Dear Customer, An Instalment amount is due toward your Loan No. "+trnPaymentRequest.getLaNumber()+" with Avanse Financial Service. Make your payments in just 3 clicks, use the link below to make your payments from a wide range of digital payment options\r\n" + 
				"\r\n" + 
				shortURL+"\r\n" + 
				" \r\n" + 
				"In case of any queries please write to us on wecare@avanse.com \r\n" + 
				"\r\n" + 
				"Regards \r\n" + 
				"Team Avanse\r\n";
		return content;
	}

	private void sendPaymentSMS(PrePaymentRequest prePaymentRequest, TrnPaymentRequest trnPaymentRequest, String shortURL, 
			TrnPayment trnPayment, AccountHolderDetails accountHolderDetails) {
		if (!isEmptyString(accountHolderDetails.getMobileNumber())) {
			System.out.println("sendPaymentSMS" + 1);
			String smsContents = null;
			String paymentPurpose = trnPaymentRequest.getPaymentPurpose().toUpperCase();
			if(paymentPurpose.equals("OVERDUE")) {
				smsContents = getOverdueSMSContent(shortURL, trnPaymentRequest);
			}else if(paymentPurpose.equals("PARTPAYMENT") || paymentPurpose.equals("FORECLOSURE")) {
				smsContents = getSMSContentForPartpaymentAndForeClosure(shortURL, trnPaymentRequest);
			}else if(paymentPurpose.equals("FEES")) {
				smsContents = getSMSContentForFees(shortURL, trnPaymentRequest);
			}
			smsService.sendSMS(smsContents, accountHolderDetails.getMobileNumber(), accountHolderDetails.getName(), trnPaymentRequest.getPaymentRequestId(), trnPaymentRequest.getPaymentPurpose(), accountHolderDetails.getAccountName());
			System.out.println("sendPaymentSMS" + 2);
		}
		System.out.println("sendPaymentSMS" + 3);
	}

	private String getSMSContentForPartpaymentAndForeClosure(String shortURL, TrnPaymentRequest trnPaymentRequest) {
		String content = "Dear Customer, Thank you for your request "+trnPaymentRequest.getPaymentPurpose()+" towards you Loan No "+trnPaymentRequest.getLaNumber()
		+" with Avanse Financial Services. Make your payments in just 3 clicks, use the link below to make your payments from a wide range of digital payment options.\r\n" + 
				" \r\n" + 
				shortURL+"\r\n" + 
				"\r\n" + 
				"Please write to us with your payment reference no on wecare@avanse.com after successful completion of the payment. Please allow us 15 days to send you <revised amort chart | NOC> to your registered email address.  \r\n" + 
				"\r\n" + 
				"Regards \r\n" + 
				"Team Avanse\r\n";
		return content;
	}

	private String getSMSContentForFees(String shortURL, TrnPaymentRequest trnPaymentRequest) {
		String content = "Dear Customer, Thank you for choosing Avanse Financial Services as your preferred education loan partner. Please click on the link below to pay charges towards your loan account number "+trnPaymentRequest.getLaNumber()+". \r\n" + 
				"\r\n" + 
				shortURL+" \r\n" + 
				"\r\n" + 
				"In case of any queries regarding associated charges or issues while making the payment please reach out to your relationship manager. \r\n" + 
				"\r\n" + 
				"Regards \r\n" + 
				"Team Avanse \r\n";
		return content;
	}

	private String getOverdueSMSContent(String shortURL, TrnPaymentRequest trnPaymentRequest) {
		String content = "Dear Customer, An Instalment amount is due toward your Loan No. "+trnPaymentRequest.getLaNumber()+" with Avanse Financial Service. Make your payments in just 3 clicks, use the link below to make your payments from a wide range of digital payment options\r\n" + 
				"\r\n" + 
				shortURL+" \r\n" + 
				"\r\n" + 
				"In case of any queries please write to us on wecare@avanse.com \r\n" + 
				"\r\n" + 
				"Regards \r\n" + 
				"Team Avanse";
		return content;
	}

	private MstSourceMapping validateSourceId(PrePaymentRequest prePaymentRequest) {
		System.out.println("validateSourceId" + 1);
		Optional<MstSourceMapping> source = mstSourceMappingRepository.findBySourceId(prePaymentRequest.getSourceId());
		System.out.println("validateSourceId" + 2);
		if (!source.isPresent()) {
			System.out.println("validateSourceId" + 3);
			throw new AppException("SourceId not available in master for generation of token for pennant api");
		}
		return source.get();
	}

	private void validateLoanType(TrnPaymentRequest trnPaymentRequest) {
		System.out.println("validateLoanType" + 1);
		if (trnPaymentRequest.getLoanType() != null) {
			System.out.println("validateLoanType" + 2);
			boolean fundAccExists = mstFundAccountMappingRepository.existsByLoanTypeAndPaymentPurpose(trnPaymentRequest.getLoanType(), trnPaymentRequest.getPaymentPurpose());
			System.out.println("validateLoanType" + 3);
			if (!fundAccExists) {
				throw new AppException("Fund Account mapping not available in database master against Loan Type:" + trnPaymentRequest.getLoanType() + " and PaymentPurpose:" + trnPaymentRequest.getPaymentPurpose());
			}
		}
	}

	private PennantView fetchPennantViewfromAPI(HttpEntity<String> request, PennantView pennantView) {
		System.out.println("fetchPennantViewfromAPI" + 1);
		ResponseEntity<EncryptDataPojo> loanDetails = restTemplate.postForEntity(overDueApi, request, EncryptDataPojo.class);
		System.out.println("fetchPennantViewfromAPI" + 2);
		EncryptDataPojo pojo = loanDetails.getBody();
		System.out.println("fetchPennantViewfromAPI" + 3);
		String decryptedLoanDetails = EncryptUtil.decryptString(pojo.getEncryptedData(), "");
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("fetchPennantViewfromAPI" + 4);
			pennantView = mapper.readValue(decryptedLoanDetails, PennantView.class);
			System.out.println("fetchPennantViewfromAPI" + 5);
			System.out.println("Pennant View:" + pennantView);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		return pennantView;
	}

	private void validateRequestDateAgainstPennantSysDate(PrePaymentRequest prePaymentRequest, TrnPaymentRequest trnPaymentRequest) {
		try {
			System.out.println("validateRequestDateAgainstPennantSysDate" + 1);
			FinanceResponse pennantSystemDate = pennetAPI.callRestTemplateForReceipting(getSystemDate_url, null, "systemDateService", HttpMethod.GET);
			System.out.println("validateRequestDateAgainstPennantSysDate" + 2);
			String strPennantDate = pennantSystemDate.getAppDate();
			System.out.println("appDate:" + strPennantDate);
			System.out.println("Payment Request Date:" + prePaymentRequest.getPaymentRequestDateTime());
			Date pennetDate = SDF_YYYY_MM_DD.parse(strPennantDate);
			String strDate = SDF_YYYY_MM_DD.format(prePaymentRequest.getPaymentRequestDateTime());
			Date date = SDF_YYYY_MM_DD.parse(strDate);
			long dateDiff = (date.getTime() - pennetDate.getTime()) / 86400000;
			System.out.println("PennetDate:" + pennetDate + "***SystemDate:" + date + "***Date Diff:" + dateDiff);
			if (dateDiff < -29) {
				throw new AppException("Payment is not able to processes due Pennet Date policy");
			} else if (dateDiff < 0) {
			} else {
				trnPaymentRequest.setPaymentRequestDateTime(pennetDate);
			}
			System.out.println("PennetDate:" + pennetDate + "***SystemDate:" + date + "***Date Diff:" + dateDiff);
		} catch (ParseException e1) {
			throw new AppException("Exception getting system date:" + e1.getMessage());
		}
	}

	private void validateRequest(PrePaymentRequest prePaymentRequest) {
		if (prePaymentRequest.getSourceId() == null) {
			throw new AppException("Source Id is missing");
		}
		if (isEmptyString(prePaymentRequest.getLaNumber())) {
			throw new AppException("Loan Number Missing");
		}

		if (isEmptyString(prePaymentRequest.getPaymentPurpose())) {
			throw new AppException("Payment Purpose Missing");
		}

		if (prePaymentRequest.getAmount() <= 0) {
			throw new AppException("Amount should be greater than 0");
		}

		if (prePaymentRequest.getReceipents() == null || prePaymentRequest.getReceipents().isEmpty()) {
			throw new AppException("Receipent List is empty");
		}

		if (UtilityClass.isEmptyString(prePaymentRequest.getCrmSrNo())) {
			throw new AppException("CRM Sr No is missing");
		}
	}

	private void validateRequestForFees(PrePaymentRequest prePaymentRequest) {
		if (prePaymentRequest.getSourceId() == null) {
			throw new AppException("Source Id is missing");
		}
		if (isEmptyString(prePaymentRequest.getLaNumber())) {
			throw new AppException("Loan Number Missing");
		}

		if (isEmptyString(prePaymentRequest.getPaymentPurpose())) {
			throw new AppException("Payment Purpose Missing");
		}

		if (prePaymentRequest.getAmount() <= 0) {
			throw new AppException("Amount should be greater than 0");
		}

		if (prePaymentRequest.getReceipents() == null || prePaymentRequest.getReceipents().isEmpty()) {
			throw new AppException("Receipent List is empty");
		}

		if (prePaymentRequest.getFeesList() == null || prePaymentRequest.getFeesList().isEmpty()) {
			throw new AppException("Fees list is empty. Kindly add one or more fee code and Amount");
		}

//		if (UtilityClass.isEmptyString(prePaymentRequest.getCrmSrNo())) {
//			throw new AppException("CRM Sr No is missing");
//		}

	}

	private TrnPayment saveTrnPaymentDetails(TrnPaymentRequest request, long requestId, String bigURL, String shortURL) {
		TrnPayment trnPayment = new TrnPayment();
		try {
			trnPayment.setPaymentRequestId(requestId);
			trnPayment.setUniqueReferenceNumber(request.getUniqueReferenceNumber());
			trnPayment.setLanNumber(request.getLaNumber());
			trnPayment.setSourceId(request.getSourceId());
			trnPayment.setMode(request.getMode());
			trnPayment.setTotalAmount(request.getAmount());
			trnPayment.setBigLink(bigURL);
			trnPayment.setShortLink(shortURL);
			trnPayment.setUserId(request.getUserId());
			trnPayment.setPaymentPurpose(request.getPaymentPurpose());
			trnPayment.setCustomerName(request.getCustomerName());
			trnPayment.setMobileNumber(request.getMobileNo());
			trnPayment.setEmailId(request.getEmailId());
			trnPayment.setAddress(request.getAddress());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			System.out.println(exceptionAsString);
		}
		trnPaymentRepository.save(trnPayment);
		return trnPayment;
	}

	@Override
	public String prepareShortURLAndSmsEmail(TrnPaymentRequest trnPaymentRequest, PrePaymentRequest prePaymentRequest, Long requestId) {
		trnPaymentRequest.setPaymentRequestId(requestId.intValue());
		String bigURL = bigPaymentURL.replace("<REQUEST_ID>", requestId + "").replace("<PAYMENT_PURPOSE>", trnPaymentRequest.getPaymentPurpose().trim());
		System.out.println("prepareShortURLAndSmsEmail Big URL:" + bigURL);
		String shortURL = serviceOperation.generateTinyURL(bigURL, prePaymentRequest.getLaNumber());
		System.out.println("prepareShortURLAndSmsEmail Short URL:" + shortURL);
		trnPaymentRequest.setPaymentRequestId(requestId.intValue());
		TrnPayment trnPayment = saveTrnPaymentDetails(trnPaymentRequest, requestId, bigURL, shortURL);
		System.out.println("prepareShortURLAndSmsEmail" + 2);
		if (prePaymentRequest.getReceipents() != null && !prePaymentRequest.getReceipents().isEmpty()) {
			for (AccountHolderDetails accountHolderDetails : prePaymentRequest.getReceipents()) {
				System.out.println("prepareShortURLAndSmsEmail" + 3);
				sendPaymentSMS(prePaymentRequest, trnPaymentRequest, shortURL, trnPayment, accountHolderDetails);
				System.out.println("prepareShortURLAndSmsEmail" + 4);
				sendPaymentEmail(trnPaymentRequest, shortURL, trnPayment, accountHolderDetails);
				System.out.println("prepareShortURLAndSmsEmail" + 5);
			}
		}
		return shortURL;
	}

	@Override
	public TrnPaymentRequest getRequestDetailsPartPayments(TrnPaymentRequest trnPaymentRequest) {
		System.out.println("getRequestDetailsPartPayments" + 1);
		if (trnPaymentRequest.getPaymentRequestId() == 0)
			throw new AppException("Invalid Input PaymentRequestId.");

		Optional<TrnPaymentRequest> response = trnPaymentRequestRepository.findById(trnPaymentRequest.getPaymentRequestId());
		System.out.println("getRequestDetailsPartPayments" + 2);
		if (!response.isPresent()) {
			throw new AppException("Payment id does not exist");
		}
		TrnPaymentRequest dbTrnPaymentRequest = response.get();
		if ("PAYMENT_COMPLETED".equals(dbTrnPaymentRequest.getRequestStatus())) {
			throw new AppException("Payment alredy completed for given request id!!!");
		}

//		Double allowed_amt = null;
		dbTrnPaymentRequest.setCharges(trnPaymentRequestDetailsRepository.findAllByTrnPaymentRequest(dbTrnPaymentRequest));
		System.out.println("getRequestDetailsPartPayments" + 3);

		Optional<MstFundAccountMapping> mfam = mstFundAccountMappingRepository.findByLoanTypeAndPaymentPurpose(dbTrnPaymentRequest.getLoanType(), dbTrnPaymentRequest.getPaymentPurpose());
		System.out.println("getRequestDetailsPartPayments" + 4);
		if (mfam.isPresent()) {
			System.out.println("getRequestDetailsPartPayments" + 5);
			dbTrnPaymentRequest.setMerchantId(mfam.get().getMerchantID());
			dbTrnPaymentRequest.setRazorPayKey(mfam.get().getRazorPayKey());
		}
		HttpHeaders pennantHeaderApi = new HttpHeaders();
		MstSourceMapping source = fetchSourceFromDB(dbTrnPaymentRequest);
		dbTrnPaymentRequest.setSourceAppName(source.getSourceAppName());
		AvanseUtility.preparePennantHeader(pennantHeaderApi, source);

		JSONObject overdueRequest = new JSONObject();
		overdueRequest.put("loanNo", dbTrnPaymentRequest.getLaNumber());
		HttpEntity<String> pennantRequest = AvanseUtility.preparePennantRequest(overdueRequest, pennantHeaderApi);

		if (dbTrnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("PARTPAYMENT")) {
			dbTrnPaymentRequest.setPaymentName("Part Payment Amount");
			fetchLoanDetails(dbTrnPaymentRequest, pennantRequest);
		} else if (dbTrnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("FORECLOSURE")) {
			dbTrnPaymentRequest.setPaymentName("Fore Closure Amount");
			fetchLoanDetails(dbTrnPaymentRequest, pennantRequest);
		}
		System.out.println("getRequestDetailsPartPayments" + 8);
		return dbTrnPaymentRequest;

	}

	private void fetchLoanDetails(TrnPaymentRequest dbTrnPaymentRequest, HttpEntity<String> pennantRequest) {
		try {
			ResponseEntity<EncryptDataPojo> entity = restTemplate.postForEntity(overDueApi, pennantRequest, EncryptDataPojo.class);
			System.out.println("getRequestDetailsPartPayments" + 5);
			EncryptDataPojo encryptedDataPojo = entity.getBody();
			String decryptString = EncryptUtil.decryptString(encryptedDataPojo.getEncryptedData(), "");
			ObjectMapper mapper = new ObjectMapper();
			PennantView pennantView = null;
			try {
				pennantView = mapper.readValue(decryptString, PennantView.class);
				System.out.println("getRequestDetailsPartPayments" + 6);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}

			if (pennantView != null) {
				System.out.println("getRequestDetailsPartPayments" + 7);
				dbTrnPaymentRequest.setCustomerName(pennantView.getCustomer_name());
				dbTrnPaymentRequest.setLoanBranch(pennantView.getLOAN_BRANCH());
				dbTrnPaymentRequest.setMobileNo(pennantView.getMOBILE_NUMBER());
				dbTrnPaymentRequest.setEmailId(pennantView.getEMAIL_ID());
				dbTrnPaymentRequest.setAddress(pennantView.getADDRESS());
			}

		} catch (RestClientException e) {
			throw new AppException("Unable to connect to ELMSPennantViews Api");
		}
	}

	@Override
	public TrnPaymentRequest paymentProcessingForFees(TrnPaymentRequest trnPaymentRequest, PrePaymentRequest prePaymentRequest) {
		System.out.println("paymentProcessing" + 1);
		validateRequestForFees(prePaymentRequest);
		System.out.println("paymentProcessing" + 2);
		validateSourceId(prePaymentRequest);
		System.out.println("paymentProcessing" + 3);
		validateRequestDateAgainstPennantSysDate(prePaymentRequest, trnPaymentRequest);
		System.out.println("paymentProcessing" + 7);

//		List<AccountHolderDetails> accountHolderDetailsList = new ArrayList<>();
//		updateFeeDetails(trnPaymentRequest, accountHolderDetailsList);
		validateLoanType(trnPaymentRequest);

		System.out.println("paymentProcessing" + 10);
		trnPaymentRequest.setRequestStatus("NEW_REQUEST");
		trnPaymentRequest.setPaymentRequestJson(UtilityClass.convertObjectToJsonString(prePaymentRequest));
		trnPaymentRequest.setAmount(prePaymentRequest.getAmount());
		trnPaymentRequest.setTotalAmount(prePaymentRequest.getTotalAmount());
		trnPaymentRequest.setPaymentRequestJson(UtilityClass.convertObjectToJsonString(prePaymentRequest));
		trnPaymentRequest.setRemarks("Agent Remark");
		TrnPaymentRequest response = trnPaymentRequestRepository.save(trnPaymentRequest);
		saveTrnFeeDetails(prePaymentRequest, response);

		System.out.println("paymentProcessing" + 11);
		if (response == null) {
			throw new AppException("Payment request ID is not generated");
		}
		return response;
	}

	private void saveTrnFeeDetails(PrePaymentRequest prePaymentRequest, TrnPaymentRequest trnPaymentRequest) {
		List<TrnFeeDetails> trnFeeDetails = prePaymentRequest.getFeesList();
		if (trnFeeDetails != null && !trnFeeDetails.isEmpty()) {
			for (TrnFeeDetails obj : trnFeeDetails) {
				obj.setPaymentRequestId(trnPaymentRequest.getPaymentRequestId());
				trnFeeDetailsMappingRepository.save(obj);
			}
		}
	}

	@Override
	public TrnFeePayment saveTrnFeePaymentDetails(TrnPaymentRequest request, int requestId, String bigURL, String shortURL) {
		TrnFeePayment trnFeePayment = new TrnFeePayment();
		try {
			trnFeePayment.setPaymentRequestId(requestId + "");
			trnFeePayment.setUniqueReferenceNumber(request.getUniqueReferenceNumber());
			trnFeePayment.setLanNumber(request.getLaNumber());
			trnFeePayment.setSourceId(request.getSourceId());
			trnFeePayment.setMode(request.getMode());
			trnFeePayment.setTotalAmount(request.getAmount());
			trnFeePayment.setEmailId(request.getEmailId());
			trnFeePayment.setMobileNumber(request.getMobileNo());
			trnFeePayment.setCustomerName(request.getCustomerName());
			trnFeePayment.setCif(request.getCif());
			trnFeePayment.setBigLink(bigURL);
			trnFeePayment.setShortLink(shortURL);
			trnFeePayment.setUserId(request.getUserId());

		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
		}
		trnFeePaymentRepository.save(trnFeePayment);
		return trnFeePayment;
	}

	@Override
	public void saveFeeCodeDetails(TrnPaymentRequest trnPaymentRequest, TrnFeePayment trnFeePayment) {
		for (TrnFeeDetails trnFeeDetails : trnPaymentRequest.getTrnFeeDetails()) {
			TrnFeeDetails tmp = trnFeeDetails;
			tmp.setFeeTransactionId((int) trnFeePayment.getId());
			trnFeeDetailsMappingRepository.save(tmp);
		}
	}

	@Override
	public void sendSMSandEmailForFess(TrnPaymentRequest trnPaymentRequest, TrnPaymentRequest tpr, String shortURL, TrnFeePayment trnFeePayment) {
		System.out.println("43");
		if (trnPaymentRequest.getMobileNo() != null && !trnPaymentRequest.getMobileNo().trim().equals("")) {
			String smsContents = "Dear Customer, Your application for loan no " + trnPaymentRequest.getLaNumber() + " has been successfully logged in to our system. " + "Please click the link below to pay associated charges for further processing your application.\r\n" + "\r\n" + " \r\n" + "\r\n" + shortURL + "\r\n" + "\r\n" + " \r\n" + "\r\n" + "In case of any queries regarding associated charges or issues while making the payment please reach out to your relationship manager.\r\n" + "\r\n" + " \r\n" + "\r\n" + "Thank you for choosing Avanse Financial Services as your preferred education loan partner.\r\n" + "\r\n" + " \r\n" + "\r\n" + "Regards\r\n" + "\r\n" + " \r\n" + "\r\n" + "Team Avanse";
			smsService.messageFee(smsContents, trnPaymentRequest.getMobileNo(), (int) trnFeePayment.getId(), "PAYMENT_SYSTEM");
		}
		System.out.println("44");
		if (trnPaymentRequest.getEmailId() != null && !trnPaymentRequest.getEmailId().trim().equals("")) {
			String emailContent = "Dear Customer, Your application for loan no " + trnPaymentRequest.getLaNumber() + " has been successfully logged in to our system. Please click the link below to pay associated charges for further processing your application.\r\n" + "\r\n" + " \r\n" + "\r\n" + "" + shortURL + "\r\n" + "\r\n" + " \r\n" + "\r\n" + "In case of any queries regarding associated charges or issues while making the payment please reach out to your relationship manager.\r\n" + "\r\n" + " \r\n" + "\r\n" + "Thank you for choosing Avanse Financial Services as your preferred education loan partner.\r\n" + "\r\n" + " \r\n" + "\r\n" + "Regards\r\n" + "\r\n" + " \r\n" + "\r\n" + "Team Avanse";
			String subject1 = subject.replace("<PAYMENT_PURPOSE>", "Fees");
			emailService.sendSimpleMessageFee(trnPaymentRequest.getEmailId(), subject1, emailContent, (int) trnFeePayment.getId(), "PAYMENT_SYSTEM");
			System.out.println("45");
		}
		System.out.println("46");
		if (tpr != null) {
			tpr.setShortURL(shortURL);
		}
	}

	@Override
	public ApplicantDetailsResponse getDetailsAgainstLoanNumberForFee(TrnPaymentRequest trnPaymentRequest) {
		ApplicantDetailsResponse applicantDetailsResponse = new ApplicantDetailsResponse();
		List<AccountHolderDetails> accountHolderDetailsList = new ArrayList<>();
		updateFeeDetails(trnPaymentRequest, accountHolderDetailsList);
		System.out.println("getDetailsAgainstLoanNumber" + 5);
		System.out.println("getDetailsAgainstLoanNumber" + 6);
		applicantDetailsResponse.setAccountHolderDetailsList(accountHolderDetailsList);
		return applicantDetailsResponse;
	}

	private void updateFeeDetails(TrnPaymentRequest trnPaymentRequest, List<AccountHolderDetails> accountHolderDetailsList) throws JSONException {
		HttpHeaders pennantHeaderApi = new HttpHeaders();
		System.out.println("getDetailsAgainstLoanNumber" + 1 + " Source id:" + trnPaymentRequest.getSourceId());
		MstSourceMapping source = fetchSourceFromDB(trnPaymentRequest);
		System.out.println("getDetailsAgainstLoanNumber" + 2);
		AvanseUtility.preparePennantHeader(pennantHeaderApi, source);
		System.out.println("getDetailsAgainstLoanNumber" + 3);
		JSONObject overdueRequest = new JSONObject();
		overdueRequest.put("loanNo", trnPaymentRequest.getLaNumber());
		HttpEntity<String> pennantRequest = AvanseUtility.preparePennantRequest(overdueRequest, pennantHeaderApi);
		System.out.println("getDetailsAgainstLoanNumber" + 4);
		PennantViewOther[] pennantViewArray = fetchFeeDetailsFromPennant(trnPaymentRequest, pennantRequest);
		if (pennantViewArray != null && pennantViewArray.length != 0) {
			AvanseUtility.prepareAccountHolderDetailsOther(accountHolderDetailsList, pennantViewArray);
		} else {
			PennantView[] fetchPennantArray = fetchPennantArray(trnPaymentRequest, pennantRequest);
			if (fetchPennantArray != null && fetchPennantArray.length != 0)
				AvanseUtility.prepareAccountHolderDetails(accountHolderDetailsList, fetchPennantArray);
		}
		if (accountHolderDetailsList != null && !accountHolderDetailsList.isEmpty()) {
			for (AccountHolderDetails accountHolderDetails : accountHolderDetailsList) {
				if (accountHolderDetails.getAccountName().equalsIgnoreCase("APPLICANT")) {
					trnPaymentRequest.setCustomerName(accountHolderDetails.getName());
					trnPaymentRequest.setMobileNo(accountHolderDetails.getMobileNumber());
					trnPaymentRequest.setEmailId(accountHolderDetails.getEmailId());
					trnPaymentRequest.setAddress(accountHolderDetails.getAddress());
					trnPaymentRequest.setLoanBranch(accountHolderDetails.getLoanBranch());
					trnPaymentRequest.setLoanType(accountHolderDetails.getLoanType());
				}
			}
		}
	}

	@Override
	public PennantViewOther[] fetchFeeDetailsFromPennant(TrnPaymentRequest trnPaymentRequest, HttpEntity<String> request) {
		System.out.println("fetchFeeDetailsFromPennant loan API: " + loanDetailsAPIForFee);
		ResponseEntity<EncryptDataPojo> loanDetails = restTemplate.postForEntity(loanDetailsAPIForFee, request, EncryptDataPojo.class);
		System.out.println("fetchFeeDetailsFromPennant" + 1);
		EncryptDataPojo pojo = loanDetails.getBody();
		System.out.println("fetchFeeDetailsFromPennant" + 2);
		String decryptedLoanDetails = EncryptUtil.decryptString(pojo.getEncryptedData(), "");
		System.out.println("fetchFeeDetailsFromPennant" + 3);
		ObjectMapper mapper = new ObjectMapper();
		PennantViewOther[] pennantViewArray = null;
		try {
			pennantViewArray = mapper.readValue(decryptedLoanDetails, PennantViewOther[].class);
			System.out.println("fetchFeeDetailsFromPennant" + 4);
		} catch (JsonProcessingException e) {
			System.out.println("fetchFeeDetailsFromPennant" + 5);
			e.printStackTrace();
		}
		return pennantViewArray;
	}

	@Override
	public String fetchCustomerName(TrnPaymentRequest trnPaymentRequest, MstSourceMapping source) {
		String customerName = "Customer";
		HttpHeaders pennantHeaderApi = new HttpHeaders();
		AvanseUtility.preparePennantHeader(pennantHeaderApi, source);
		JSONObject overdueRequest = new JSONObject();
		overdueRequest.put("loanNo", trnPaymentRequest.getLaNumber());
		HttpEntity<String> pennantRequest = AvanseUtility.preparePennantRequest(overdueRequest, pennantHeaderApi);
		if (trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("FEES")) {
			PennantViewOther[] pennantViewArray = fetchFeeDetailsFromPennant(trnPaymentRequest, pennantRequest);
			if (pennantViewArray != null && pennantViewArray.length != 0) {
				customerName = getCustomerNameFromPennant(customerName, pennantViewArray);
			} else {
				PennantView[] fetchPennantArray = fetchPennantArray(trnPaymentRequest, pennantRequest);
				customerName = getCustomerNameFromPennant(customerName, fetchPennantArray);
			}
		} else {
			PennantView[] fetchPennantArray = fetchPennantArray(trnPaymentRequest, pennantRequest);
			customerName = getCustomerNameFromPennant(customerName, fetchPennantArray);
		}
		return customerName;
	}

	private String getCustomerNameFromPennant(String customerName, PennantViewOther[] fetchPennantArray) {
		if (fetchPennantArray != null && fetchPennantArray.length != 0) {
			for (PennantViewOther pennantView : fetchPennantArray) {
				if (pennantView == null) {
					continue;
				}
				if (pennantView.getTYPE().equalsIgnoreCase("APPLICANT")) {
					customerName = pennantView.getCustomer_name();
				}
			}
		}
		return customerName;
	}

	private String getCustomerNameFromPennant(String customerName, PennantView[] fetchPennantArray) {
		if (fetchPennantArray != null && fetchPennantArray.length != 0) {
			for (PennantView pennantView : fetchPennantArray) {
				if (pennantView == null) {
					continue;
				}
				if (pennantView.getTYPE().equalsIgnoreCase("APPLICANT")) {
					customerName = pennantView.getCustomer_name();
				}
			}
		}
		return customerName;
	}

}
