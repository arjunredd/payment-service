package com.avanse.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avanse.jpa.model.TrnPaymentRequest;
import com.avanse.jpa.model.TrnPaymentRequestDetails;

public interface TrnPaymentRequestDetailsRepository extends JpaRepository<TrnPaymentRequestDetails,Integer>{
	
	List<TrnPaymentRequestDetails> findAllByTrnPaymentRequest(TrnPaymentRequest trnPaymentRequest);

}
