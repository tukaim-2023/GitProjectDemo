package com.monstarbill.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monstarbill.master.models.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
//	Optional<Supplier> findByIdandIsDeleted(Long id, Boolean isDeleted);
	public Optional<Supplier> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
