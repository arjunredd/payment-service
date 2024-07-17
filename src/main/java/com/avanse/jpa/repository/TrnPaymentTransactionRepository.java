package com.avanse.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avanse.jpa.model.TrnPaymentTransaction;

public interface TrnPaymentTransactionRepository extends JpaRepository<TrnPaymentTransaction,Integer>{

	boolean existsByRazorPayOrderIdOrRazorPayPaymentIdOrAuthCode(String refTransNo,String refTransNo2,String refTransNo3);
	
	boolean existsByRazorPayOrderId(String refTransNo);

	boolean existsByRazorPayPaymentId(String refTransNo);

	boolean existsByAuthCode(String refTransNo);

	Optional<TrnPaymentTransaction> findByRazorPayOrderId(String orderId);

}
