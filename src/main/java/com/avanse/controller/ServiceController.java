package com.avanse.controller;

//@RestController("/")
public class ServiceController {
	
	/*
	 * @Value("${baseUrl}") private String baseUrl;
	 * 
	 * @Autowired RestTemplate restTemplate;
	 * 
	 * @Autowired HttpHeaders headers;
	 * 
	 * @Value("${apiKey}") private String apiKey;
	 * 
	 * @Value("${apiSecret}") private String apiSecret;
	 * 
	 * @Autowired private TrnPaymentRequestRepository sourceInfoRepository;
	 */
	
	
	/*
	 * @GetMapping("/orders") public Collection getOrders(){
	 * 
	 * HttpEntity<String> request = new HttpEntity<String>(headers);
	 * ResponseEntity<Collection> respEntity =
	 * restTemplate.exchange(baseUrl+"orders", HttpMethod.GET, request,
	 * Collection.class); Collection response = respEntity.getBody(); return
	 * response;
	 * 
	 * }
	 * 
	 * @GetMapping("/sources") public List<TrnPaymentRequest> getSourceInfoData() {
	 * return sourceInfoRepository.findAll(); }
	 */
	
	
	/*
	 * @RequestMapping(value="/paymentSuccess",method=RequestMethod.POST,consumes=
	 * MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces=MediaType.
	 * APPLICATION_JSON_VALUE) public @ResponseBody MultiValueMap
	 * getPaymentResponse(@RequestBody MultiValueMap paramMap) {
	 * 
	 * 
	 * System.out.println("orderId:"+paramMap.getFirst("razorpay_order_id"));
	 * System.out.println("paymentId:"+paramMap.getFirst("razorpay_payment_id"));
	 * System.out.println("signature:"+paramMap.getFirst("razorpay_signature"));
	 * 
	 * String generated_signature = null; try { generated_signature =
	 * hmac_sha256(paramMap.getFirst("razorpay_order_id") + "|" +
	 * paramMap.getFirst("razorpay_payment_id"), apiSecret); } catch
	 * (SignatureException e) { e.printStackTrace(); }
	 * 
	 * System.out.println("generated_sign:"+generated_signature);
	 * 
	 * if (generated_signature!=null &&
	 * generated_signature.equals(paramMap.getFirst("razorpay_signature"))) {
	 * paramMap.add("response", "payment is successful");
	 * 
	 * }else { paramMap.add("response", "payment is failed"); }
	 * 
	 * return paramMap; }
	 */
	
	
}
