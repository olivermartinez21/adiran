package com.tmm.myre.catalog.dto;


import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CatCustomerDto extends AbstractManagement implements ITransferObject {
	
	private static final long serialVersionUID = 1L;
	private String  customerId;
	private String  customerCode;
	private String  customerName;
	private String  customerType;
	private String  customerAddress;
}
