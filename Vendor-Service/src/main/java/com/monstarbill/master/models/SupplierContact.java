package com.monstarbill.master.models;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import com.monstarbill.master.commons.AppConstants;
import com.monstarbill.master.commons.CommonUtils;
import com.monstarbill.master.enums.Operation;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "setup2",name = "supplier_contact")
@Audited
@AuditTable("supplier_contact_aud")
public class SupplierContact implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@Column(name = "supplier_id")
	private Long supplierId;
//====================Supplier Contact Form field=================

	
	@Column(name = "contact_name")
	private String contactName;
	
	@NotNull(message = "contact number is mandatory")
	@Column(name = "contact_no",unique = true,nullable = false)
	private String contactNumber;
	
	@Column(name = "alt_contact_No")
	private String altContactNumber;
	
	@Column(name = "email")
	private String email;
	@Column(name = "fax")
	private String fax;
	@Column(name = "website")
	private String website;
	
	@Column(name = "is_primary_caontact",columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean iPrimaryContact;
//========================some extra information in time details=========================	
	@Column(name = "is_deleted",columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isDeleted;
	
	@CreationTimestamp
	@Column(name = "created_date",updatable = false)
	private Date createdDate;
	
	@Column(name = "created_by",updatable = false)
	private String createdBy;
	
	@UpdateTimestamp
	@Column(name = "last_modified_date")
	private Date lastModifieddate;
  
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

//=================Override clone method=====================
	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
//*******************************Method for CompareFileds On supplier contact*************************
public List<SupplierHistory> compareFields(SupplierContact supplierContact) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
	    List<SupplierHistory> supplierHistories = new ArrayList<SupplierHistory>();      
	    Field[] fields = this.getClass().getDeclaredFields();

	    for(Field field : fields){
	    	String fieldName = field.getName();
	    	
	    	if (!CommonUtils.getUnusedFieldsOfHistory().contains(fieldName.toLowerCase())) {
		        Object oldValue = field.get(this);
		        Object newValue = field.get(supplierContact);
		        
		        if (oldValue == null) {
		        	if (newValue != null) {
		        		supplierHistories.add(this.prepareSupplierHistory(supplierContact, field));
		        	}
		        } else if (!oldValue.equals(newValue)){
		        	supplierHistories.add(this.prepareSupplierHistory(supplierContact, field));
		        }
	    	}
	    }
	    return supplierHistories;
	}

	private SupplierHistory prepareSupplierHistory(SupplierContact supplierContact, Field field) throws IllegalAccessException {
		SupplierHistory supplierHistory = new SupplierHistory();
		supplierHistory.setSupplierId(supplierContact.getSupplierId());
		supplierHistory.setChildId(supplierContact.getId());
		supplierHistory.setModuleName(AppConstants.SUPPLIER_CONTACT);
		supplierHistory.setChangeType(AppConstants.UI);
		supplierHistory.setOperation(Operation.UPDATE.toString());
		supplierHistory.setLastModifiedBy(supplierContact.getLastModifiedBy());
		supplierHistory.setFieldName(CommonUtils.splitCamelCaseWithCapitalize(field.getName()));
		if (field.get(this) != null) supplierHistory.setOldValue(field.get(this).toString());
		if (field.get(supplierContact) != null) supplierHistory.setNewValue(field.get(supplierContact).toString());
		return supplierHistory;
	}
	
	
	
	
	
	
	
	
}
