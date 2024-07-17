package com.avanse.consumer;

import java.util.List;



public class FinanceRequest {
    private String finReference;

    private long amount;

    private String paymentMode;
    
    private String excessAdjustTo;
    
    private String reqType;
    
    private boolean stpProcess;
    
    private String processStage;
    
    private String serviceReqNo;
    
    private String remarks;
    
    private ReceiptDetail receiptDetail;
    
    private List<Fee>  fees;

    private String realizationDate;

    public String getExcessAdjustTo() {
		return excessAdjustTo;
	}

	public void setExcessAdjustTo(String excessAdjustTo) {
		this.excessAdjustTo = excessAdjustTo;
	}

	public boolean isStpProcess() {
		return stpProcess;
	}

	public void setStpProcess(boolean stpProcess) {
		this.stpProcess = stpProcess;
	}

	public String getProcessStage() {
		return processStage;
	}

	public void setProcessStage(String processStage) {
		this.processStage = processStage;
	}

	public String getServiceReqNo() {
		return serviceReqNo;
	}

	public void setServiceReqNo(String serviceReqNo) {
		this.serviceReqNo = serviceReqNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getFinReference ()
    {
        return finReference;
    }

    public void setFinReference (String finReference)
    {
        this.finReference = finReference;
    }

    public long getAmount ()
    {
        return amount;
    }

    public void setAmount (long amount)
    {
        this.amount = amount;
    }

    public List<Fee> getFees() {
		return fees;
	}

	public void setFees(List<Fee> fees) {
		this.fees = fees;
	}

	public String getRealizationDate ()
    {
        return realizationDate;
    }

    public void setRealizationDate (String realizationDate)
    {
        this.realizationDate = realizationDate;
    }

    public String getPaymentMode ()
    {
        return paymentMode;
    }

    public void setPaymentMode (String paymentMode)
    {
        this.paymentMode = paymentMode;
    }

    public ReceiptDetail getReceiptDetail ()
    {
        return receiptDetail;
    }

    public void setReceiptDetail (ReceiptDetail receiptDetail)
    {
        this.receiptDetail = receiptDetail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [finReference = "+finReference+", amount = "+amount+", fees = "+fees+", realizationDate = "+realizationDate+", paymentMode = "+paymentMode+", receiptDetail = "+receiptDetail+"]";
    }

	/*
	 * public FinanceRequest(String finReference, Double amount, List<Fee> fees,
	 * String realizationDate, String paymentMode, ReceiptDetail receiptDetail) {
	 * super(); this.finReference = finReference; this.amount = amount; this.fees =
	 * fees; this.realizationDate = realizationDate; this.paymentMode = paymentMode;
	 * this.receiptDetail = receiptDetail; }
	 */
    
    

	public FinanceRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FinanceRequest(String finReference, long amount, String paymentMode, String excessAdjustTo, String reqType,
			boolean stpProcess, String processStage, String serviceReqNo, String remarks, ReceiptDetail receiptDetail,
			List<Fee> fees, String realizationDate) {
		super();
		this.finReference = finReference;
		this.amount = amount;
		this.paymentMode = paymentMode;
		this.excessAdjustTo = excessAdjustTo;
		this.reqType = reqType;
		this.stpProcess = stpProcess;
		this.processStage = processStage;
		this.serviceReqNo = serviceReqNo;
		this.remarks = remarks;
		this.receiptDetail = receiptDetail;
		this.fees = fees;
		this.realizationDate = realizationDate;
	}
   

}
