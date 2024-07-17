package com.avanse.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

public class ELMSView {

	private int id;

	private String loanNo;

	private String customer_name;

	private String LOAN_BRANCH;

	private String TYPE;

	private Date CUSTOMER_DOB;

	private String MOBILE_NUMBER;

	private String EMAIL_ID;

	private String ADDRESS;

	private Double POS;

	private String MODE_OF_REPAYMENT;

	private Double EMI_SI_PI_AMOUNT;

	private Double OVERDUE_PI_AMOUNT;

	private Double OVERDUE_SI_AMOUNT;

	private Double OVERDUE_EMI_AMOUNT;

	private Double BOUNCE_CHARGE;

	private Double PENAL_CHARGE;

	private Double TOTAL_AMOUNT;

	private String Employee;

	private Date START_DATE;

	private Date END_DATE;

	private String PROD_TYPE;

	private double CURRENT_MONTH_INST;

	private Double availableCashLimit;

	/*
	 * public ELMSView(String loanNo, String customer_name, String lOAN_BRANCH,
	 * String tYPE, String mOBILE_NUMBER, String eMAIL_ID, String aDDRESS, Double
	 * pOS, String mODE_OF_REPAYMENT, Double eMI_SI_PI_AMOUNT, Double
	 * oVERDUE_PI_AMOUNT, Double oVERDUE_SI_AMOUNT, Double oVERDUE_EMI_AMOUNT,
	 * Double bOUNCE_CHARGE, Double pENAL_CHARGE, Double tOTAL_AMOUNT, String
	 * employee, String pROD_TYPE) { super(); this.loanNo = loanNo;
	 * this.customer_name = customer_name; LOAN_BRANCH = lOAN_BRANCH; TYPE = tYPE;
	 * //CUSTOMER_DOB = cUSTOMER_DOB; MOBILE_NUMBER = mOBILE_NUMBER; EMAIL_ID =
	 * eMAIL_ID; ADDRESS = aDDRESS; POS = pOS; MODE_OF_REPAYMENT =
	 * mODE_OF_REPAYMENT; EMI_SI_PI_AMOUNT = eMI_SI_PI_AMOUNT; OVERDUE_PI_AMOUNT =
	 * oVERDUE_PI_AMOUNT; OVERDUE_SI_AMOUNT = oVERDUE_SI_AMOUNT; OVERDUE_EMI_AMOUNT
	 * = oVERDUE_EMI_AMOUNT; BOUNCE_CHARGE = bOUNCE_CHARGE; PENAL_CHARGE =
	 * pENAL_CHARGE; TOTAL_AMOUNT = tOTAL_AMOUNT; Employee = employee; //START_DATE
	 * = sTART_DATE; //END_DATE = eND_DATE; PROD_TYPE = pROD_TYPE; }
	 */

	public ELMSView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Double getAvailableCashLimit() {
		return availableCashLimit;
	}

	public void setAvailableCashLimit(Double availableCashLimit) {
		this.availableCashLimit = availableCashLimit;
	}

	public double getCURRENT_MONTH_INST() {
		return CURRENT_MONTH_INST;
	}

