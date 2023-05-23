package com.monstarbill.master.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.monstarbill.master.commons.AppConstants;
import com.monstarbill.master.commons.CustomException;
import com.monstarbill.master.enums.Operation;
import com.monstarbill.master.models.Supplier;
import com.monstarbill.master.models.SupplierContact;
import com.monstarbill.master.models.SupplierHistory;
import com.monstarbill.master.models.SupplierSubsidiary;
import com.monstarbill.master.repository.SupplierContactRepository;
import com.monstarbill.master.repository.SupplierHistoryRepository;
import com.monstarbill.master.repository.SupplierRepository;
import com.monstarbill.master.repository.SupplierSubsidiaryRepository;
import com.monstarbill.master.service.SupplierService;


import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private SupplierContactRepository supplierContactRepository;
	@Autowired
	private SupplierSubsidiaryRepository supplierSubsidiaryRepository;
	@Autowired
	private SupplierHistoryRepository supplierHistoryRepository;
	
//==================================
	
		
	
	@Override
	public Supplier saveSupplier(Supplier supplier) {
		
//==================(1) Supplier save started=============
		Long supplierId = null;
		Optional<Supplier> oldSupplier=Optional.empty();
		
		
//=================== cloning of existing object=========================
		if (supplier.getId() == null) {
			supplier.setCreatedBy("supplierId is null");
			log.info("supplier id is null");
		} else {
			oldSupplier = this.supplierRepository.findByIdAndIsDeleted(supplier.getId(), false);
			if (oldSupplier.isPresent()) {
				try {
					oldSupplier = Optional.ofNullable((Supplier) oldSupplier.get().clone());
				} catch (CloneNotSupportedException e) {
					log.error("getting error while supplier cloneing");
					throw new CustomException("getting error while supplier cloning");
				}
			}
		}
		
		Supplier savedSupplier;
		try {
			savedSupplier = this.supplierRepository.save(supplier);
		} catch (DataIntegrityViolationException e) {
			log.error("Supplier unique constrain violetd." + e.getMostSpecificCause());
			throw new CustomException("Supplier unique constrain violetd :" + e.getMostSpecificCause());
		}
		
		
		supplierId =updateSupplierHistory(supplier,oldSupplier);
		log.info("supplier save finished");
		
//====================== save supplier finished  ======================		

//=======================save supplier address=================================================
		System.out.println("save supplier address");



//========================save supplier address finished=========================
		

//======================(3) Supplier Subsidiary Save Started Inside Supplier===========================
		log.info("save supplier subsidiary started");
		List<SupplierSubsidiary> supplierSubsidiaries=supplier.getSupplierSubsidiary();
		if(CollectionUtils.isNotEmpty(supplierSubsidiaries)) {
			for(SupplierSubsidiary supplierSubsidiary:supplierSubsidiaries) {
				this.saveSubsidiary(supplierId,supplierSubsidiary);
			}
			savedSupplier.setSupplierSubsidiary(supplierSubsidiaries);
		}
		
//======================(3) Supplier Subsidiary Save  Inside Supplier finished ===========================		
		
		
//======================(4) Supplier Contact Save Started Inside Supplier===========================
		log.info("save supplier Contact started");
		List<SupplierContact> supplierContacts = supplier.getSupplierContacts();
		if (CollectionUtils.isNotEmpty(supplierContacts)) {
			for (SupplierContact supplierContact : supplierContacts) {
				this.saveContact(supplierId, supplierContact);
			}
			savedSupplier.setSupplierContacts(supplierContacts);
		}
				
//======================(4) Supplier Contact Save  Inside Supplier finished ===========================			
		
		
		
				
		return savedSupplier;
	}
	
	


//***************************************Method to save Supplier child's table********************************	
	
	





private void saveContact(Long supplierId, SupplierContact supplierContact) {
	//get existing address using deep copy
	Optional<SupplierContact> oldSupplierContact=Optional.empty();
	if(supplierContact.getId()==null) {
		log.info("supplier contact id is null");
	} else {
		oldSupplierContact = this.supplierContactRepository.findByIdAndIsDeleted(supplierContact.getId(), false);
		if (oldSupplierContact.isPresent()) {
			try {
				oldSupplierContact = Optional.ofNullable((SupplierContact) oldSupplierContact.get().clone());
			} catch (CloneNotSupportedException e) {
				log.info("Error while cloning the object");
				throw new CustomException("Error while cloning the object");
			}
		}
	}
	
	supplierContact.setSupplierId(supplierId);	
	supplierContact=this.supplierContactRepository.save(supplierContact);
	if(supplierContact == null) {
		log.info("error while save supplier contact");
	throw new CustomException("error while save supplier contact");
	}
	updateSupplierContactHistory(oldSupplierContact, supplierContact);
	
	
	}



