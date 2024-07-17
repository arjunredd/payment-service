package com.avanse.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avanse.jpa.model.TrnDedupeHistory;

public interface TrnDedupeHistoryRepository extends JpaRepository<TrnDedupeHistory, Integer>{

}
