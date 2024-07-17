package com.avanse.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avanse.jpa.model.TrnFeeDetails;

public interface TrnFeeDetailsMappingRepository extends CrudRepository<TrnFeeDetails, Long> {

	List<TrnFeeDetails> findAllByfeeTransactionId(int feeTransactionId);

	List<TrnFeeDetails> findAllByPaymentRequestId(long paymentRequestId);

}
