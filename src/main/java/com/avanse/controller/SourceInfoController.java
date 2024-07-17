package com.avanse.controller;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.avanse.consumer.FinanceResponse;
import com.avanse.consumer.PennetAPI;
import com.avanse.exception.AppException;
import com.avanse.filters.EncryptDecryptFilter;
import com.avanse.filters.EncryptUtil;
import com.avanse.jpa.model.ApplicantDetailsResponse;
import com.avanse.jpa.model.MstBankBranchDetails;
import com.avanse.jpa.model.MstFeesDetails;
import com.avanse.jpa.model.MstSourceMapping;
import com.avanse.jpa.model.PaymentStatusRequest;
import com.avanse.jpa.model.PaymentStatusResponse;
import com.avanse.jpa.model.PrePaymentRequest;
import com.avanse.jpa.model.TrnDedupeHistory;
import com.avanse.jpa.model.TrnFeePayment;
import com.avanse.jpa.model.TrnPaymentRequest;
import com.avanse.jpa.model.TrnPaymentRequestDetails;
import com.avanse.jpa.model.TrnPaymentTransaction;
import com.avanse.jpa.repository.MstBankBranchMappingRepository;
import com.avanse.jpa.repository.MstCustomRepository;
import com.avanse.jpa.repository.MstFeesDeailsRepository;
import com.avanse.jpa.repository.MstFundAccountMappingRepository;
import com.avanse.jpa.repository.MstSourceMappingRepository;
import com.avanse.jpa.repository.TrnDedupeHistoryRepository;
import com.avanse.jpa.repository.TrnPaymentReceiptRepositry;
import com.avanse.jpa.repository.TrnPaymentRequestDetailsRepository;
import com.avanse.jpa.repository.TrnPaymentRequestRepository;
import com.avanse.jpa.repository.TrnPaymentTransactionRepository;
import com.avanse.model.EncryptDataPojo;
import com.avanse.model.PennantView;
import com.avanse.service.PaymentService;
import com.avanse.service.ServiceOperation;
import com.avanse.utility.UtilityClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/source")
public class SourceInfoController {
	@Autowired
	MstBankBranchMappingRepository mstBankBranchMappingRepository;

	@Autowired
	private TrnPaymentRequestRepository trnPaymentRequestRepository;

	@Autowired
	private TrnPaymentRequestDetailsRepository trnPaymentRequestDetailsRepository;

	@Autowired
	private TrnPaymentTransactionRepository trnPaymentTransactionRepository;

	@Autowired
	private TrnDedupeHistoryRepository trnDedupeHistoryRepository;

	@Autowired
	MstFundAccountMappingRepository mstFundAccountMappingRepository;

	Map<String, String> responseMap;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	MstSourceMappingRepository mstSourceMappingRepository;

//	@Autowired
//	private TrnFeePaymentRepository trnFeePaymentRepository;
	@Autowired
	PennetAPI pennetAPI;

	@Autowired
	TrnPaymentReceiptRepositry trnPaymentReceiptRepositry;

//	@Autowired
//	private TrnFeeDetailsMappingRepository trnFeeDetailsMappingRepository;
	
	@Autowired
	private MstFeesDeailsRepository mstFeesDeailsRepository;

//	@Autowired
//	private EmailService emailService;
//
//	@Autowired
//	private SmsService smsService;
	
	@Autowired
	private PaymentService paymentService;

	@Value("${isValidLANApi}")
	private String isValidLANApi;

	@Value("${overDueApi}")
	private String overDueApi;

	@Value("${feeAPI}")
	private String feeAPI;

	@Value("${getSystemDate}")
	private String getSystemDate_url;
	
	/*
	 * @Value("${smsContent}") private String smsContent;
	 */

	@Value("${emailSubject}")
	private String subject;

	/*
	 * @Value("${emailContent}") private String emailLContent;
	 */

	@Value("${bigPaymentURL}")
	private String bigPaymentURL;

	@Autowired
	private ServiceOperation serviceOperation;

	@Autowired
	private MstCustomRepository mstCustomRepository;

	@Value("${baseQuery}")
	private String baseQuery;

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@PostMapping("/requestId")
	public ResponseEntity<TrnPaymentRequest> getRequestId(@Valid @RequestBody TrnPaymentRequest trnPaymentRequest) {

		String mode = trnPaymentRequest.getMode().toUpperCase();

		// TODO

		JSONObject reqJSON = new JSONObject(trnPaymentRequest);
		System.out.println("input json:" + reqJSON.toString());

		JSONObject inputObject = new JSONObject();

		JSONObject overdueReq = new JSONObject();

		JSONObject jsonObject = null;

//		String mode = trnPaymentRequest.getMode().toUpperCase();
		StringBuffer errorMsg = new StringBuffer("");
		String requestPurpose = trnPaymentRequest.getRequestPurpose().toUpperCase();

		validateRequest(trnPaymentRequest, mode, errorMsg, requestPurpose);
		if (errorMsg.length() > 0) {
			throw new AppException("Validation Failed:" + errorMsg.toString());
		}

		System.out.println("requestPurpose:" + requestPurpose + "**MODE:" + trnPaymentRequest.getMode());

		/* Bank Code Validation */
		if ("RECEIPT".equals(requestPurpose)) {
			bankCodeValidation(trnPaymentRequest, mode);
		}
		/* Bank Code Validation */

		/* Payment Request Date Logic */
		long dateDiff = 0;
//		String systemDate;
		FinanceResponse systemDateResp = null;

		systemDateResp = pennetAPI.callRestTemplateForReceipting(getSystemDate_url, null, "systemDateService", HttpMethod.GET);

		String appDate = systemDateResp.getAppDate();
		System.out.println("appDate:" + appDate);
		System.out.println("Payment Request Date:" + trnPaymentRequest.getPaymentRequestDateTime());

		Date pennetDate;
		try {
			pennetDate = new SimpleDateFormat("yyyy-MM-dd").parse(appDate);
		} catch (ParseException e1) {
			throw new AppException("Exception getting system date:" + e1.getMessage());
		}
		Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(trnPaymentRequest.getPaymentRequestDateTime());
			date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		} catch (ParseException e) {
			throw new AppException("Please check the paymentRequestDateTime");
		}
		dateDiff = (date.getTime() - pennetDate.getTime()) / 86400000;
		System.out.println("PennetDate:" + pennetDate + "***SystemDate:" + date + "***Date Diff:" + dateDiff);
		if (dateDiff < -29) {
			throw new AppException("Payment is not able to processes due Pennet Date policy");
		} else if (dateDiff < 0) {
		} else {
			trnPaymentRequest.setPaymentRequestDateTime(pennetDate);
		}
		System.out.println("PennetDate:" + pennetDate + "***SystemDate:" + date + "***Date Diff:" + dateDiff);

