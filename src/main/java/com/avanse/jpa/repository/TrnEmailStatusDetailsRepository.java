package com.avanse.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avanse.jpa.model.TrnEmailStatusDetails;

public interface TrnEmailStatusDetailsRepository extends CrudRepository<TrnEmailStatusDetails, Long> {

	List<TrnEmailStatusDetails> findByPaymentRequestId(long paymentRequestId);

}