	public void setCURRENT_MONTH_INST(double cURRENT_MONTH_INST) {
		CURRENT_MONTH_INST = cURRENT_MONTH_INST;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLOAN_BRANCH() {
		return LOAN_BRANCH;
	}

	public void setLOAN_BRANCH(String lOAN_BRANCH) {
		LOAN_BRANCH = lOAN_BRANCH;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public Date getCUSTOMER_DOB() {
		return CUSTOMER_DOB;
	}

	public void setCUSTOMER_DOB(Date cUSTOMER_DOB) {
		CUSTOMER_DOB = cUSTOMER_DOB;
	}

	public String getMOBILE_NUMBER() {
		return MOBILE_NUMBER;
	}

	public void setMOBILE_NUMBER(String mOBILE_NUMBER) {
		MOBILE_NUMBER = mOBILE_NUMBER;
	}

	public String getEMAIL_ID() {
		return EMAIL_ID;
	}

	public void setEMAIL_ID(String eMAIL_ID) {
		EMAIL_ID = eMAIL_ID;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public Double getPOS() {
		return POS;
	}

	public void setPOS(Double pOS) {
		POS = pOS;
	}

	public String getMODE_OF_REPAYMENT() {
		return MODE_OF_REPAYMENT;
	}

	public void setMODE_OF_REPAYMENT(String mODE_OF_REPAYMENT) {
		MODE_OF_REPAYMENT = mODE_OF_REPAYMENT;
	}

	public Double getEMI_SI_PI_AMOUNT() {
		return EMI_SI_PI_AMOUNT;
	}

	public void setEMI_SI_PI_AMOUNT(Double eMI_SI_PI_AMOUNT) {
		EMI_SI_PI_AMOUNT = eMI_SI_PI_AMOUNT;
	}

	public Double getOVERDUE_PI_AMOUNT() {
		return OVERDUE_PI_AMOUNT;
	}

	public void setOVERDUE_PI_AMOUNT(Double oVERDUE_PI_AMOUNT) {
		OVERDUE_PI_AMOUNT = oVERDUE_PI_AMOUNT;
	}

	public Double getOVERDUE_SI_AMOUNT() {
		return OVERDUE_SI_AMOUNT;
	}

	public void setOVERDUE_SI_AMOUNT(Double oVERDUE_SI_AMOUNT) {
		OVERDUE_SI_AMOUNT = oVERDUE_SI_AMOUNT;
	}

	public Double getOVERDUE_EMI_AMOUNT() {
		return OVERDUE_EMI_AMOUNT;
	}

	public void setOVERDUE_EMI_AMOUNT(Double oVERDUE_EMI_AMOUNT) {
		OVERDUE_EMI_AMOUNT = oVERDUE_EMI_AMOUNT;
	}

	public Double getBOUNCE_CHARGE() {
		return BOUNCE_CHARGE;
	}

	public void setBOUNCE_CHARGE(Double bOUNCE_CHARGE) {
		BOUNCE_CHARGE = bOUNCE_CHARGE;
	}

	public Double getPENAL_CHARGE() {
		return PENAL_CHARGE;
	}

	public void setPENAL_CHARGE(Double pENAL_CHARGE) {
		PENAL_CHARGE = pENAL_CHARGE;
	}

	public Double getTOTAL_AMOUNT() {
		return TOTAL_AMOUNT;
	}

	public void setTOTAL_AMOUNT(Double tOTAL_AMOUNT) {
		TOTAL_AMOUNT = tOTAL_AMOUNT;
	}

	public String getEmployee() {
		return Employee;
	}

	public void setEmployee(String employee) {
		Employee = employee;
	}

	public Date getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(Date sTART_DATE) {
		START_DATE = sTART_DATE;
	}

	public Date getEND_DATE() {
		return END_DATE;
	}

	public void setEND_DATE(Date eND_DATE) {
		END_DATE = eND_DATE;
	}

	public String getPROD_TYPE() {
		return PROD_TYPE;
	}

	public void setPROD_TYPE(String pROD_TYPE) {
		PROD_TYPE = pROD_TYPE;
	}

	@Override
	public String toString() {
		return "ELMSView [id=" + id + ", loanNo=" + loanNo + ", customer_name=" + customer_name + ", LOAN_BRANCH="
				+ LOAN_BRANCH + ", TYPE=" + TYPE + ", CUSTOMER_DOB=" + CUSTOMER_DOB + ", MOBILE_NUMBER=" + MOBILE_NUMBER
				+ ", EMAIL_ID=" + EMAIL_ID + ", ADDRESS=" + ADDRESS + ", POS=" + POS + ", MODE_OF_REPAYMENT="
				+ MODE_OF_REPAYMENT + ", EMI_SI_PI_AMOUNT=" + EMI_SI_PI_AMOUNT + ", OVERDUE_PI_AMOUNT="
				+ OVERDUE_PI_AMOUNT + ", OVERDUE_SI_AMOUNT=" + OVERDUE_SI_AMOUNT + ", OVERDUE_EMI_AMOUNT="
				+ OVERDUE_EMI_AMOUNT + ", BOUNCE_CHARGE=" + BOUNCE_CHARGE + ", PENAL_CHARGE=" + PENAL_CHARGE
				+ ", TOTAL_AMOUNT=" + TOTAL_AMOUNT + ", Employee=" + Employee + ", START_DATE=" + START_DATE
				+ ", END_DATE=" + END_DATE + ", PROD_TYPE=" + PROD_TYPE + ", CURRENT_MONTH_INST=" + CURRENT_MONTH_INST
				+ ", availableCashLimit=" + availableCashLimit + "]";
	}

}