		System.out.println("Date Logic Finished");
		/* Payment Request Date Logic */

		/* Value Date Logic */
		if (mode.equals("CASH") || mode.equals("CHEQUE") || mode.equals("DD")) {
			System.out.println("VALUE DATE:" + trnPaymentRequest.getValueDate());
			if (trnPaymentRequest.getValueDate() != null) {
				try {
					pennetDate = new SimpleDateFormat("yyyy-MM-dd").parse(appDate);
				} catch (ParseException e1) {
					throw new AppException("Exception getting system date:" + e1.getMessage());
				}
				date = null;
				try {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String strDate = dateFormat.format(trnPaymentRequest.getValueDate());
					date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
				} catch (ParseException e) {
					throw new AppException("Please check the paymentRequestDateTime");
				}
				dateDiff = (date.getTime() - pennetDate.getTime()) / 86400000;
				System.out.println("PennetDate:" + pennetDate + "***SystemDate:" + date + "***Date Diff:" + dateDiff);
				if (dateDiff < -29) {
					throw new AppException("Payment is not able to processes due Pennet Date policy");
				} else if (dateDiff < 0) {
				} else {
					trnPaymentRequest.setValueDate(pennetDate);
				}
				System.out.println("PennetDate:" + pennetDate + "***SystemDate:" + date + "***Date Diff:" + dateDiff);

				System.out.println("Date Logic Finished");
			}
		}
		/* Value Date Logic */

		// Dedupe logic to check payment reference
		System.out.println("retran:" + trnPaymentRequest.getRefTransaction() + "+Paymentref:" + trnPaymentRequest.getPaymentRef());
		System.out.println("Dedupe Started");
		if ("RECEIPT".equals(requestPurpose) && trnPaymentRequest.getRefTransaction() != null && !"".equals(trnPaymentRequest.getRefTransaction().trim())) {
			dedupLogic(trnPaymentRequest);
		}

		System.out.println("Dedupe Completed");

		String laNumber = trnPaymentRequest.getLaNumber();
		Double allowed_amt = null;
		Double availCashLimit = null;
		Double total_amt = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println("1");
		Optional<MstSourceMapping> source = mstSourceMappingRepository.findBySourceId(trnPaymentRequest.getSourceId());
		System.out.println("2");

		if (source.isPresent()) {

			preparePennantHeader(headers, source);

			// Base64.getEncoder().en(mstSourceMapping.get().getSourceId()+":"+mstSourceMapping.get().getSecretKey());
		} else {
			throw new AppException("SourceId not available in master for generation of token for elmspennantviews api");
		}

		overdueReq.put("loanNo", laNumber);

		inputObject.put(EncryptDecryptFilter.ENCRYPTION_KEY, EncryptUtil.encryptString(overdueReq.toString(), ""));

		HttpEntity<String> request = new HttpEntity<String>(inputObject.toString(), headers);

		if (trnPaymentRequest.getPaymentPurpose().equals("OVERDUE")) {
			System.out.println("3");
			ResponseEntity<EncryptDataPojo> resp = restTemplate.postForEntity(isValidLANApi, request, EncryptDataPojo.class);
			System.out.println("4");

			EncryptDataPojo encryptDataPojo = resp.getBody();

			String decryptString = EncryptUtil.decryptString(encryptDataPojo.getEncryptedData(), "");

			jsonObject = new JSONObject(decryptString);

			boolean isValidLAN = jsonObject.getBoolean("valid");

			if (isValidLAN == false) {
				throw new AppException("Invalid LAN Number");
			}
		}

