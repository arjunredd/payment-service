package com.avanse.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avanse.jpa.model.TrnPaymentReceipt;


@Repository
public interface TrnPaymentReceiptRepositry extends JpaRepository<TrnPaymentReceipt
,Integer> {
	
	boolean existsByPennantReceiptNo(String pennetRecieptNo);
	
}
