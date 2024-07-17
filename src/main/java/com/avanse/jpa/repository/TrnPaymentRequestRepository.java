package com.avanse.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avanse.jpa.model.TrnPaymentRequest;

public interface TrnPaymentRequestRepository extends JpaRepository<TrnPaymentRequest,Integer>{

	Optional<TrnPaymentRequest> findTopByRequestStatusNotLikeAndLaNumberLikeOrderByPaymentRequestDateTimeDesc(
			String string, String laNumber);

	@Query(
			  value = "select top 1 * from TrnPaymentRequest t where t.LANumber = :laNumber and t.RequestStatus != :status \r\n" + 
			  		"ORDER BY t.PaymentRequestDateTime desc", 
			  nativeQuery = true
			  )
	Optional<TrnPaymentRequest> getTopLastRecord(@Param("laNumber") String laNumber, @Param("status") String status);

	Optional<TrnPaymentRequest> findTopByLaNumberOrderByPaymentRequestDateTimeDesc(String laNumber);

	boolean existsByRefTransaction(String refTransaction);
	boolean existsBypaymentRef(String paymentRef);
	boolean existsByPaymentRef(String paymentRef);


	Optional<TrnPaymentRequest> findByLaNumber(String laNumber);
	//Optional<TrnPaymentRequest> findTopByLaNumberAndRequestStatusNotLikeOrderByPaymentRequestDateTimeDesc(String laNumber,
			//String status);

	//Optional<TrnPaymentRequest> findTopByRequestStatusNotLikeAndLaNumberOrderByPaymentRequestDateTimeDesc(String string,
		//	String laNumber);
	
	
}
