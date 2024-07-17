package com.avanse.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.avanse.jpa.model.MstBankBranchDetails;

public interface MstBankBranchMappingRepository extends CrudRepository<MstBankBranchDetails, Long> {
	
//	@Query("SELECT t FROM MstBankBranchDetails t WHERE " +
//            "t.MICR = :micr OR T.IFSC = :ifsc" )
//	MstBankBranchDetails findByMICROrIFSC(@Param("micr") String micr, @Param("ifsc") String ifsc);
	List<MstBankBranchDetails> findByMicrAndIfsc(String micr, String ifsc);

	List<MstBankBranchDetails> findByMicr(String micr);
	
	List<MstBankBranchDetails> findByIfsc(String ifsc);
}
