package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "MstFees")
public class MstFeesDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "FeeCode")
	private String feeCode;

	@Column(name = "FeeDescription")
	private String feeDescription;

	@Column(name="AvailableForOnline", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean enabled;

	@Column(name = "CreatedOn", updatable = false, nullable = false)
	@CreatedDate
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "IST")
	private Date createdOn = new Date();

	public MstFeesDetails() {
		
	}
	
	
	public MstFeesDetails(String feeCode, String feeDescription) {
		super();
		this.feeCode = feeCode;
		this.feeDescription = feeDescription;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFeeDescription() {
		return feeDescription;
	}

	public void setFeeDescription(String feeDescription) {
		this.feeDescription = feeDescription;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