		String api = null;
		if (trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("OVERDUE")) {
			System.out.println("Inside overdue api condition");
			api = overDueApi;
			ResponseEntity<EncryptDataPojo> loanDetails = restTemplate.postForEntity(api, request, EncryptDataPojo.class);

			EncryptDataPojo pojo = loanDetails.getBody();

			String decryptedLoanDetails = EncryptUtil.decryptString(pojo.getEncryptedData(), "");

			// JSONObject o2=new JSONObject(decryptedLoanDetails);

			ObjectMapper mapper = new ObjectMapper();

			/*
			 * if (trnPaymentRequest.getLaNumber().contains("/")) {
			 * System.out.println("Inside trnPaymentRequest.getLaNumber().contains(\"/\")");
			 * ELMSView elmsView = null; try { elmsView =
			 * mapper.readValue(decryptedLoanDetails, ELMSView.class);
			 * System.out.println("ELMS View:"+elmsView); } catch (JsonProcessingException
			 * e1) { e1.printStackTrace(); }
			 * 
			 * if (trnPaymentRequest.getLoanType() == null) {
			 * trnPaymentRequest.setLoanType(elmsView.getPROD_TYPE()); }
			 * 
			 * if(elmsView.getEMI_SI_PI_AMOUNT()==null || elmsView.getEMI_SI_PI_AMOUNT()==0)
			 * {
			 * 
			 * total_amt=elmsView.getCURRENT_MONTH_INST()+elmsView.getBOUNCE_CHARGE() +
			 * elmsView.getPENAL_CHARGE(); allowed_amt = elmsView.getBOUNCE_CHARGE() +
			 * elmsView.getPENAL_CHARGE() + (3 * elmsView.getCURRENT_MONTH_INST());
			 * 
			 * }else {
			 * 
			 * total_amt = elmsView.getEMI_SI_PI_AMOUNT() +elmsView.getBOUNCE_CHARGE() +
			 * elmsView.getPENAL_CHARGE(); allowed_amt = elmsView.getTOTAL_AMOUNT() + (3 *
			 * elmsView.getCURRENT_MONTH_INST()); }
			 * 
			 * trnPaymentRequest.setPenalCharge(elmsView.getPENAL_CHARGE());
			 * trnPaymentRequest.setBounceCharge(elmsView.getBOUNCE_CHARGE());
			 * availCashLimit=elmsView.getAvailableCashLimit();
			 * 
			 * } else {
			 */
			System.out.println("Inside Pennent view");
			PennantView e = null;
			try {
				e = mapper.readValue(decryptedLoanDetails, PennantView.class);
				System.out.println("Pennant View:" + e);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (trnPaymentRequest.getLoanType() == null) {
				trnPaymentRequest.setLoanType(e.getPROD_TYPE());
			}

			if (e.getEMI_SI_PI_AMOUNT() == 0) {
				total_amt = e.getCURRENT_MONTH_INST() + e.getBOUNCE_CHARGE() + e.getPENAL_CHARGE();
				allowed_amt = e.getBOUNCE_CHARGE() + e.getPENAL_CHARGE() + (2 * e.getCURRENT_MONTH_INST());
			} else {
				total_amt = e.getEMI_SI_PI_AMOUNT() + e.getBOUNCE_CHARGE() + e.getPENAL_CHARGE();
				allowed_amt = e.getTOTAL_AMOUNT() + (2 * e.getCURRENT_MONTH_INST());
			}

			trnPaymentRequest.setPenalCharge(e.getPENAL_CHARGE());
			trnPaymentRequest.setBounceCharge(e.getBOUNCE_CHARGE());
			availCashLimit = e.getAvailableCashLimit();
			/* } */
		} else if (trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("FEES")) {
			System.out.println("Purpose is FEES");
		}

		if (trnPaymentRequest.getLoanType() != null) {
			boolean fundAccExists = mstFundAccountMappingRepository.existsByLoanTypeAndPaymentPurpose(trnPaymentRequest.getLoanType(), trnPaymentRequest.getPaymentPurpose());

			if (!fundAccExists) {
				throw new AppException("Fund Account mapping not available in database master against Loan Type:" + trnPaymentRequest.getLoanType() + " and PaymentPurpose:" + trnPaymentRequest.getPaymentPurpose());
			}
		}

		if (trnPaymentRequest.getAmount() == null) {
			trnPaymentRequest.setAmount(total_amt);
		} else {
			if ("RECEIPT".equals(requestPurpose) && "CASH".equals(trnPaymentRequest.getMode()) && trnPaymentRequest.getAmount().compareTo(availCashLimit) > 0) {
				throw new AppException("Cash limit exceeded. AvailableCashLimit=" + availCashLimit);
			}
		}
		trnPaymentRequest.setTotalAmount(total_amt);
		if (trnPaymentRequest.getCurrency() == null) {
			trnPaymentRequest.setCurrency("INR");
		}

		if ("Receipt".equals(trnPaymentRequest.getRequestPurpose())) {
			trnPaymentRequest.setRequestStatus("PAYMENT_COMPLETED");
		} else {
			trnPaymentRequest.setRequestStatus("NEW_REQUEST");
		}

		if (trnPaymentRequest.getAmount() <= 0) {
			throw new AppException("Amount should be greater than 0");
		}
		// Dedupe logic to check payment reference
		System.out.println("retran:" + trnPaymentRequest.getRefTransaction() + "+Paymentref:" + trnPaymentRequest.getPaymentRef());
		System.out.println("Dedupe Started");
		if ("RECEIPT".equals(requestPurpose) && trnPaymentRequest.getRefTransaction() != null && !"".equals(trnPaymentRequest.getRefTransaction().trim())) {
			dedupLogic(trnPaymentRequest);
		}

		System.out.println("Dedupe Completed");

		TrnPaymentRequest response = trnPaymentRequestRepository.save(trnPaymentRequest);

		if ("Receipt".equals(trnPaymentRequest.getRequestPurpose())) {
			// trnPaymentRequest.setRequestStatus("PAYMENT_COMPLETED");

			TrnPaymentTransaction trnPaymentTransaction = new TrnPaymentTransaction(null, trnPaymentRequest.getRefTransaction(), null, null, null);
			trnPaymentTransaction.setPaymentRequest(response);
			try {
				trnPaymentTransactionRepository.save(trnPaymentTransaction);
			} catch (Exception e) {
				throw new AppException("Error occurred in saving referenced transaction id for receipt request purpose");
			}
		}

		List<TrnPaymentRequestDetails> list = trnPaymentRequest.getCharges();

		if (list != null) {
			list.forEach(det -> {
				det.setTrnPaymentRequest(response);
			});
			System.out.println("5");

			trnPaymentRequestDetailsRepository.saveAll(list);
			System.out.println("6");
		}

		if (response == null) {
			throw new AppException("Payment request ID is not generated");
		} else {
			

			int requestId = response.getPaymentRequestId();
			TrnPaymentRequest tpr = new TrnPaymentRequest();
			tpr.setPaymentRequestId(requestId);

			// Start-Code to save successful request and response json of payment and
			// overdue api call
			response.setPaymentRequestJson(reqJSON != null ? reqJSON.toString() : null);
			response.setOverdueAPIRequestJson(overdueReq != null ? overdueReq.toString() : null);
			response.setOverdueAPIResponseJson(jsonObject != null ? jsonObject.toString() : null);

			JSONObject respJSON = new JSONObject(tpr);
			System.out.println("output json:" + respJSON.toString());

			response.setPaymentResponseJson(respJSON != null ? respJSON.toString() : null);
			System.out.println("7");
			
			// Dedupe logic to check payment reference
			/*
			 * System.out.println("retran:" + trnPaymentRequest.getRefTransaction() +
			 * "+Paymentref:" + trnPaymentRequest.getPaymentRef());
			 * System.out.println("Dedupe Started Before Saving"); if
			 * ("RECEIPT".equals(requestPurpose) && trnPaymentRequest.getRefTransaction() !=
			 * null && !"".equals(trnPaymentRequest.getRefTransaction().trim())) {
			 * dedupLogic(trnPaymentRequest); }
			 * 
			 * System.out.println("Dedupe Completed Before Saving");
			 */


			trnPaymentRequestRepository.save(response);
			System.out.println("8");

			// End-Code to save successful request and response json of payment and overdue
			// api call
			if (trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("Fees")) {
				String bigURL = bigPaymentURL.replace("<REQUEST_ID>", requestId + "")
						.replace("<PAYMENT_PURPOSE>", "Fees");
				System.out.println(bigURL);
				String shortURL = serviceOperation.generateTinyURL(bigURL, trnPaymentRequest.getLaNumber());
				System.out.println(shortURL);

				System.out.println("41");
				TrnFeePayment trnFeePayment = paymentService.saveTrnFeePaymentDetails(trnPaymentRequest, requestId, 
						bigURL, shortURL);
				System.out.println("42");
				paymentService.saveFeeCodeDetails(trnPaymentRequest, trnFeePayment);

				paymentService.sendSMSandEmailForFess(trnPaymentRequest, tpr, shortURL, trnFeePayment);
			}
			return ResponseEntity.ok(tpr);

		}

	}

