package com.monstarbill.master.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@Entity
@Table(schema = "setup2", name = "supplier_history")
@ToString

public class SupplierHistory implements Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long supplierId;
	
	private Long childId;
	
//===================================History Field================	
	@Column(name = "moduleName")
	private String moduleName;
	
	private String operation;
	
	@Column(name = "field_name")
	private String fieldName;
	
	@Column(name = "change_type")
	private String changeType;
	
	@Column(name = "new_value")
	private String newValue;
	
	@Column(name = "old_value")
	private String oldValue;
	
	
	//----------------------some extra details---------------------------	
	
 	    @CreationTimestamp
		@Column(name = "created_date")
		private Date createdDate;

		
		private String createdBy;

//		@UpdateTimestamp
		@Column(name = "last_modified_date")
		private Timestamp lastModifiedDate;

		@Column(name = "last_modified_by")
		private String lastModifiedBy;

}