public void saveSubsidiary(Long supplierId, SupplierSubsidiary supplierSubsidiary) {
	Optional<SupplierSubsidiary> oldSupplierSubsidiary = Optional.empty();

	if (supplierSubsidiary.getId() == null) {
		log.info("subsidiary");
	} else {
		// Get the existing object using the deep copy
		oldSupplierSubsidiary = this.supplierSubsidiaryRepository.findByIdAndIsDeleted(supplierSubsidiary.getId(),
				false);
		if (oldSupplierSubsidiary.isPresent()) {
			try {
				oldSupplierSubsidiary = Optional
						.ofNullable((SupplierSubsidiary) oldSupplierSubsidiary.get().clone());
			} catch (CloneNotSupportedException e) {
				log.error("Error while Cloning the object. Please contact administrator.");
				throw new CustomException("Error while Cloning the object. Please contact administrator.");
			}
		}
	}
	supplierSubsidiary.setSupplierId(supplierId);
	
	SupplierSubsidiary savedSupplierSubsidiary = this.supplierSubsidiaryRepository.save(supplierSubsidiary);

	updateSupllierSubsidiaryHistory(oldSupplierSubsidiary, savedSupplierSubsidiary);
	}






//******************************** METHOD FOR UPDATE SUPPLIER Child's HISTORY TABLE*****************************************************************


private void updateSupllierSubsidiaryHistory(Optional<SupplierSubsidiary> oldSupplierSubsidiary,
		SupplierSubsidiary savedSupplierSubsidiary) {
	//insert the field into history table
	if(oldSupplierSubsidiary.isPresent()) {
		List<SupplierHistory> supplierHistories=new ArrayList<SupplierHistory>();
		try {
			supplierHistories=oldSupplierSubsidiary.get().compareFields(savedSupplierSubsidiary);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			log.error("error while comparing the new and old objects");
			throw new CustomException("error while comparing the new and old objects");
		}
	}else {
		this.supplierHistoryRepository.save(this.prepareSupplierHistory(savedSupplierSubsidiary.getSupplierId(), savedSupplierSubsidiary.getId(), 
				AppConstants.SUPPLIER_SUBSIDIARY, Operation.CREATE.toString(), savedSupplierSubsidiary.getLastModifiedBy(),
				null, String.valueOf(savedSupplierSubsidiary.getId())));
		
		 
	}
}

private void updateSupplierContactHistory(Optional<SupplierContact> oldSupplierContact,
		SupplierContact supplierContact) {
	//insert the field into history table
	if(oldSupplierContact.isPresent()) {
		List<SupplierHistory> supplierHistories=new ArrayList<>();
		try {
			supplierHistories=oldSupplierContact.get().compareFields(supplierContact);
			this.supplierHistoryRepository.saveAll(supplierHistories);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			log.error("error while comparing the new and old objects");
			throw new CustomException("error while comparing the new and old objects");
		}
	}else {
		this.supplierHistoryRepository.save(this.prepareSupplierHistory(supplierContact.getSupplierId(),
				supplierContact.getId(), AppConstants.SUPPLIER_CONTACT, Operation.CREATE.toString(),
				supplierContact.getLastModifiedBy(), null, String.valueOf(supplierContact.getId())));
	}
	
}





	//******************************** METHOD FOR UPDATE SUPPLIER HISTORY TABLE*****************************************************************
	private Long updateSupplierHistory(Supplier supplier, Optional<Supplier> oldSupplier) {
		
		Long supplierId = null;
		if (supplier != null) {
			supplierId = supplier.getId();
		
		
//===============insert the field into history table=================
		if(oldSupplier.isPresent()) {
			List<SupplierHistory> supplierHistories=new ArrayList<SupplierHistory>();
			try {
				supplierHistories=oldSupplier.get().compareFields(supplier);
				if(CollectionUtils.isNotEmpty(supplierHistories)){
					this.supplierHistoryRepository.saveAll(supplierHistories);
				}
			}catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				log.error("error while compare the old and new object");
				throw new CustomException("error while compare the old and new object, contact admin");
			}
			log.info("supplier history is update successfully");
		}else {
			// Insert in history table as Operation - INSERT
			this.supplierHistoryRepository.save(this.prepareSupplierHistory(supplierId, null, AppConstants.SUPPLIER,
					Operation.CREATE.toString(), supplier.getLastModifiedBy(), null, null));
		}
		log.info("Supplier History Saved successfully.");
	} else {
		log.error("Error while saving the supplier.");
		throw new CustomException("Error while saving the supplier.");
	}
					
		return supplierId;
	}

//***************************************commons for all ***************************************

	public SupplierHistory prepareSupplierHistory(Long supplierId, Long childId, String moduleName, String operation,
			String lastModifiedBy, String oldValue, String newValue) {
		SupplierHistory supplierHistory = new SupplierHistory();
		supplierHistory.setSupplierId(supplierId);
		supplierHistory.setChildId(childId);
		supplierHistory.setModuleName(moduleName);
		supplierHistory.setChangeType(AppConstants.UI);
		supplierHistory.setOperation(operation);
		supplierHistory.setOldValue(oldValue);
		supplierHistory.setNewValue(newValue);
		supplierHistory.setLastModifiedBy(lastModifiedBy);
		return supplierHistory;
	}


	
//#################################################################################################################################################
//#################################################################################################################################################

@Override
public List<Supplier> getAllSupplier() {
	List<Supplier> findAll = supplierRepository.findAll();
	return findAll;
}

}
