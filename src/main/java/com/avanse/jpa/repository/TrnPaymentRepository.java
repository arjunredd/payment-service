package com.avanse.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import com.avanse.jpa.model.TrnPayment;

public interface TrnPaymentRepository extends CrudRepository<TrnPayment, Long> {

	TrnPayment findBypaymentRequestId(long paymentRequestId);

}
