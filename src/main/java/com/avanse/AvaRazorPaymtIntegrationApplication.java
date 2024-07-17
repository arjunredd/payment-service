package com.avanse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AvaRazorPaymtIntegrationApplication {
	
	//@Value("${apiKey}")
	//private String apiKey;
	
	//@Value("${apiSecret}")
	//private String apiSecret;
	
	/*
	 * @Value("${proxy.ip}") private static String proxy_ip;
	 * 
	 * @Value("${proxy.port}") private static String proxy_port;
	 * 
	 * @Value("${proxy.user}") private static String proxy_user;
	 * 
	 * @Value("${proxy.password}") private static String proxy_password;
	 */

	public static void main(String[] args) {
		
		
		
		
		/*
		 * System.setProperty("https.proxyHost", proxy_ip);
		 * System.setProperty("https.proxyPort", proxy_port);
		 * 
		 * Authenticator.setDefault( new Authenticator() {
		 * 
		 * @Override public PasswordAuthentication getPasswordAuthentication() { return
		 * new PasswordAuthentication(proxy_user, proxy_password.toCharArray()); } } );
		 * 
		 * System.setProperty("https.proxyUser", proxy_user);
		 * System.setProperty("https.proxyPassword", proxy_password);
		 * 
		 * System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
		 */
		 
		  
		/*
		 * System.setProperty("https.proxyHost", "10.150.1.162");
		 * System.setProperty("https.proxyPort", "8080");
		 * System.setProperty("https.proxyUser", "aitdev");
		 * System.setProperty("https.proxyPassword", "Tea@1234");
		 * 
		 * System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
		 */
		 
		 
		
		SpringApplication.run(AvaRazorPaymtIntegrationApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	/*
	 * @Bean public HttpHeaders getHttpHeaders() { String plainCreds =
	 * apiKey+":"+apiSecret; byte[] plainCredsBytes = plainCreds.getBytes();
	 * 
	 * byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes); String
	 * base64Creds = new String(base64CredsBytes);
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.add("Authorization",
	 * "Basic " + base64Creds);
	 * 
	 * return headers; }
	 */
	
	/*
	 * @Bean public SimpleClientHttpRequestFactory getFactory() {
	 * SimpleClientHttpRequestFactory factory= new SimpleClientHttpRequestFactory();
	 * factory.setProxy(new Proxy(Proxy.Type.HTTP, new
	 * java.net.InetSocketAddress(proxy_ip, proxy_port))); return factory; }
	 */
	

}
