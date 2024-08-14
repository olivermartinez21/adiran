package com.tmm.myre.event.dto;

import java.sql.Date;

import javax.persistence.Column;

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
public class EventInformationDto extends AbstractManagement implements ITransferObject {
	
	public static final long serialVersionUID = 1L;
	
	//private Integer eventDetailsIdentifier;
	private String eventId;
	private String eventType; 
	private Date eventDate;
	private String estimateRequired;
	private String inspected; 
	private String inspectedBy; 
	private String booking;
	private String fillState;
	private String alternateUnit;
	private String associatedUnit;
	private String transportType;
	private String sapSaleOrder;
	private String unitQuality;
	private String unitClasification;
	private String sealNumber;
	private String customerIdentifier;
	private String type;
	private String model;
	private String location;
	private String container;
	private String modelYear;
	private String containerId;
	private String transmit;
	

}
