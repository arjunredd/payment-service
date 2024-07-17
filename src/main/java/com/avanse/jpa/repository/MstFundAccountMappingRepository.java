package com.avanse.jpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.avanse.jpa.model.MstFundAccountMapping;

@Repository
public interface MstFundAccountMappingRepository extends CrudRepository<MstFundAccountMapping,Integer>{

	Optional<MstFundAccountMapping> findByLoanTypeAndPaymentPurpose(String loanType, String paymentPurpose);
	
	boolean existsByLoanTypeAndPaymentPurpose(String loanType, String paymentPurpose);
   
}
