package com.avanse.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BankBranches")
public class MstBankBranchDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BankBranchID")
	private long BankBranchID;
	private String BankCode;
	private String BranchCode;
	private String BranchDesc;
	private String City;
	@Column(name = "MICR")
	private String micr;
	@Column(name = "IFSC")
	private String ifsc;
	private int Version;
	private long LastMntBy;
	private Date LastMntOn;
	private String RecordStatus;
	private String RoleCode;
	private String NextRoleCode;
	private String TaskId;
	private String NextTaskId;
	private String RecordType;
	private long WorkflowId;
	private String AddOfBranch;
	@Column(name = "Nach", columnDefinition = "BIT")
	private boolean Nach;
	@Column(name = "Dd", columnDefinition = "BIT")
	private boolean Dd;
	@Column(name = "Dda", columnDefinition = "BIT")
	private boolean Dda;
	@Column(name = "Ecs", columnDefinition = "BIT")
	private boolean Ecs;
	@Column(name = "Cheque", columnDefinition = "BIT")
	private boolean Cheque;
	@Column(name = "Active", columnDefinition = "BIT")
	private boolean Active;

	public long getBankBranchID() {
		return BankBranchID;
	}

	public void setBankBranchID(long bankBranchID) {
		BankBranchID = bankBranchID;
	}

	public String getBankCode() {
		return BankCode;
	}

	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	public String getBranchCode() {
		return BranchCode;
	}

	public void setBranchCode(String branchCode) {
		BranchCode = branchCode;
	}

	public String getBranchDesc() {
		return BranchDesc;
	}

	public void setBranchDesc(String branchDesc) {
		BranchDesc = branchDesc;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	
	public String getIfsc() {
		return ifsc;
	}
	
	public void setMicr(String micr) {
		this.micr = micr;
	}
	
	public String getMicr() {
		return micr;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

	public long getLastMntBy() {
		return LastMntBy;
	}

	public void setLastMntBy(long lastMntBy) {
		LastMntBy = lastMntBy;
	}

	public Date getLastMntOn() {
		return LastMntOn;
	}

	public void setLastMntOn(Date lastMntOn) {
		LastMntOn = lastMntOn;
	}

	public String getRecordStatus() {
		return RecordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		RecordStatus = recordStatus;
	}

	public String getRoleCode() {
		return RoleCode;
	}

	public void setRoleCode(String roleCode) {
		RoleCode = roleCode;
	}

	public String getNextRoleCode() {
		return NextRoleCode;
	}

	public void setNextRoleCode(String nextRoleCode) {
		NextRoleCode = nextRoleCode;
	}

	public String getTaskId() {
		return TaskId;
	}

	public void setTaskId(String taskId) {
		TaskId = taskId;
	}

	public String getNextTaskId() {
		return NextTaskId;
	}

	public void setNextTaskId(String nextTaskId) {
		NextTaskId = nextTaskId;
	}

	public String getRecordType() {
		return RecordType;
	}

	public void setRecordType(String recordType) {
		RecordType = recordType;
	}

	public long getWorkflowId() {
		return WorkflowId;
	}

	public void setWorkflowId(long workflowId) {
		WorkflowId = workflowId;
	}

	public String getAddOfBranch() {
		return AddOfBranch;
	}

	public void setAddOfBranch(String addOfBranch) {
		AddOfBranch = addOfBranch;
	}

	public boolean isNach() {
		return Nach;
	}

	public void setNach(boolean nach) {
		Nach = nach;
	}

	public boolean isDd() {
		return Dd;
	}

	public void setDd(boolean dd) {
		Dd = dd;
	}

	public boolean isDda() {
		return Dda;
	}

	public void setDda(boolean dda) {
		Dda = dda;
	}

	public boolean isEcs() {
		return Ecs;
	}

	public void setEcs(boolean ecs) {
		Ecs = ecs;
	}

	public boolean isCheque() {
		return Cheque;
	}

	public void setCheque(boolean cheque) {
		Cheque = cheque;
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}

	@Override
	public String toString() {
		return "MstBankBranchDetails [BankBranchID=" + BankBranchID + ", BankCode=" + BankCode + ", BranchCode="
				+ BranchCode + ", BranchDesc=" + BranchDesc + ", City=" + City + ", MICR=" + micr + ", IFSC=" + ifsc
				+ ", Version=" + Version + ", LastMntBy=" + LastMntBy + ", LastMntOn=" + LastMntOn + ", RecordStatus="
				+ RecordStatus + ", RoleCode=" + RoleCode + ", NextRoleCode=" + NextRoleCode + ", TaskId=" + TaskId
				+ ", NextTaskId=" + NextTaskId + ", RecordType=" + RecordType + ", WorkflowId=" + WorkflowId
				+ ", AddOfBranch=" + AddOfBranch + ", Nach=" + Nach + ", Dd=" + Dd + ", Dda=" + Dda + ", Ecs=" + Ecs
				+ ", Cheque=" + Cheque + ", Active=" + Active + "]";
	}

}
