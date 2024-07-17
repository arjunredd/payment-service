package com.avanse.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avanse.jpa.model.MstSourceMapping;

public interface MstSourceMappingRepository extends JpaRepository<MstSourceMapping,UUID>{

	Optional<MstSourceMapping> findBySourceId(UUID sourceId);

	List<MstSourceMapping> findAllByIsDeleted(boolean status);

	boolean existsBySourceIdAndSecretKey(UUID sourceId, String secretKey);

}
