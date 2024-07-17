package com.avanse.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class TinyurlData {

	
	private String RedirectUrl;
	
	private String ExpiryDate;
	
	
	private String RefCode;
	
	
	private String LinkUrl;
	
	
	private String Code;
	
	public String getRedirectUrl() {
		return RedirectUrl;
	}
	
	@JsonProperty("RedirectUrl")
	public void setRedirectUrl(String redirectUrl) {
		RedirectUrl = redirectUrl;
	}
	
	@JsonProperty("ExpiryDate")
	public String getExpiryDate() {
		return ExpiryDate;
	}
	
	@JsonProperty("ExpiryDate")
	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}
	
	@JsonProperty("RefCode")
	public String getRefCode() {
		return RefCode;
	}
	
	@JsonProperty("RefCode")
	public void setRefCode(String refCode) {
		RefCode = refCode;
	}
	
	@JsonProperty("LinkUrl")
	public String getLinkUrl() {
		return LinkUrl;
	}
	
	@JsonProperty("LinkUrl")
	public void setLinkUrl(String linkUrl) {
		LinkUrl = linkUrl;
	}
	
	@JsonProperty("Code")
	public String getCode() {
		return Code;
	}
	
	@JsonProperty("Code")
	public void setCode(String code) {
		Code = code;
	}
	
	
}
