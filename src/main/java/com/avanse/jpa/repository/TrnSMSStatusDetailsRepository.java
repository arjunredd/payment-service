package com.avanse.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avanse.jpa.model.TrnSMSStatusDetails;

public interface TrnSMSStatusDetailsRepository extends CrudRepository<TrnSMSStatusDetails, Long> {

	List<TrnSMSStatusDetails> findByPaymentRequestId(long paymentRequestId);

}