	private void validateRequest(TrnPaymentRequest trnPaymentRequest, String mode, StringBuffer errorMsg, String requestPurpose) {
		switch (requestPurpose) {
		case "RECEIPT":

			if (trnPaymentRequest.getAmount() == null || trnPaymentRequest.getAmount() == 0) {
				errorMsg.append("Amount is mandatory in request purpose RECEIPT\n");
			}
			if (trnPaymentRequest.getAmount() <= 0) {
				errorMsg.append("Amount should be greater than 0 in request purpose RECEIPT\n");
			}
			System.out.println("Payment Mode:" + mode);
			if (trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("OVERDUE")) {
				switch (mode) {
				case "NEFT":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode NEFT\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode NEFT\n");
					}

					break;
				case "RTGS":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode RTGS\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode RTGS\n");
					}

					break;
				case "NET BANKING":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode NET BANKING\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode NET BANKING\n");
					}

					break;
				case "UPI":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode UPI\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode UPI\n");
					}

					break;
				case "WALLET":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Wallet\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Wallet\n");
					}

					break;
				case "DEBIT CARD":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Debit Card\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Debit Card\n");
					}

					break;
				case "DEBIT CARD – RUPAY":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Debit Card – Rupay\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Debit Card – Rupay\n");
					}

					break;
				case "PREPAID_CARD":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Prepaid _Card\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Prepaid _Card\n");
					}

					break;
				case "IMPS":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode IMPS\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode IMPS\n");
					}

					break;
				case "ACCOUNT_TRANSFER":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Account_Transfer\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Account_Transfer\n");
					}

					break;
				case "AEPS":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode AEPS\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode AEPS\n");
					}

					break;
				case "INTERNET BANKING":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Internet Banking\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Internet Banking\n");
					}

					break;
				case "BHARAT_QR":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Internet Banking\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Internet Banking\n");
					}

					break;
				case "USSD":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode Internet Banking\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode Internet Banking\n");
					}

					break;

				case "CASH":
					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode CASH\n");
					}

					break;
				case "CHEQUE":
				case "DD":

					if (trnPaymentRequest.getPaymentRef() == null || "".equals(trnPaymentRequest.getPaymentRef().trim())) {
						errorMsg.append("PaymentRef is mandatory in payment mode CHEQUE/DD\n");
					}

					if (trnPaymentRequest.getRefTransaction() == null || "".equals(trnPaymentRequest.getRefTransaction().trim())) {
						errorMsg.append("RefTransaction is mandatory in payment mode CHEQUE/DD\n");
					}

					if (trnPaymentRequest.getValueDate() == null) {
						errorMsg.append("ValueDate is mandatory in payment mode CHEQUE/DD\n");
					}

					if (trnPaymentRequest.getFavourName() == null || "".equals(trnPaymentRequest.getFavourName())) {
						errorMsg.append("FavorName is mandatory in payment mode CHEQUE/DD\n");
					}

					break;

				default:
					throw new AppException("Invalid mode");
				}
			} else if (trnPaymentRequest.getPaymentPurpose().equalsIgnoreCase("FEES")) {
				switch (mode) {
				case "NEFT":
					break;
				case "RTGS":
					break;
				case "NET BANKING":
					break;
				case "UPI":
					break;
				case "WALLET":
					break;
				case "DEBIT CARD":
					break;
				case "DEBIT CARD – RUPAY":
					break;
				case "PREPAID_CARD":
					break;
				case "IMPS":
					break;
				case "ACCOUNT_TRANSFER":
					break;
				case "AEPS":
					break;
				case "INTERNET BANKING":
					break;
				case "BHARAT_QR":
					break;
				case "USSD":
					break;
				case "CASH":
					if (trnPaymentRequest.getValueDate() == null) {
						errorMsg.append("ValueDate is mandatory in payment mode CASH\n");
					}
					break;
				case "CHEQUE":
					if (trnPaymentRequest.getValueDate() == null) {
						errorMsg.append("ValueDate is mandatory in payment mode CHEQUE/DD\n");
					}
					break;
				case "DD":
					if (trnPaymentRequest.getValueDate() == null) {
						errorMsg.append("ValueDate is mandatory in payment mode CHEQUE/DD\n");
					}

					if (trnPaymentRequest.getFavourName() == null || "".equals(trnPaymentRequest.getFavourName())) {
						errorMsg.append("FavorName is mandatory in payment mode CHEQUE/DD\n");
					}

					break;

				default:
					throw new AppException("Invalid mode");
				}
			} else {
				throw new AppException("Invalid Payment Purpose");
			}

		case "PAYMENT":
			break;
		default:
			throw new AppException("Invalid Request purpose");
		}
	}

	private void bankCodeValidation(TrnPaymentRequest trnPaymentRequest, String mode) {
		if ((trnPaymentRequest.getBankCode() == null || trnPaymentRequest.getBankCode().trim().equals("")) && (mode.equals("DD") || mode.equals("CHEQUE"))) {

			if ((trnPaymentRequest.getMicr() == null && trnPaymentRequest.getIfsc() == null)) {
				throw new AppException("Validation Failed:MICR and IFSC code both can not be blank");
			}
			if (trnPaymentRequest.getIfsc() == null || trnPaymentRequest.getIfsc().trim().equals("")) {
				List<MstBankBranchDetails> mstBankBranchDetails = mstBankBranchMappingRepository.findByMicr(trnPaymentRequest.getMicr());
				// trnPaymentRequest.setBankCode(mstBankBranchDetails.getBranchCode());
				if (mstBankBranchDetails.size() == 0) {
					throw new AppException("Validation Failed:MICR Code is not found in Bank Master");
				}
				for (MstBankBranchDetails bankBranchDetail : mstBankBranchDetails) {
					trnPaymentRequest.setBankCode(bankBranchDetail.getBankCode());
				}
			} else if (trnPaymentRequest.getMicr() == null || trnPaymentRequest.getMicr().trim().equals("")) {
				List<MstBankBranchDetails> mstBankBranchDetails = mstBankBranchMappingRepository.findByIfsc(trnPaymentRequest.getIfsc());
				// trnPaymentRequest.setBankCode(mstBankBranchDetails.getBranchCode());
				if (mstBankBranchDetails.size() == 0) {
					throw new AppException("Validation Failed:IFSC Code is not found in Bank Master");
				}
				for (MstBankBranchDetails bankBranchDetail : mstBankBranchDetails) {
					trnPaymentRequest.setBankCode(bankBranchDetail.getBankCode());
				}
			} else {
				List<MstBankBranchDetails> mstBankBranchDetails = mstBankBranchMappingRepository.findByMicrAndIfsc(trnPaymentRequest.getMicr(), trnPaymentRequest.getIfsc());
				if (mstBankBranchDetails.size() == 0) {
					throw new AppException("Validation Failed:MICR and IFSC Code is not found in Bank Master");
				}
				// trnPaymentRequest.setBankCode(mstBankBranchDetails.getBranchCode());
				for (MstBankBranchDetails bankBranchDetail : mstBankBranchDetails) {
					trnPaymentRequest.setBankCode(bankBranchDetail.getBankCode());
				}
			}
		} else {
			List<MstBankBranchDetails> mstBankBranchDetails = mstBankBranchMappingRepository
					.findByIfsc(trnPaymentRequest.getBankCode());
			if (mstBankBranchDetails.size() > 0) {
				for (MstBankBranchDetails bankBranchDetail : mstBankBranchDetails) {
					trnPaymentRequest.setBankCode(bankBranchDetail.getBankCode());
				}
			} else {
				List<MstBankBranchDetails> mstBankBranchDetails1 = mstBankBranchMappingRepository
						.findByMicr(trnPaymentRequest.getBankCode());
				if (mstBankBranchDetails1.size() == 0) {
					throw new AppException("Validation Failed:Bank Code is not found in Bank Master");
				} else {
					for (MstBankBranchDetails bankBranchDetail : mstBankBranchDetails1) {
						trnPaymentRequest.setBankCode(bankBranchDetail.getBankCode());
					}
				}
			}

		}

	}

	private void dedupLogic(TrnPaymentRequest trnPaymentRequest) {
		boolean reTransCheck = false;
		boolean paymentRefCheck = false;
		boolean pennentRecieptCheck = false;
		boolean razorPayOrderIdCheck = false;
		boolean razorPayPaymentIdCheck = false;
		boolean authCodeCheck = false;

		/* RefTransaction Dedupe */
		if (trnPaymentRequest.getRefTransaction() != null) {
			String refTransNo = trnPaymentRequest.getRefTransaction();
			reTransCheck = trnPaymentRequestRepository.existsByRefTransaction(refTransNo);
			paymentRefCheck = trnPaymentRequestRepository.existsByPaymentRef(refTransNo);
			razorPayOrderIdCheck = trnPaymentTransactionRepository.existsByRazorPayOrderId(refTransNo);
			razorPayPaymentIdCheck = trnPaymentTransactionRepository.existsByRazorPayPaymentId(refTransNo);
			authCodeCheck = trnPaymentTransactionRepository.existsByAuthCode(refTransNo);
			pennentRecieptCheck = trnPaymentReceiptRepositry.existsByPennantReceiptNo(refTransNo);
			if (reTransCheck || paymentRefCheck || razorPayOrderIdCheck || razorPayPaymentIdCheck || authCodeCheck || pennentRecieptCheck) {
				TrnDedupeHistory tdh = new TrnDedupeHistory(trnPaymentRequest.getLaNumber(), refTransNo, trnPaymentRequest.getPaymentPurpose(), trnPaymentRequest.getMode(), new Date(), trnPaymentRequest.getSourceId());
				trnDedupeHistoryRepository.saveAndFlush(tdh);
				throw new AppException("Duplicate Transaction");
			}
		}
		/* RefTransaction Dedupe */

		/* PaymentRef Dedupe */
		if (trnPaymentRequest.getPaymentRef() != null) {
			String paymentRef = trnPaymentRequest.getPaymentRef();
			reTransCheck = trnPaymentRequestRepository.existsByRefTransaction(paymentRef);
			paymentRefCheck = trnPaymentRequestRepository.existsByPaymentRef(paymentRef);
			razorPayOrderIdCheck = trnPaymentTransactionRepository.existsByRazorPayOrderId(paymentRef);
			razorPayPaymentIdCheck = trnPaymentTransactionRepository.existsByRazorPayPaymentId(paymentRef);
			authCodeCheck = trnPaymentTransactionRepository.existsByAuthCode(paymentRef);
			pennentRecieptCheck = trnPaymentReceiptRepositry.existsByPennantReceiptNo(paymentRef);
			if (reTransCheck || paymentRefCheck || razorPayOrderIdCheck || razorPayPaymentIdCheck || authCodeCheck || pennentRecieptCheck) {
				TrnDedupeHistory tdh = new TrnDedupeHistory(trnPaymentRequest.getLaNumber(), trnPaymentRequest.getRefTransaction(), trnPaymentRequest.getPaymentPurpose(), trnPaymentRequest.getMode(), new Date(), trnPaymentRequest.getSourceId());
				trnDedupeHistoryRepository.saveAndFlush(tdh);
				throw new AppException("Duplicate Transaction");
			}
		}
		/* PaymentRef Dedupe */

		/* RazorPayOrderId Dedupe */
		if (trnPaymentRequest.getTrnPaymentTransaction() != null) {
			if (trnPaymentRequest.getTrnPaymentTransaction().getRazorPayOrderId() != null) {
				String razorPayOrderId = trnPaymentRequest.getTrnPaymentTransaction().getRazorPayOrderId();
				reTransCheck = trnPaymentRequestRepository.existsByRefTransaction(razorPayOrderId);
				paymentRefCheck = trnPaymentRequestRepository.existsByPaymentRef(razorPayOrderId);
				razorPayOrderIdCheck = trnPaymentTransactionRepository.existsByRazorPayOrderId(razorPayOrderId);
				razorPayPaymentIdCheck = trnPaymentTransactionRepository.existsByRazorPayPaymentId(razorPayOrderId);
				authCodeCheck = trnPaymentTransactionRepository.existsByAuthCode(razorPayOrderId);
				pennentRecieptCheck = trnPaymentReceiptRepositry.existsByPennantReceiptNo(razorPayOrderId);
				if (reTransCheck || paymentRefCheck || razorPayOrderIdCheck || razorPayPaymentIdCheck || authCodeCheck || pennentRecieptCheck) {
					TrnDedupeHistory tdh = new TrnDedupeHistory(trnPaymentRequest.getLaNumber(), trnPaymentRequest.getRefTransaction(), trnPaymentRequest.getPaymentPurpose(), trnPaymentRequest.getMode(), new Date(), trnPaymentRequest.getSourceId());
					trnDedupeHistoryRepository.saveAndFlush(tdh);
					throw new AppException("Duplicate Transaction");
				}
			}
			/* RazorPayOrderId Dedupe */

			/* RazorPayPaymentId Dedupe */
			if (trnPaymentRequest.getTrnPaymentTransaction().getRazorPayPaymentId() != null) {
				String razorPayPaymentId = trnPaymentRequest.getTrnPaymentTransaction().getRazorPayPaymentId();
				reTransCheck = trnPaymentRequestRepository.existsByRefTransaction(razorPayPaymentId);
				paymentRefCheck = trnPaymentRequestRepository.existsByPaymentRef(razorPayPaymentId);
				razorPayOrderIdCheck = trnPaymentTransactionRepository.existsByRazorPayOrderId(razorPayPaymentId);
				razorPayPaymentIdCheck = trnPaymentTransactionRepository.existsByRazorPayPaymentId(razorPayPaymentId);
				authCodeCheck = trnPaymentTransactionRepository.existsByAuthCode(razorPayPaymentId);
				pennentRecieptCheck = trnPaymentReceiptRepositry.existsByPennantReceiptNo(razorPayPaymentId);
				if (reTransCheck || paymentRefCheck || razorPayOrderIdCheck || razorPayPaymentIdCheck || authCodeCheck || pennentRecieptCheck) {
					TrnDedupeHistory tdh = new TrnDedupeHistory(trnPaymentRequest.getLaNumber(), trnPaymentRequest.getRefTransaction(), trnPaymentRequest.getPaymentPurpose(), trnPaymentRequest.getMode(), new Date(), trnPaymentRequest.getSourceId());
					trnDedupeHistoryRepository.saveAndFlush(tdh);
					throw new AppException("Duplicate Transaction");
				}
			}
			/* RazorPayPaymentId Dedupe */

			/* AuthCode Dedupe */
			if (trnPaymentRequest.getTrnPaymentTransaction().getAuthCode() != null) {
				String razorPayAuthCode = trnPaymentRequest.getTrnPaymentTransaction().getAuthCode();
				reTransCheck = trnPaymentRequestRepository.existsByRefTransaction(razorPayAuthCode);
				paymentRefCheck = trnPaymentRequestRepository.existsByPaymentRef(razorPayAuthCode);
				razorPayOrderIdCheck = trnPaymentTransactionRepository.existsByRazorPayOrderId(razorPayAuthCode);
				razorPayPaymentIdCheck = trnPaymentTransactionRepository.existsByRazorPayPaymentId(razorPayAuthCode);
				authCodeCheck = trnPaymentTransactionRepository.existsByAuthCode(razorPayAuthCode);
				pennentRecieptCheck = trnPaymentReceiptRepositry.existsByPennantReceiptNo(razorPayAuthCode);
				if (reTransCheck || paymentRefCheck || razorPayOrderIdCheck || razorPayPaymentIdCheck || authCodeCheck || pennentRecieptCheck) {
					TrnDedupeHistory tdh = new TrnDedupeHistory(trnPaymentRequest.getLaNumber(), trnPaymentRequest.getRefTransaction(), trnPaymentRequest.getPaymentPurpose(), trnPaymentRequest.getMode(), new Date(), trnPaymentRequest.getSourceId());
					trnDedupeHistoryRepository.saveAndFlush(tdh);
					throw new AppException("Duplicate Transaction");
				}
			}
		}
		/* RazorPayPaymentId Dedupe */
	}

	@PostMapping("/getPaymentStatus")
	public ResponseEntity<?> getPaymentStatus(@Valid @RequestBody PaymentStatusRequest paymentStatusRequest) {
		System.out.println("Get Payment status request:" + paymentStatusRequest);
		String query = "";
		List<PaymentStatusResponse> paymentStatusResponses = new ArrayList<PaymentStatusResponse>();
		if (paymentStatusRequest.getLaNumber() != null && !paymentStatusRequest.getLaNumber().trim().equals("")) {
			query = baseQuery + " WHERE r.LANumber = '" + paymentStatusRequest.getLaNumber() + "'";
		} else if (paymentStatusRequest.getPaymentRequestId() != 0) {
			query = baseQuery + " WHERE r.PaymentRequestID = " + paymentStatusRequest.getPaymentRequestId();
		} else if (paymentStatusRequest.getStartDate() != null && paymentStatusRequest.getEndDate() != null) {
			query = baseQuery + " WHERE PaymentRequestDateTime BETWEEN  '" + simpleDateFormat.format(paymentStatusRequest.getStartDate()) + "' AND '" + simpleDateFormat.format(paymentStatusRequest.getEndDate()) + "'";
			System.out.println(query);
		}
		paymentStatusResponses.addAll(mstCustomRepository.getData(query));
		return ResponseEntity.ok(paymentStatusResponses);
	}
	
	@PostMapping("/getApplicantsDetails")
	public @ResponseBody ResponseEntity<?> getDetailsAgainstLoanNumber(@RequestBody TrnPaymentRequest trnPaymentRequest) {
		if (UtilityClass.isEmptyString(trnPaymentRequest.getLaNumber())) {
			throw new AppException("Loan Number Missing");
		} else if (UtilityClass.isEmptyString(trnPaymentRequest.getPaymentPurpose())) {
			throw new AppException("Payment Purpose Missing");
		}
		ApplicantDetailsResponse applicantDetailsResponse = null;
		String paymentPurpose = trnPaymentRequest.getPaymentPurpose().toUpperCase();
		if (paymentPurpose.equals("PARTPAYMENT") || paymentPurpose.equals("FORECLOSURE") || paymentPurpose.equals("OVERDUE")) {
			applicantDetailsResponse = paymentService.getDetailsAgainstLoanNumber(trnPaymentRequest);
		} else if (paymentPurpose.equals("FEES")) {
			applicantDetailsResponse = paymentService.getDetailsAgainstLoanNumberForFee(trnPaymentRequest);
		}
		return ResponseEntity.ok(applicantDetailsResponse);
	}
	
	@PostMapping("/agentLinkProcessing")
	public @ResponseBody ResponseEntity<?> paymentProcessing(@RequestBody 
	PrePaymentRequest prePaymentRequest) {
		System.out.println("agentLinkProcessing PrePaymentRequest"+prePaymentRequest);
		if(UtilityClass.isEmptyString(prePaymentRequest.getPaymentPurpose())) {
			throw new AppException("Kindly provide the payment purpose.");
		}
		String paymentPurpose = prePaymentRequest.getPaymentPurpose().toUpperCase();
		TrnPaymentRequest trnPaymentRequest = generateTrnPaymentRequest(prePaymentRequest);
		trnPaymentRequest.setAgentRequest(true);
		System.out.println("agentLinkProcessing======"+trnPaymentRequest);
		long requestId = 0;
		String shortLink = "";
		if(paymentPurpose.equals("OVERDUE")) {
			System.out.println("agentLinkProcessing "+1);
			ResponseEntity<TrnPaymentRequest> trnResponse = getRequestId(trnPaymentRequest);
			System.out.println("agentLinkProcessing "+2);
			requestId = trnResponse.getBody().getPaymentRequestId();
			System.out.println("agentLinkProcessing "+3);
			System.out.println("agentLinkProcessing requestId"+requestId);
			shortLink = paymentService.prepareShortURLAndSmsEmail(trnPaymentRequest, prePaymentRequest, requestId);
		}else if(paymentPurpose.equals("PARTPAYMENT") || paymentPurpose.equals("FORECLOSURE")) {
			trnPaymentRequest = paymentService.paymentProcessing(trnPaymentRequest, prePaymentRequest);
			requestId = trnPaymentRequest.getPaymentRequestId();
			shortLink = paymentService.prepareShortURLAndSmsEmail(trnPaymentRequest, prePaymentRequest, requestId);
		}else if(paymentPurpose.equals("FEES")) {
			trnPaymentRequest = paymentService.paymentProcessingForFees(trnPaymentRequest, prePaymentRequest);
			requestId = trnPaymentRequest.getPaymentRequestId();
			shortLink = paymentService.prepareShortURLAndSmsEmail(trnPaymentRequest, prePaymentRequest, requestId);
		}
		TrnPaymentRequest tmp = new TrnPaymentRequest();
		tmp.setPaymentRequestId((int) requestId);
		tmp.setShortURL(shortLink);
		return ResponseEntity.ok(tmp);
	}

	private TrnPaymentRequest generateTrnPaymentRequest(PrePaymentRequest prePaymentRequest) {
		TrnPaymentRequest trnPaymentRequest = new TrnPaymentRequest();
		trnPaymentRequest.setLaNumber(prePaymentRequest.getLaNumber());
		trnPaymentRequest.setRequestPurpose(prePaymentRequest.getRequestPurpose());
		trnPaymentRequest.setPaymentPurpose(prePaymentRequest.getPaymentPurpose());
		trnPaymentRequest.setSourceId(prePaymentRequest.getSourceId());
		trnPaymentRequest.setMode(prePaymentRequest.getMode());
		trnPaymentRequest.setUserId(prePaymentRequest.getUserId());
		trnPaymentRequest.setSessionId(prePaymentRequest.getSessionId());
		trnPaymentRequest.setCrmSrNo(prePaymentRequest.getCrmSrNo());
		String currency = prePaymentRequest.getCurrency();
		currency = currency != null ? currency : "INR";
		trnPaymentRequest.setCurrency(currency);
		trnPaymentRequest.setAmount(prePaymentRequest.getAmount());
		trnPaymentRequest.setTotalAmount(prePaymentRequest.getTotalAmount());
		trnPaymentRequest.setLoanType(prePaymentRequest.getLoanType());
		trnPaymentRequest.setFeeType(prePaymentRequest.getFeeType());
		return trnPaymentRequest;
	}

	private void preparePennantHeader(HttpHeaders pennantHeaderApi, Optional<MstSourceMapping> source) {
		pennantHeaderApi.setContentType(MediaType.APPLICATION_JSON);
		String auth = source.get().getSourceId() + ":" + source.get().getSecretKey();
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		pennantHeaderApi.add("Authorization", authHeader);
	}
	
	@GetMapping("/getFeeCodeDetails")
	public @ResponseBody ResponseEntity<?> getFeeCodeDetails() {
		List<MstFeesDetails> findByEnabledTrue = mstFeesDeailsRepository.findByEnabledTrue();
		return ResponseEntity.ok(findByEnabledTrue);
	}
	
	public void setMstBankBranchMappingRepository(MstBankBranchMappingRepository mstBankBranchMappingRepository) {
		this.mstBankBranchMappingRepository = mstBankBranchMappingRepository;
	}

	public MstBankBranchMappingRepository getMstBankBranchMappingRepository() {
		return mstBankBranchMappingRepository;
	}
	
}
