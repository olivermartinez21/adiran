package com.tmm.myre.assignments.dto;

import java.sql.Date;
import java.sql.Time;

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
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentsFullDto extends AbstractManagement implements ITransferObject{
	
private static final long serialVersionUID = 1L;
	
	private String assigntmentFullId;
	private String consecutiveNumber;
	private String unit;
	private String size; 
	private String plantDestination; 
	private String platform;  
	private Date dateOfDelivery;  
	private String deliverytime;  
	private Integer status; 
}
