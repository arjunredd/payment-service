package com.avanse.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class PaymentSecurity {
	public static String encrypt(String input, String key) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new String(Base64.encodeBase64(crypted));
	}

	public static String decrypt(String input, String key) {
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decodeBase64(input));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new String(output);
	}

	public static String checkSum(String input) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(input.getBytes());

		byte byteData[] = md.digest();

		/*
		 * //convert the byte to hex format method 1 StringBuffer sb = new
		 * StringBuffer(); for (int i = 0; i < byteData.length; i++) {
		 * sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1)); }
		 * 
		 * System.out.println("Hex format : " + sb.toString());
		 */

		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		// System.out.println("Hex format : " + hexString.toString());
		return hexString.toString();
	}

	/*
	 * public static void main(String[] args) { try { String key =
	 * "axisbank12345678"; String data =
	 * "CID=1162&RID=121&CRN=21100018&AMT=1.0&VER=1.0&TYP=test&CNY=INR&RTU=https://10.9.0.9/easypay/frontend/index.php/api/output&PPI=test1|asd|test|29/04/2015|8097520469|rajas.vyas@tejora.com|1&RE1=&RE2=&RE3=&RE4=&RE5=&CKS=";
	 * 
	 * String checkSum = PaymentSecurity.checkSum("1162121211000181.0axis");
	 * 
	 * data = data + checkSum; String encypt = PaymentSecurity.encrypt(data, key);
	 * System.out.println("Encrypt :: " + encypt);
	 * 
	 * String decrypt = PaymentSecurity.decrypt(encypt, key);
	 * 
	 * System.out.println("decrypt :: " + decrypt); } catch
	 * (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
}
