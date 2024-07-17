package com.avanse.controller;

import static com.avanse.util.Signature.hmac_sha256;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.avanse.exception.AppException;
import com.avanse.filters.EncryptDecryptFilter;
import com.avanse.filters.EncryptUtil;
import com.avanse.jpa.model.MstFeesDetails;
import com.avanse.jpa.model.MstFundAccountMapping;
import com.avanse.jpa.model.MstSourceMapping;
import com.avanse.jpa.model.TrnClientDetailsRequest;
import com.avanse.jpa.model.TrnEmailStatusDetails;
import com.avanse.jpa.model.TrnFeeDetails;
import com.avanse.jpa.model.TrnFeePayment;
import com.avanse.jpa.model.TrnPayment;
import com.avanse.jpa.model.TrnPaymentRequest;
import com.avanse.jpa.model.TrnPaymentTransaction;
import com.avanse.jpa.model.TrnSMSStatusDetails;
import com.avanse.jpa.repository.MstFeesDeailsRepository;
import com.avanse.jpa.repository.MstFundAccountMappingRepository;
import com.avanse.jpa.repository.MstSourceMappingRepository;
import com.avanse.jpa.repository.TrnEmailStatusDetailsRepository;
import com.avanse.jpa.repository.TrnFeeDetailsMappingRepository;
import com.avanse.jpa.repository.TrnFeePaymentRepository;
import com.avanse.jpa.repository.TrnPaymentRepository;
import com.avanse.jpa.repository.TrnPaymentRequestDetailsRepository;
import com.avanse.jpa.repository.TrnPaymentRequestRepository;
import com.avanse.jpa.repository.TrnPaymentTransactionRepository;
import com.avanse.jpa.repository.TrnSMSStatusDetailsRepository;
import com.avanse.model.ELMSView;
import com.avanse.model.EncryptDataPojo;
import com.avanse.model.FeeDeatils;
import com.avanse.model.Order;
import com.avanse.model.PennantView;
import com.avanse.service.EmailService;
import com.avanse.service.PaymentService;
import com.avanse.service.SmsService;
import com.avanse.utility.UtilityClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	// @Value("${apiKey}")
	private String apiKey;

	// @Value("${apiSecret}")
	private String apiSecret;

	// @Value("${apiMerchantId}")
	private String apiMerchantId;

	@Value("${overDueApi}")
	private String overDueApi;

	@Value("${proxy.ip}")
	private String proxy_ip;

	@Value("${proxy.port}")
	private int proxy_port;

	@Value("${proxy.user}")
	private static String proxy_user;

	@Value("${proxy.password}")
	private static String proxy_password;

	@Autowired
	private TrnPaymentRequestRepository trnPaymentRequestRepository;
	
	@Autowired
	private TrnSMSStatusDetailsRepository trnSMSStatusDetailsRepository;
	
	@Autowired
	private TrnEmailStatusDetailsRepository trnEmailStatusDetailsRepository;

	@Autowired
	private TrnPaymentRequestDetailsRepository trnPaymentRequestDetailsRepository;

	@Autowired
	private TrnPaymentTransactionRepository trnPaymentTransactionRepository;
	
	@Autowired
	private TrnFeeDetailsMappingRepository trnFeeDetailsMappingRepository;

	@Autowired
	private MstSourceMappingRepository mstSourceMappingRepository;

	@Autowired
	private MstFundAccountMappingRepository mstFundAccountMappingRepository;
	
	@Autowired
	private MstFeesDeailsRepository mstFeesDeailsRepository;
	
	@Autowired
	private PaymentService paymentService;

	Map<String, String> responseMap;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EmailService emailService;

	@Autowired
	SmsService smsService;
	
	@Autowired
	private TrnFeePaymentRepository trnFeePaymentRepository;
	
	@Autowired
	private TrnPaymentRepository trnPaymentRepository;

	@Value("${mail.subject}")
	private String mailSubject;

	public static final String ENCRYPTION_KEY = "encryptedData";

	@Value("${getLoanDetails}")
	private String getLoanDetails;

	// to get last payment details of loan application number order by payment
	// request date.
	@PostMapping("/lastPayment")
	public ResponseEntity<?> getLastPaymentDetails(@RequestBody TrnPaymentRequest trnPaymentRequest) {
		if (trnPaymentRequest.getLaNumber() == null || "".equals(trnPaymentRequest.getLaNumber().trim())) {
			throw new AppException("Null or blank input loan number");
		}

		Optional<TrnPaymentRequest> lastRecord = trnPaymentRequestRepository
				.getTopLastRecord(trnPaymentRequest.getLaNumber(), "NEW_REQUEST");

		if (lastRecord.isPresent()) {
			TrnPaymentRequest resp = lastRecord.get();
			resp.setTrnPaymentTransaction(null);
			return ResponseEntity.ok(resp);
		} else {
			throw new AppException("No payment record found");
		}

	}

	@PostMapping("/fee-details")
	public ResponseEntity<?> getFeeDetails(@RequestBody FeeDeatils feeDeatils) {
		System.out.println("request:" + feeDeatils.toString());

		if (feeDeatils.getPaymentRequestId() == 0)
			throw new AppException("Invalid Input PaymentRequestId.");

		Optional<TrnPaymentRequest> response = trnPaymentRequestRepository.findById(feeDeatils.getPaymentRequestId());

		TrnPaymentRequest dbTrnPaymenyRequest = response.get();
		if (!response.isPresent()) {
			throw new AppException("Request details not found!!!");
		}
		if (!dbTrnPaymenyRequest.getPaymentPurpose().equalsIgnoreCase("FEES")) {
			throw new AppException("Request ID is not FEES!!!");
		}
		if ("PAYMENT_COMPLETED".equals(response.get().getRequestStatus())) {
			throw new AppException("Payment alredy completed for given request id!!!");
		}

		MstSourceMapping source = fetchSourceFromDB(dbTrnPaymenyRequest);
		System.out.println("savePaymentResponse" + 1);
		if (dbTrnPaymenyRequest.isAgentRequest()) {
//			updateCustmerDetails(dbTrnPaymenyRequest, source);
			updateCustmerDetails(dbTrnPaymenyRequest, source);
			System.out.println("savePaymentResponse" + 3);
			List<TrnFeeDetails> trnFeeDetails = new ArrayList<>();
			try {
				System.out.println("savePaymentResponse payment request id" + Long.valueOf(feeDeatils.getPaymentRequestId()) );
				trnFeeDetails = trnFeeDetailsMappingRepository.findAllByPaymentRequestId(Long.valueOf(feeDeatils.getPaymentRequestId()));
				System.out.println("savePaymentResponse size" + trnFeeDetails.size() );
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
	            e.printStackTrace(new PrintWriter(sw));
	            String exceptionAsString = sw.toString();
	            System.out.println(exceptionAsString);
			}
			if (trnFeeDetails.isEmpty()) {
				throw new AppException("No FEES details found!!!");
			}
			for(TrnFeeDetails trnFeeDetail1 : trnFeeDetails) {
				MstFeesDetails mstFeesDetails = mstFeesDeailsRepository.findByFeeCode(trnFeeDetail1.getFeeCode());
				trnFeeDetail1.setFeeCode(mstFeesDetails.getFeeDescription());
			}
			dbTrnPaymenyRequest.setTrnFeeDetails(trnFeeDetails);
		
		} else {
			System.out.println("11");
			List<TrnFeePayment> trnFeePayment1 = trnFeePaymentRepository.findByPaymentRequestId(Integer.toString(dbTrnPaymenyRequest.getPaymentRequestId()));
			int feeTransactionId = 0;
			for (TrnFeePayment trnFeePayment : trnFeePayment1) {
				System.out.println(trnFeePayment.getId());
				feeTransactionId = (int) trnFeePayment.getId();
				dbTrnPaymenyRequest.setUniqueReferenceNumber(trnFeePayment.getUniqueReferenceNumber());
				dbTrnPaymenyRequest.setUserId(trnFeePayment.getUserId());
				dbTrnPaymenyRequest.setCustomerName(trnFeePayment.getCustomerName());
				dbTrnPaymenyRequest.setEmailId(trnFeePayment.getEmailId());
				dbTrnPaymenyRequest.setMobileNo(trnFeePayment.getMobileNumber());
				dbTrnPaymenyRequest.setCif(trnFeePayment.getCif());
			}

			List<TrnFeeDetails> trnFeeDetails = trnFeeDetailsMappingRepository.findAllByfeeTransactionId(feeTransactionId);
			if (trnFeeDetails.isEmpty()) {
				throw new AppException("No FEES details found!!!");
			}
			List<TrnFeeDetails> trnFeeDetails2 = new ArrayList<TrnFeeDetails>();
			TrnFeeDetails trnFeeDetail;
			for(TrnFeeDetails trnFeeDetail1 : trnFeeDetails) {
				trnFeeDetail = new TrnFeeDetails();
				String feeCode = trnFeeDetail1.getFeeCode();
				MstFeesDetails mstFeesDetails = mstFeesDeailsRepository.findByFeeCode(feeCode);
				trnFeeDetail.setFeeId(trnFeeDetail1.getFeeId());
				trnFeeDetail.setFeeAmount(trnFeeDetail1.getFeeAmount());
				trnFeeDetail.setFeeTransactionId(trnFeeDetail1.getFeeTransactionId());
				trnFeeDetail.setFeeCode(mstFeesDetails.getFeeDescription());
				trnFeeDetails2.add(trnFeeDetail);
			}
			//dbTrnPaymenyRequest.setTrnFeeDetails(trnFeeDetails);
			dbTrnPaymenyRequest.setTrnFeeDetails(trnFeeDetails2);
//		}
			System.out.println("response:" + dbTrnPaymenyRequest.toString());
		}
		dbTrnPaymenyRequest.setSourceAppName(source.getSourceAppName());
		return ResponseEntity.ok(dbTrnPaymenyRequest);
	}

	private void updateCustmerDetails(TrnPaymentRequest dbTrnPaymenyRequest, MstSourceMapping source) {
		System.out.println("updateCustmerDetails"+1);
		TrnPayment trnPayment = trnPaymentRepository.findBypaymentRequestId(Long.valueOf(dbTrnPaymenyRequest.getPaymentRequestId()));
		System.out.println("updateCustmerDetails"+2);
		if(trnPayment == null) {
			throw new AppException("Payment Request Id not exist in database");
		}
		
		dbTrnPaymenyRequest.setCustomerName(trnPayment.getCustomerName());
		dbTrnPaymenyRequest.setEmailId(trnPayment.getEmailId());
		dbTrnPaymenyRequest.setMobileNo(trnPayment.getMobileNumber());
		/*
		System.out.println("getDetailsAgainstLoanNumber" + 2);
		HttpHeaders pennantHeaderApi = new HttpHeaders();
		AvanseUtility.preparePennantHeader(pennantHeaderApi, source);
		System.out.println("getDetailsAgainstLoanNumber" + 3);
		JSONObject overdueRequest = new JSONObject();
		overdueRequest.put("loanNo", dbTrnPaymenyRequest.getLaNumber());
		HttpEntity<String> pennantRequest = AvanseUtility.preparePennantRequest(overdueRequest, pennantHeaderApi);
		PennantViewOther[] pennantViewArray = paymentService.fetchFeeDetailsFromPennant(dbTrnPaymenyRequest, pennantRequest);
		List<AccountHolderDetails> accountHolderDetailsList = new ArrayList<>();
		if (pennantViewArray != null && pennantViewArray.length != 0) {
			AvanseUtility.prepareAccountHolderDetailsOther(accountHolderDetailsList, pennantViewArray);
		} else {
			PennantView[] fetchPennantArray = paymentService.fetchPennantArray(dbTrnPaymenyRequest, pennantRequest);
			if (fetchPennantArray != null && fetchPennantArray.length != 0)
				AvanseUtility.prepareAccountHolderDetails(accountHolderDetailsList, fetchPennantArray);
		}
		if (accountHolderDetailsList != null && !accountHolderDetailsList.isEmpty()) {
			for (AccountHolderDetails accountHolderDetails : accountHolderDetailsList) {
				if (accountHolderDetails.getAccountName().equalsIgnoreCase("APPLICANT")) {
					dbTrnPaymenyRequest.setCustomerName(accountHolderDetails.getName());
					dbTrnPaymenyRequest.setEmailId(accountHolderDetails.getEmailId());
					dbTrnPaymenyRequest.setMobileNo(accountHolderDetails.getMobileNumber());
				}
			}
		}
		*/
	}

	
	@PostMapping("/requestDetails")
	public ResponseEntity<?> getRequestDetails(@RequestBody TrnPaymentRequest trnPaymentRequest) {

		System.out.println("request:" + trnPaymentRequest.toString());

		if (trnPaymentRequest.getPaymentRequestId() == 0)
			throw new AppException("Invalid Input PaymentRequestId.");

		Optional<TrnPaymentRequest> response = trnPaymentRequestRepository
				.findById(trnPaymentRequest.getPaymentRequestId());

		TrnPaymentRequest tr = response.get();
		Double allowed_amt = null;

		if (response.isPresent()) {

			if ("PAYMENT_COMPLETED".equals(response.get().getRequestStatus())) {
				throw new AppException("Payment alredy completed for given request id!!!");
			}

			tr.setCharges(trnPaymentRequestDetailsRepository.findAllByTrnPaymentRequest(response.get()));

			Optional<MstFundAccountMapping> mfam = mstFundAccountMappingRepository
					.findByLoanTypeAndPaymentPurpose(tr.getLoanType(), tr.getPaymentPurpose());

			if (mfam.isPresent()) {

				tr.setMerchantId(mfam.get().getMerchantID());
				tr.setRazorPayKey(mfam.get().getRazorPayKey());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Optional<MstSourceMapping> source = mstSourceMappingRepository.findBySourceId(tr.getSourceId());

			if (source.isPresent()) {

				String auth = source.get().getSourceId() + ":" + source.get().getSecretKey();
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);

				headers.add("Authorization", authHeader);

			} else {
				throw new AppException(
						"SourceId not available in master for generation of token for elmspennantviews api");
			}

			JSONObject inputObject = new JSONObject();

			JSONObject lanObject = new JSONObject();
			lanObject.put("loanNo", response.get().getLaNumber());

			inputObject.put(EncryptDecryptFilter.ENCRYPTION_KEY, EncryptUtil.encryptString(lanObject.toString(), ""));

			HttpEntity<String> request = new HttpEntity<String>(inputObject.toString(), headers);

/*			if (response.get().getLaNumber().contains("/")) {
				try {

					ResponseEntity<EncryptDataPojo> entity = restTemplate.postForEntity(overDueApi, request,
							EncryptDataPojo.class);

					EncryptDataPojo d = entity.getBody();

					String decryptString = EncryptUtil.decryptString(d.getEncryptedData(), "");

					ObjectMapper mapper = new ObjectMapper();
					ELMSView e = null;
					try {
						e = mapper.readValue(decryptString, ELMSView.class);
					} catch (JsonProcessingException e1) {
						e1.printStackTrace();
					}

					if (e != null) {

						allowed_amt = e.getTOTAL_AMOUNT() + (3 * e.getCURRENT_MONTH_INST());
						tr.setAllowedAmt(allowed_amt);
						tr.setCustomerName(e.getCustomer_name());
						tr.setLoanBranch(e.getLOAN_BRANCH());
						tr.setMobileNo(e.getMOBILE_NUMBER());
						tr.setEmailId(e.getEMAIL_ID());
						tr.setAddress(e.getADDRESS());
						tr.setEmi_si_pi_amt(e.getEMI_SI_PI_AMOUNT());
						tr.setOverdue_emi_amt(e.getOVERDUE_EMI_AMOUNT());
						tr.setCURRENT_MONTH_INST(e.getCURRENT_MONTH_INST());
					}

				} catch (RestClientException e) {
					throw new AppException("Unable to connect to ELMSPennantViews Api");
				}
			} else {*/

				try {

					ResponseEntity<EncryptDataPojo> entity = restTemplate.postForEntity(overDueApi, request,
							EncryptDataPojo.class);

					EncryptDataPojo d = entity.getBody();

					String decryptString = EncryptUtil.decryptString(d.getEncryptedData(), "");

					ObjectMapper mapper = new ObjectMapper();
					ELMSView e = null;
					try {
						e = mapper.readValue(decryptString, ELMSView.class);
					} catch (JsonProcessingException e1) {
						e1.printStackTrace();
					}

					if (e != null) {
						allowed_amt = e.getTOTAL_AMOUNT() + (2 * e.getCURRENT_MONTH_INST());
						tr.setAllowedAmt(allowed_amt);
						tr.setCustomerName(e.getCustomer_name());
						tr.setLoanBranch(e.getLOAN_BRANCH());
						tr.setMobileNo(e.getMOBILE_NUMBER());
						tr.setEmailId(e.getEMAIL_ID());
						tr.setAddress(e.getADDRESS());
						tr.setEmi_si_pi_amt(e.getEMI_SI_PI_AMOUNT());
						tr.setOverdue_emi_amt(e.getOVERDUE_EMI_AMOUNT());
						tr.setCURRENT_MONTH_INST(e.getCURRENT_MONTH_INST());
					}

				} catch (RestClientException e) {
					throw new AppException("Unable to connect to ELMSPennantViews Api");
				}

			}
		/* } */

		if (!response.isPresent()) {
			throw new AppException("Request details not found!!!");
		} else {
			System.out.println("response:" + tr.toString());
			return ResponseEntity.ok(tr);
		}

	}

	@PostMapping("/orderId")
	public ResponseEntity<?> getOrder(@RequestBody TrnPaymentRequest trnPaymentRequest) {

		Optional<TrnPaymentRequest> optional = trnPaymentRequestRepository.findById(trnPaymentRequest.getPaymentRequestId());

		if (!optional.isPresent()) {
			throw new AppException("Invalid payment request ID");
		}
		TrnPaymentRequest dbTrnPaymentRequest = optional.get();
		if (!"NEW_REQUEST".equals(dbTrnPaymentRequest.getRequestStatus())) {
			throw new AppException("Payment request already exists for given request id!!!");
		}

		if(trnPaymentRequest.getAmount() <= 0) {
			throw new AppException("Amount Should be greater than 0");
		}

		TrnPaymentRequest t2 = dbTrnPaymentRequest;

		String paymentPurpose = t2.getPaymentPurpose();
		if (trnPaymentRequest.getAmount().compareTo(dbTrnPaymentRequest.getAmount()) != 0) {
			if ("OVERDUE".equals(paymentPurpose.toUpperCase())
					&& trnPaymentRequest.getAmount().compareTo(t2.getTotalAmount()) < 0) {
				t2.setPaymentPurpose("OverdueWithoutCharges");
				paymentPurpose = "OverdueWithoutCharges";
			}
			t2.setAmount(trnPaymentRequest.getAmount());
			trnPaymentRequestRepository.saveAndFlush(t2);
		}

		Optional<MstFundAccountMapping> mfam = mstFundAccountMappingRepository.findByLoanTypeAndPaymentPurpose(dbTrnPaymentRequest.getLoanType(), paymentPurpose);

		if (!mfam.isPresent()) {
			throw new AppException("No razor pay merchant id, RazorPay Key and Secret assigned for loanType:" + dbTrnPaymentRequest.getLoanType() + " and paymentPurpose:" + dbTrnPaymentRequest.getPaymentPurpose());
		}
		MstFundAccountMapping mstFundAccountMapping = mfam.get();
		apiMerchantId = mstFundAccountMapping.getMerchantID();
		apiKey = mstFundAccountMapping.getRazorPayKey();
		apiSecret = mstFundAccountMapping.getRazorSecret();
		if (UtilityClass.isEmptyString(apiMerchantId) || UtilityClass.isEmptyString(apiKey) || 
				UtilityClass.isEmptyString(apiSecret)) {
			throw new AppException("Null or blank razor pay merchant id or RazorPay Key or RazorPay Secret assigned for loanType:" + dbTrnPaymentRequest.getLoanType() + " and paymentPurpose:" + dbTrnPaymentRequest.getPaymentPurpose() + " in database master");
		}
		Order order = new Order((long) (trnPaymentRequest.getAmount() * 100), trnPaymentRequest.getCurrency());
		Order response = testRazor(order);
		if (response == null) {
			throw new AppException("Order Id not generated");
		}
		String orderId = ((Order) response).getOrderId();
		t2.setRequestStatus("ORDER_GENERATED");
		TrnPaymentTransaction trn = new TrnPaymentTransaction(orderId, null, null, new Date(), null);
		trn.setPaymentRequest(t2);
		trnPaymentTransactionRepository.saveAndFlush(trn);
		trnPaymentRequestRepository.saveAndFlush(t2);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/success")
	public ResponseEntity<?> savePaymentResponse(@RequestBody TrnPaymentTransaction trnPaymentTransaction) {
		TrnPaymentRequest trnPaymentRequest = validateRequest(trnPaymentTransaction);
		validateSignature(trnPaymentTransaction);
		TrnPaymentTransaction savedTrnPaymentTransaction = trnPaymentTransactionRepository.save(trnPaymentTransaction);

		if (savedTrnPaymentTransaction == null) {
			new AppException("Payment data save failed!!!");
		}
		responseMap = new HashMap<String, String>();
		responseMap.put("successMessage", "Payment data saved successfully!!!");

		String laNumber = trnPaymentRequest.getLaNumber();

		StringBuffer emailIds = new StringBuffer("");
		StringBuffer mobileNos = new StringBuffer("");
		String customerName = "Customer";
		
		System.out.println("savePaymentResponse"+2);
//		MstSourceMapping source = fetchSourceFromDB(trnPaymentRequest);
		System.out.println("savePaymentResponse"+1);
		if (trnPaymentRequest.isAgentRequest()) {
			System.out.println("savePaymentResponse"+3);
			customerName = prepareEmailIdsAndMobileNumberForAgent(trnPaymentRequest, emailIds, mobileNos);
//			if(customerName.equalsIgnoreCase("Customer")) {
//				customerName = paymentService.fetchCustomerName(trnPaymentRequest,source);
//			}
			//customer name pending due to business confirmation
			customerName = "Customer";
			String smsMessage = getSMSMessageForAgent(trnPaymentRequest, savedTrnPaymentTransaction, laNumber);
			System.out.println("savePaymentResponse smsMessage"+smsMessage);
			String emailMessage = getEmailMessageForAgent(trnPaymentRequest, savedTrnPaymentTransaction, laNumber, customerName);
			System.out.println("savePaymentResponse emailMessage"+emailMessage);
			sendEmailAndSMS(savedTrnPaymentTransaction, laNumber, emailIds, mobileNos, smsMessage, emailMessage);
		} else {
			if (trnPaymentRequest != null && trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("FEES")) {
				customerName = prepareEmailIdsAndMobileNumberForFees(trnPaymentRequest, emailIds, mobileNos);
			} else if (trnPaymentRequest != null && trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("OVERDUE")) {
				customerName = prepareEmailIdsAndMobileNumberForOverdue(trnPaymentRequest, laNumber, emailIds, mobileNos, customerName);
			}
			String smsMessage = getSMSMessage(trnPaymentRequest, savedTrnPaymentTransaction, laNumber);
			System.out.println("savePaymentResponse smsMessage"+smsMessage);
			String emailMessage = getEmailMessage(trnPaymentRequest, savedTrnPaymentTransaction, laNumber, customerName);
			System.out.println("savePaymentResponse emailMessage"+emailMessage);
			sendEmailAndSMS(savedTrnPaymentTransaction, laNumber, emailIds, mobileNos, smsMessage, emailMessage);
		}
		return ResponseEntity.ok(responseMap);
	}

	private String getEmailMessageForAgent(TrnPaymentRequest trnPaymentRequest, TrnPaymentTransaction savedTrnPaymentTransaction, 
			String laNumber, String customerName) {
		String purpose = trnPaymentRequest.getPaymentPurpose().toUpperCase();
		purpose = purpose.startsWith("OVERDUE") ? "Overdue": trnPaymentRequest.getPaymentPurpose();
		String emailMessage = "Dear "+customerName+",\r\n" + 
				"\r\n" + 
				" \r\n" + 
				"\r\n" + 
				"Your payment of Rs. "+trnPaymentRequest.getAmount()+" was successful. \r\n" + 
				"\r\n" + 
				" \r\n" + 
				"\r\n" + 
				"Please take note of the details below:\r\n" + 
				"\r\n" + 
				" \r\n" + 
				"\r\n" + 
				"LAN No: "+laNumber+"\r\n" +
				"\r\n" + 
				"Amount: "+ trnPaymentRequest.getAmount()+"\r\n"+ 
				"\r\n" + 
				"Payment Purpose: "+purpose+"\r\n" + 
				"\r\n" + 
				"Transaction ID: "+trnPaymentRequest.getPaymentRequestId()+"\r\n" + 
				"\r\n" + 
				"Payment ID: "+savedTrnPaymentTransaction.getRazorPayPaymentId()+"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				" \r\n" + 
				"\r\n" + 
				"Get ready to #AspireWithoutBoundaries\r\n" + 
				"\r\n" + 
				" \r\n" + 
				"\r\n" + 
				"Regards,\r\n" + 
				"\r\n" + 
				"Team Avanse";
		return emailMessage;
	}

	private String getSMSMessageForAgent(TrnPaymentRequest trnPaymentRequest, TrnPaymentTransaction savedTrnPaymentTransaction, String laNumber) {
		String purpose = trnPaymentRequest.getPaymentPurpose().toUpperCase();
		purpose = purpose.startsWith("OVERDUE") ? "Overdue": trnPaymentRequest.getPaymentPurpose();
		String smsMessage = "Your payment of Rs." + trnPaymentRequest.getAmount() + " towards " + purpose 
		+ " for " + "loan no " + laNumber + " was successful. Please note the Transaction ID " + trnPaymentRequest.getPaymentRequestId() + 
		" and Payment ID " + savedTrnPaymentTransaction.getRazorPayPaymentId() + " for your reference.\r\n" 
		+ "\r\n" + "Regards,\r\n" + "\r\n" + "Team Avanse";
		return smsMessage;
	}

	private String prepareEmailIdsAndMobileNumberForAgent(TrnPaymentRequest trnPaymentRequest, 
			StringBuffer emailIds, StringBuffer mobileNos) {
		String customerName = "Customer";
		System.out.println("prepareEmailIdsAndMobileNumberForAgent" + 1);
		List<TrnSMSStatusDetails> smsStatusDetails = trnSMSStatusDetailsRepository.findByPaymentRequestId(trnPaymentRequest.getPaymentRequestId());
		System.out.println("prepareEmailIdsAndMobileNumberForAgent" + 2);
		List<TrnEmailStatusDetails> emailStatusDetails = trnEmailStatusDetailsRepository.findByPaymentRequestId(trnPaymentRequest.getPaymentRequestId());
		System.out.println("prepareEmailIdsAndMobileNumberForAgent" + 3);
		if (smsStatusDetails != null && !smsStatusDetails.isEmpty()) {
			System.out.println("prepareEmailIdsAndMobileNumberForAgent" + 4);
			for (TrnSMSStatusDetails obj : smsStatusDetails) {
				if (obj != null) {
					if (!UtilityClass.isEmptyString(obj.getContactNo())) {
						mobileNos.append(obj.getContactNo() + "|");
					}

					if (obj.getApplicantType().equalsIgnoreCase("APPLICANT")) {
						customerName = obj.getCustomerName();
					}
				}
			}
		}

		if (emailStatusDetails != null && !emailStatusDetails.isEmpty()) {
			System.out.println("prepareEmailIdsAndMobileNumberForAgent" + 5);
			for (TrnEmailStatusDetails obj : emailStatusDetails) {
				if (obj != null) {
					if (!UtilityClass.isEmptyString(obj.getToEmailId())) {
						emailIds.append(obj.getToEmailId() + ",");
					}
					if (obj.getApplicantType().equalsIgnoreCase("APPLICANT")) {
						customerName = obj.getCustomerName();
					}
				}
			}
		}
		System.out.println("prepareEmailIdsAndMobileNumberForAgent" + 5);
		System.out.println("prepareEmailIdsAndMobileNumberForAgent mobile" + mobileNos.toString());
		System.out.println("prepareEmailIdsAndMobileNumberForAgent email" + emailIds.toString());
		System.out.println("prepareEmailIdsAndMobileNumberForAgent customerName" + customerName);
		return customerName;
	}

	private void sendEmailAndSMS(TrnPaymentTransaction savedTrnPaymentTransaction, String laNumber,
			StringBuffer emailIds, StringBuffer mobileNos, String smsMessage, String emailMessage) {
		if (emailIds.length() > 0) {
			emailIds.deleteCharAt(emailIds.length() - 1);
			emailService.sendSimpleMessage(emailIds.toString(), mailSubject, emailMessage,
					savedTrnPaymentTransaction.getPaymentTransactionId(), "PAYMENT_SYSTEM");
		} else {
			System.out.println("No email id found for sending email against lan:" + laNumber);
		}

		if (mobileNos.length() > 0) {
			mobileNos.deleteCharAt(mobileNos.length() - 1);
			smsService.message(smsMessage, mobileNos.toString(), savedTrnPaymentTransaction.getPaymentTransactionId(),
					"PAYMENT_SYSTEM");
		} else {
			System.out.println("No mobile no found for sending sms against lan:" + laNumber);
		}
	}

	private String getEmailMessage(TrnPaymentRequest trnPaymentRequest,
			TrnPaymentTransaction savedTrnPaymentTransaction, String laNumber, String customerName) {
		String emailMessage = "Dear " + customerName + ",\r\n" + " \r\n" + "Your payment of Rs. "
				+ trnPaymentRequest.getAmount() + " towards your loan was successful. \r\n" + " \r\n"
				+ "Please check the details below:\r\n" + " \r\n" + "Transaction ID:"
				+ trnPaymentRequest.getPaymentRequestId() + "\r\n" + "LAN No:" + laNumber + "\r\n" + "Amount:"
				+ trnPaymentRequest.getAmount() + "\r\n" + "Payment ID:" + savedTrnPaymentTransaction.getRazorPayPaymentId() + "\r\n"
				+ " \r\n" + "Get ready to #AspireWithoutBoundaries\r\n" + " \r\n" + "Regards,\r\n" + "Team Avanse";
		return emailMessage;
	}

	private String getSMSMessage(TrnPaymentRequest trnPaymentRequest, TrnPaymentTransaction savedTrnPaymentTransaction,
			String laNumber) {
		String smsMessage = "Thank you for your payment of Rs. " + trnPaymentRequest.getAmount()
				+ " towards the LAN No. " + laNumber + ".\r\n" + "\r\n" + "For your reference, Transaction ID "
				+ trnPaymentRequest.getPaymentRequestId() + " and Payment ID " + savedTrnPaymentTransaction.getRazorPayPaymentId() + ".\r\n"
				+ "\r\n" + "Team Avanse";
		return smsMessage;
	}

	private String prepareEmailIdsAndMobileNumberForOverdue(TrnPaymentRequest trnPaymentRequest, String laNumber,
			StringBuffer emailIds, StringBuffer mobileNos, String customerName) {
		HttpHeaders headers = prepareHeader(trnPaymentRequest);
		PennantView[] pennantViewArray = preparePennantViewArray(laNumber, headers);
		for (int i = 0; i < pennantViewArray.length; i++) {
			if (pennantViewArray[i].getEMAIL_ID() != null && !"".equals(pennantViewArray[i].getEMAIL_ID().trim())) {
				emailIds.append(pennantViewArray[i].getEMAIL_ID() + ",");
			}

			if (pennantViewArray[i].getMOBILE_NUMBER() != null && !"".equals(pennantViewArray[i].getMOBILE_NUMBER().trim())) {
				mobileNos.append(pennantViewArray[i].getMOBILE_NUMBER() + "|");
			}

			if (pennantViewArray[i].getTYPE() != null && "APPLICANT".equals(pennantViewArray[i].getTYPE().toUpperCase())) {
				customerName = pennantViewArray[i].getCustomer_name();
			}
		}
		return customerName;
	}

	private PennantView[] preparePennantViewArray(String laNumber, HttpHeaders headers) {
		JSONObject inputObject = new JSONObject();

		JSONObject inputObject2 = new JSONObject();
		inputObject2.put("loanNo", laNumber);

		inputObject.put(ENCRYPTION_KEY, EncryptUtil.encryptString(inputObject2.toString(), ""));

		HttpEntity<String> request = new HttpEntity<String>(inputObject.toString(), headers);

		ResponseEntity<EncryptDataPojo> entity = restTemplate.postForEntity(getLoanDetails, request,
				EncryptDataPojo.class);

		EncryptDataPojo d = entity.getBody();

		String decryptString = EncryptUtil.decryptString(d.getEncryptedData(), "");

		ObjectMapper mapper = new ObjectMapper();
		PennantView[] e = null;

		try {
			e = mapper.readValue(decryptString, PennantView[].class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return e;
	}

	private HttpHeaders prepareHeader(TrnPaymentRequest trnPaymentRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Optional<MstSourceMapping> source = mstSourceMappingRepository.findBySourceId(trnPaymentRequest.getSourceId());
		if (source.isPresent()) {
			String auth = source.get().getSourceId() + ":" + source.get().getSecretKey();
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.add("Authorization", authHeader);
		} else {
			System.out.println("Source Id not available to create authorization");
		}
		return headers;
	}

	private String prepareEmailIdsAndMobileNumberForFees(TrnPaymentRequest trnPaymentRequest, 
			StringBuffer emailIds, StringBuffer mobileNos) {
		String customerName = "Customer";
		List<TrnFeePayment> trnFeePaymentList = trnFeePaymentRepository.findByPaymentRequestId(""+trnPaymentRequest.getPaymentRequestId());
		if(trnFeePaymentList != null && !trnFeePaymentList.isEmpty()) {
			for (TrnFeePayment trnFeePayment : trnFeePaymentList) {
				if (trnFeePayment.getEmailId() != null && !trnFeePayment.getEmailId().trim().equals("")) {
					emailIds.append(trnFeePayment.getEmailId() + ",");
				}
				if (trnFeePayment.getMobileNumber() != null && !trnFeePayment.getMobileNumber().trim().equals("")) {
					mobileNos.append(trnFeePayment.getMobileNumber() + "|");
				}
				if (trnFeePayment.getCustomerName() != null && !trnFeePayment.getCustomerName().trim().equals("")) {
					customerName = trnFeePayment.getCustomerName();
				}
			}
		}
		return customerName;
	}

	private void validateSignature(TrnPaymentTransaction trnPaymentTransaction) {
		String generated_signature = null;
		if (trnPaymentTransaction.getRazorPayOrderId() != null
				&& trnPaymentTransaction.getRazorPayPaymentId() != null) {
			try {
				generated_signature = hmac_sha256(
						trnPaymentTransaction.getRazorPayOrderId() + "|" + trnPaymentTransaction.getRazorPayPaymentId(),
						apiSecret);

			} catch (SignatureException e) {
				new AppException("SignatureException in payment success api:" + e.getLocalizedMessage());
			}
		} else {
			throw new AppException("Input razorPayOrderId or razorPayPaymentId is null or blank");
		}
		if(generated_signature == null || 
				!generated_signature.equals(trnPaymentTransaction.getRazorPaySignature())) {
			new AppException("Payment data save failed due to invalid razor pay signature!!!");
		}
	}

	private TrnPaymentRequest validateRequest(TrnPaymentTransaction trnPaymentTransaction) {
		TrnPaymentRequest trnPaymentRequest = null;
		Optional<TrnPaymentTransaction> trn = trnPaymentTransactionRepository.findByRazorPayOrderId(trnPaymentTransaction.getRazorPayOrderId());
		if (trn.isPresent()) {
			trnPaymentTransaction.setPaymentTransactionId(trn.get().getPaymentTransactionId());
		} else {
			throw new AppException("PaymentTransaction not available for orderId");
		}
		if (trnPaymentTransaction.getPaymentRequest() != null
				&& trnPaymentTransaction.getPaymentRequest().getPaymentRequestId() != 0) {
			Optional<TrnPaymentRequest> optional = trnPaymentRequestRepository.findById(trnPaymentTransaction.getPaymentRequest().getPaymentRequestId());
			if (optional.isPresent()) {
				trnPaymentRequest = optional.get();
				trnPaymentRequest.setRequestStatus("PAYMENT_COMPLETED");
				trnPaymentTransaction.setPaymentRequest(trnPaymentRequest);
			} else {
				throw new AppException("PaymentRequestID not available!!!");
			}
		} else {
			throw new AppException("PaymentRequestId is null or blank");
		}
		return trnPaymentRequest;
	}

	public Object addOrder(@RequestBody Order inOrder) {
		try {
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", inOrder.getAmount()); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("account_id", "acc_" + apiMerchantId);

			com.razorpay.Order order = razorpay.Orders.create(orderRequest);

			JSONObject jsonObject = order.toJson();

			inOrder.setOrderId(jsonObject.getString("id"));
			inOrder.setEntity(jsonObject.getString("entity"));
			inOrder.setAmount_paid(jsonObject.getLong("amount_paid"));
			inOrder.setAmount_due(jsonObject.getLong("amount_due"));
			// inOrder.setReceipt(jsonObject.get("receipt"));//!=null?jsonObject.getString("receipt"):null);
			inOrder.setStatus(jsonObject.get("status") != null ? jsonObject.getString("status") : null);
			inOrder.setAttempts(jsonObject.getInt("attempts"));
			inOrder.setCreated_at(jsonObject.getLong("created_at"));

			return inOrder;
		} catch (RazorpayException e) {
			System.out.println(e.getMessage());
			throw new AppException("RazorPayException in orderId generation");

		}
	}

	// @GetMapping("/testRazor")
	public Order testRazor(Order inOrder) {

		try {

			JSONObject options = new JSONObject();

			options.put("amount", Long.toString(inOrder.getAmount()));
			options.put("currency", "INR");
			options.put("payment_capture", "1");
			//options.put("account_id", "acc_" + apiMerchantId);


			/*For PROD*/
			String url = "https://api.razorpay.com/v1/orders";
			/*For UAT*/
			//String url = "https://prod-api-static.razorpay.com/v1/orders";
			String credentials = Credentials.basic(apiKey, apiSecret);

			okhttp3.MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");

			/*
			 * okhttp3.Authenticator proxyAuthenticator = new okhttp3.Authenticator() {
			 * public Request authenticate(Route route, Response response) throws
			 * IOException { String credential = Credentials.basic(proxy_user,
			 * proxy_password); return
			 * response.request().newBuilder().header("Proxy-Authorization",
			 * credential).build(); } };
			 */
			 

			/*
			 * OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60,
			 * TimeUnit.SECONDS) .writeTimeout(60, TimeUnit.SECONDS).readTimeout(60,
			 * TimeUnit.SECONDS) .proxy(new Proxy(Proxy.Type.HTTP, new
			 * InetSocketAddress(proxy_ip, proxy_port)))
			 * .proxyAuthenticator(proxyAuthenticator).build();
			 */
			 
			 			 
			 OkHttpClient client = new OkHttpClient(); 
			/*
			 * OkHttpClient client = new OkHttpClient.Builder() .connectTimeout(10000,
			 * TimeUnit.SECONDS) .readTimeout(10000,TimeUnit.SECONDS).build();;
			 */
			okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON, options.toString());
			okhttp3.Request request = new okhttp3.Request.Builder().url(url).addHeader("Authorization", credentials)
					.post(body).build();

			okhttp3.Response response = client.newCall(request).execute();

			String networkResp = response.body().string();

			System.out.println("order response:" + networkResp.toString());

			JSONObject jsonObject = new JSONObject(networkResp.toString());

			if (jsonObject.has("error")) {
				
				//System.out.println("inside has error object");
				//System.out.println(jsonObject.keys());
				JSONObject error = (JSONObject)jsonObject.get("error");
				//System.out.println("Inside error object");

				if (error.has("description")) {
					System.out.println("inside has description block");
					throw new AppException(error.getString("description"));
				}
			}

			inOrder.setOrderId(jsonObject.getString("id"));
			inOrder.setEntity(jsonObject.getString("entity"));
			inOrder.setAmount_paid(jsonObject.getLong("amount_paid"));
			inOrder.setAmount_due(jsonObject.getLong("amount_due"));
			// inOrder.setReceipt(jsonObject.get("receipt"));//!=null?jsonObject.getString("receipt"):null);
			inOrder.setStatus(jsonObject.get("status") != null ? jsonObject.getString("status") : null);
			inOrder.setAttempts(jsonObject.getInt("attempts"));
			inOrder.setCreated_at(jsonObject.getLong("created_at"));
			inOrder.setMerchantId(apiMerchantId);
			inOrder.setRazorPayKey(apiKey);
			return inOrder;

		} catch (Exception e) {
			// e.printStackTrace();
			// return e.getLocalizedMessage();
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new AppException("RazorPayException in orderId generation:" + e.getMessage());
		}

	}
	
	private MstSourceMapping fetchSourceFromDB(TrnPaymentRequest trnPaymentRequest) {
		Optional<MstSourceMapping> optional = mstSourceMappingRepository.findBySourceId(trnPaymentRequest.getSourceId());
		System.out.println("getDetailsAgainstLoanNumber" + 1);
		if (!optional.isPresent()) {
			throw new AppException("SourceId not available in master for sending SMS");
		}
		MstSourceMapping source = optional.get();
		return source;
	}
	
	@PostMapping("/partPayment_ForeclosureRequestDetails")
	public ResponseEntity<?> getRequestDetailsPartPayments(@RequestBody TrnClientDetailsRequest trnClientDetailsRequest) {
		if(trnClientDetailsRequest.getPaymentRequestId() == 0) {
			throw new AppException("Payment Request Id Missing");
		}
		TrnPaymentRequest trnPaymentRequest = new TrnPaymentRequest();
		trnPaymentRequest.setPaymentRequestId(trnClientDetailsRequest.getPaymentRequestId());
		TrnPaymentRequest requestDetailsPartPayments = paymentService.getRequestDetailsPartPayments(trnPaymentRequest);
		return ResponseEntity.ok(requestDetailsPartPayments);
	}

}
