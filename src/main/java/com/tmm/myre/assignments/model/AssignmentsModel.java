package com.tmm.myre.assignments.model;


import java.sql.Date;

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

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MYRE_ASSIGNMENTS", schema = "MYRE")
public class AssignmentsModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ASSIGNMENT_ID")private String assigntmentId;
	@Column(name = "BOOKING")private String booking;
	@Column(name = "GRADE_QUALITY")private String gradeQuality;
	@Column(name = "UNIT_TYPE")private String unitType;
	@Column(name = "UNIT_SIZE")private String unitSize;
	@Column(name = "QUANTITY_OF_UNITS")private String quantityOfUnits;
	@Column(name = "QUANTITY_OF_UNITS_USE")private String quantityOfUnitsUse;
	@Column(name = "FINAL_CLIENT")private String finalClient;
	@Column(name = "LOCATION")private String location;
	@Column(name = "OWNER_COBRATE_A")private String ownerCobrateA;
	@Column(name = "CONDITION_ASSIGNMENT")private String conditionAssignment;
	@Column(name = "WO")private String wo;
	@Column(name = "EXPIRATION_DATE")private Date expirationDate;
	@Column(name = "DATE_OF_RELEASE")private Date dateOfRelease;
	@Column(name = "OBSERVATIONS")private String observations;
	@Column(name = "TECHNOLOGY")private String technology;
	@Column(name = "VENTILATION")private String ventilation;
	@Column(name = "TEMPERATURE")private String temperature;
	@Column(name = "HUMIDITY")private String humidity;
	@Column(name = "CO2")private String co2;
	@Column(name = "O2")private String o2;
	@Column(name = "NITROGEN")private String nitrogen;
	@Column(name = "SHIPPING_COMPANY")private String shippingCompany;
	@Column(name = "DATE_EDI")private Date dateEdi;
	@Column(name = "STATUS")private Integer status;
	
	
}
