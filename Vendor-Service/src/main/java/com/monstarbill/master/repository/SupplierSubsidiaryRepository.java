package com.monstarbill.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monstarbill.master.models.SupplierSubsidiary;

public interface SupplierSubsidiaryRepository extends JpaRepository<SupplierSubsidiary, Long> {

//	Optional<SupplierSubsidiary> findByIdandIsDeleted(Long id, Boolean isDeleted);
	public Optional<SupplierSubsidiary> findByIdAndIsDeleted(Long id, boolean isDeleted);
}
