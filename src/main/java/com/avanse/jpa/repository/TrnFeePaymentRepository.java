package com.avanse.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.avanse.jpa.model.TrnFeePayment;


public interface TrnFeePaymentRepository extends CrudRepository<TrnFeePayment, Long> {

	List<TrnFeePayment> findByPaymentRequestId(String paymentRequestId);

}
