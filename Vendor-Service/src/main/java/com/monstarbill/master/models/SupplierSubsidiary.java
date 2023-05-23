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
@Entity
@Table(schema = "setup2",name = "suppplier_subsidiary")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@AuditTable("supplier_subsidiary_aud")
public class SupplierSubsidiary implements Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "supplier_id")
	private Long supplierId;

//=========================Supplier Subsidiary form field==================
	
	@NotNull(message = "subsidiary id is mandatory")
	@Column(name = "subsidiary_id")
	private Long subsidiaryId;
	
	@NotNull(message = "supplier currency is mandatory")
	@Column(name ="supplier_currency")
	private String supplierCurrency;
	
	@NotNull(message = "subisidiary currency is mandatory")
	@Column(name ="subisidiary_currency" )
	private String subisidiaryCurrency;
	
	@Column(name = "is_preferred_currency",columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isPreferredCurrency;
	
//========================some extra information in time details=========================	
	
	@Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isDeleted;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "created_by", updatable = false)
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
	
	
//*******************************Method for CompareFileds On supplier subsidiary contact*************************
	public List<SupplierHistory> compareFields(SupplierSubsidiary supplierSubsidiary)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		List<SupplierHistory> supplierHistories = new ArrayList<SupplierHistory>();
		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();

			if (!CommonUtils.getUnusedFieldsOfHistory().contains(fieldName.toLowerCase())) {
				Object oldValue = field.get(this);
				Object newValue = field.get(supplierSubsidiary);

				if (oldValue == null) {
					if (newValue != null) {
						supplierHistories.add(this.prepareSupplierHistory(supplierSubsidiary, field));
					}
				} else if (!oldValue.equals(newValue)) {
					supplierHistories.add(this.prepareSupplierHistory(supplierSubsidiary, field));
				}
			}
		}
		return supplierHistories;
	}

	private SupplierHistory prepareSupplierHistory(SupplierSubsidiary supplierSubsidiary, Field field) throws IllegalAccessException {
		SupplierHistory supplierHistory = new SupplierHistory();
		supplierHistory.setSupplierId(supplierSubsidiary.getSupplierId());
		supplierHistory.setChildId(supplierSubsidiary.getId());
		supplierHistory.setModuleName(AppConstants.SUPPLIER_SUBSIDIARY);
		supplierHistory.setChangeType(AppConstants.UI);
		supplierHistory.setLastModifiedBy(supplierSubsidiary.getLastModifiedBy());
		supplierHistory.setOperation(Operation.UPDATE.toString());
		supplierHistory.setFieldName(CommonUtils.splitCamelCaseWithCapitalize(field.getName()));
		if (field.get(this) != null) supplierHistory.setOldValue(field.get(this).toString());
		if (field.get(supplierSubsidiary) != null) supplierHistory.setNewValue(field.get(supplierSubsidiary).toString());
		return supplierHistory;
	}
	
}
