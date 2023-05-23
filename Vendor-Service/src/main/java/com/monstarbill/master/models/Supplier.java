package com.monstarbill.master.models;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import lombok.ToString;
@Getter
@Setter
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "setup2",name = "supplier")
@ToString
@Audited
@AuditTable("supplier_aud")


public class Supplier implements Cloneable {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
//============================================================
	@Column(name = "external_id")
	private String externalId;
	@Column(name = "account_id")
	private String accountId;
	@Column(name = "uin")
	private String uin;
//=============================Form field===================================
	
	@NotNull(message = "vendor name is mandatory")
	@Column(name = "vendor_name",nullable = false,unique = true)
	private String vendorName;
	
	@Column(name = "vendor_number")
	private String vendorNumber;
	
	@Column
	private String legalName;
	
	@NotNull(message = "vendor type is mandatory")
	@Column(name = "vendor_type")
	private String vendorType;
	
	@Column(name = "nature_of_supply")
	private String natureOfSupply;
	
	@Column(name = "unique_tax_identification_number")
	private String uniqueTaxIdNumber;
	
	@Column(name = "invoice_mail")
	private String invoiceMail;
	
	@Column(name = "tds_witholding")
	private String tdsWitholding;
	
	@Column(name = "payment_term")
	private String paymentTerm;
	@Column(name = "approval_status")
	private String approvalStatus;
	@Column(name = "web_site")
	private String website;
	@Lob
	@Column(name = "logo")
	private byte[] logo; 
	@Column(name = "supplier_access_mailId")
	private String  supplierAccessMailID;
	@Column(name = "integrated_id")
	private String integratedID;

//----------------------some extra active details---------------------------	
	
	@Column(name = "isActive",columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isActive;

	@Column(name="active_date")
	private Date activeDate;

	@Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isDeleted;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@UpdateTimestamp
	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	
//===============================join list of another table===================	
	
	@Transient
	private List<SupplierContact> supplierContacts;

	@Transient
	private List<SupplierSubsidiary> supplierSubsidiary;


	
//=================Override clone method=====================
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
//*******************************Method for CompareFileds in supplier*************************
	public List<SupplierHistory> compareFields(Supplier supplier)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		List<SupplierHistory> supplierHistories = new ArrayList<SupplierHistory>();
		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();

			if (!CommonUtils.getUnusedFieldsOfHistory().contains(fieldName.toLowerCase())) {
				Object oldValue = field.get(this);
				Object newValue = field.get(supplier);

				if (oldValue == null) {
					if (newValue != null) {
						supplierHistories.add(this.prepareSupplierHistory(supplier, field));
					}
				} else if (!oldValue.equals(newValue)) {
					supplierHistories.add(this.prepareSupplierHistory(supplier, field));
				}
			}
		}
		return supplierHistories;
	}

	private SupplierHistory prepareSupplierHistory(Supplier supplier, Field field) throws IllegalAccessException {
		SupplierHistory supplierHistory = new SupplierHistory();
		supplierHistory.setSupplierId(supplier.getId());
		supplierHistory.setModuleName(AppConstants.SUPPLIER);
		supplierHistory.setChangeType(AppConstants.UI);
		supplierHistory.setLastModifiedBy(supplier.getLastModifiedBy());
		supplierHistory.setOperation(Operation.UPDATE.toString());
		supplierHistory.setFieldName(CommonUtils.splitCamelCaseWithCapitalize(field.getName()));
		if (field.get(this) != null) supplierHistory.setOldValue(field.get(this).toString());
		if (field.get(supplier) != null) supplierHistory.setNewValue(field.get(supplier).toString());
		return supplierHistory;
	}
	
}
