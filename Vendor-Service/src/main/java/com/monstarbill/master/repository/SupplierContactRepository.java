package com.monstarbill.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monstarbill.master.models.SupplierContact;

public interface SupplierContactRepository extends JpaRepository<SupplierContact, Long> {

//	Optional<SupplierContact> findByIdandIsDeleted(Long id, Boolean isDeleted);
	
	public Optional<SupplierContact> findByIdAndIsDeleted(Long id, boolean isDeleted);
}
