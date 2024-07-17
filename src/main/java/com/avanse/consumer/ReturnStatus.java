package com.avanse.consumer;

public class ReturnStatus {

	private String retrunCode;
	private String  returntext;
	
	public String getRetrunCode() {
		return retrunCode;
	}
	public void setRetrunCode(String retrunCode) {
		this.retrunCode = retrunCode;
	}
	public String getReturntext() {
		return returntext;
	}
	public void setReturntext(String returntext) {
		this.returntext = returntext;
	}
	@Override
	public String toString() {
		return "ReturnStatus [retrunCode=" + retrunCode + ", returntext=" + returntext + "]";
	}
	public ReturnStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReturnStatus(String retrunCode, String returntext) {
		super();
		this.retrunCode = retrunCode;
		this.returntext = returntext;
	}
	
}
