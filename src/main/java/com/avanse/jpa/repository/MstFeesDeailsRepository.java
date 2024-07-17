package com.avanse.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.avanse.jpa.model.MstFeesDetails;

public interface MstFeesDeailsRepository extends JpaRepository<MstFeesDetails, Integer> {
	@Query
	public List<MstFeesDetails> findByEnabledTrue();
	
	public MstFeesDetails findByFeeCode(String feeCode);

}
