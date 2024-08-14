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
public class AssignmentsDto extends AbstractManagement implements ITransferObject {

	private static final long serialVersionUID = 1L;
	
	private String assigntmentId;
	private String booking;
	private String gradeQuality;
	private String unitType;
	private String unitSize;
	private String quantityOfUnits;
	private String quantityOfUnitsUse;
	private String finalClient;
	private String location;
	private String ownerCobrateA;
	private String conditionAssignment;
	private String wo;
	private Date expirationDate;
	private Date dateOfRelease;
	private String observations;
	private String technology;
	private String ventilation;
	private String temperature;
	private String humidity;
	private String co2;
	private String o2;
	private String nitrogen;
	private String shippingCompany;
	private Date dateEdi;
	private Integer status;
	
}
