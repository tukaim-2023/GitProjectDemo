package com.monstarbill.master.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monstarbill.master.models.Supplier;
import com.monstarbill.master.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/supplier")
@Slf4j
public class SupplierController {

	@Autowired
	private SupplierService supplierService;
	
	@PostMapping("/save")
	public ResponseEntity<Supplier> saveSupplier(@Valid @RequestBody Supplier supplier) {
		supplier = supplierService.saveSupplier(supplier);
		log.info("Supplier saved successfully");
		return ResponseEntity.ok(supplier);
	}
	
	@GetMapping("/get")
	public ResponseEntity<Supplier> getAllSupplier(@RequestParam Long id){
		 Supplier supplier=supplierService.getSupplierById();
		log.info("Supplier get all successfully");
		return ResponseEntity.ok(supplier);
	}
	
}
