package com.tmm.myre.assignments.dto;

import java.sql.Date;

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
public class AssignmentDto extends AbstractManagement implements ITransferObject {

	private static final long serialVersionUID = 1L;
	
	private String id; 
	private String no; 
	private String type ;
	private String size ;
	private String quality; 
	private String unitNumber; 
	private String temperatuere;
	private String ventilation;
	private String humity; 
	private String co2; 
	private String o2;
	private String nitrogen;
	private String bookingId;
	private String deliveryOrderId;
	private int status;
		
}
