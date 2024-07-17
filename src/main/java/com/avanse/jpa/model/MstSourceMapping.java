package com.avanse.jpa.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "MstSourceMapping")
public class MstSourceMapping {

	@Id
	//@GeneratedValue(generator = "UUID")
	//@GenericGenerator(
	//	name = "UUID",
	//	strategy = "org.hibernate.id.UUIDGenerator"
	//)
	@Type(type="uuid-char")
	@Column(name = "SourceID", updatable = false, nullable = false,columnDefinition="uniqueidentifier")
	private UUID sourceId;
	//private String sourceId;
	
	@Column(name = "SourceApplicationName")
	private String sourceAppName;
	
	@Column(name = "TypeOfApplication")
	private String typeOfApp;
	
	@Column(name = "SecretKey")
	private String secretKey;
	
	@Column(name="isActive")
	private boolean active;
	
	@Column(name="CreatedOn", nullable =false, updatable =false )
	@CreatedDate
	@JsonFormat(pattern="dd-MM-yyyy hh:mm:sss",timezone="IST")
	private Date createdOn;
	
	@CreatedBy
	@Column(name = "Createdby",nullable=false,updatable=false)
	private String createdBy;
	
	@Column(name="IsDeleted")
	private boolean isDeleted;
	
	@Column(name="successURL")
	private String successUrl;
	
	@Column(name="failureURL")
	private String failureUrl;
	
	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}	

	public UUID getSourceId() {
		return sourceId;
	}

	public void setSourceId(UUID sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceAppName() {
		return sourceAppName;
	}

	public void setSourceAppName(String sourceAppName) {
		this.sourceAppName = sourceAppName;
	}

	public String getTypeOfApp() {
		return typeOfApp;
	}

	public void setTypeOfApp(String typeOfApp) {
		this.typeOfApp = typeOfApp;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
