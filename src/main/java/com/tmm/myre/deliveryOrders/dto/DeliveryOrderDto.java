package com.tmm.myre.deliveryOrders.dto;


import javax.persistence.Column;
import javax.persistence.Lob;

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
public class DeliveryOrderDto extends AbstractManagement implements ITransferObject {
	
	private static final long serialVersionUID = 1L;
		
		private String deliveryOrderId;
		private String booking;
		private String owner;
		private String typeOfService;
		private String remainingUnits;
		private String billTo;
		private String carrierCompany;
		private String operator;
		private String economicNumber;
		private String workOrder;
		private String quantityOfUnits;
		private String location;
		private String assignmentId;
		private String [] assignmentList;
		private String fileName;
		private String fileType;
		private byte[] fileContent;
		private Integer containerType;
		
		
		
		
}
