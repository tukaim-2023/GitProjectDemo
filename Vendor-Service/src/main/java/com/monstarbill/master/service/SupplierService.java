package com.monstarbill.master.service;

import java.util.List;

import com.monstarbill.master.models.Supplier;

public interface SupplierService {
	
	public Supplier saveSupplier(Supplier supplier);

	public List<Supplier> getAllSupplier();

}
