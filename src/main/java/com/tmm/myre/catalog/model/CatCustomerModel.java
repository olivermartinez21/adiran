package com.tmm.myre.catalog.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tmm.myre.base.model.IModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MYRE_CAT_CUSTOMERS", schema = "MYRE")
public class CatCustomerModel implements IModel{
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CUSTOMER_ID")private String  customerId;
	@Column(name = "CUSTOMER_CODE")private String  customerCode;
	@Column(name = "CUSTOMER_NAME")private String  customerName;
	@Column(name = "CUSTOMER_TYPE")private String  customerType;
	@Column(name = "COSTUMER_ADDRESS")private String  customerAddress;
}
