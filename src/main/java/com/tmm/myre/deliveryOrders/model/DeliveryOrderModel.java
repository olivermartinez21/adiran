package com.tmm.myre.deliveryOrders.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.tmm.myre.base.model.IModel;

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
@Entity
@Table(name = "MYRE_DELIVERY_ORDER", schema = "MYRE")
public class DeliveryOrderModel implements IModel{
	
	private static final long serialVersionUID = 1L;
		@Id
		@Column(name = "DELIVERY_ORDER_ID")private String deliveryOrderId;
		@Column(name = "BOOKING")private String booking;
		@Column(name = "OWNER")private String owner;
		@Column(name = "TYPE_OF_SERVICE")private String typeOfService;
		@Column(name = "REMAINING_UNITS")private String remainingUnits;
		@Column(name = "BILL_TO")private String billTo;
		@Column(name = "CARRIER_COMPANY")private String carrierCompany;
		@Column(name = "OPERATOR")private String operator;
		@Column(name = "ECONOMIC_NUMBER")private String economicNumber;
		@Column(name = "WORK_ORDER")private String workOrder;
		@Column(name = "QUANTITY_OF_UNIT")private String quantityOfUnits;
		@Column(name = "LOCATION")private String location;
		@Column(name = "ASSIGNMENT_ID")private String assignmentId;
		@Column(name = "FILE_NAME") private String fileName;
		@Column(name = "CONTAINER_TYPE") private Integer containerType;
		@Column(name = "FILE_TYPE") private String fileType;
		@Column(name = "FILE_CONTENT")  //private String fileContent;
		@Lob
		private byte[] fileContent;
		
		
}
