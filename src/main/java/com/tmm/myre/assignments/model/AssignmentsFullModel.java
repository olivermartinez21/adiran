package com.tmm.myre.assignments.model;

import java.sql.Date;
import java.sql.Time;

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
@Table(name = "MYRE_ASSIGNMENTS_FULL", schema = "MYRE")
public class AssignmentsFullModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ASSIGNMENT_FULL_ID")private String assigntmentFullId;
	@Column(name = "CONSECUTIVE_NUMBER")private String consecutiveNumber;
	@Column(name = "UNIT")private String unit;
	@Column(name = "SIZE")private String size; 
	@Column(name = "PLANT_DESTINATION")private String plantDestination; 
	@Column(name = "PLATFORM")private String platform;  
	@Column(name = "DATEOFDELIVERY")private Date dateOfDelivery;  
	@Column(name = "DELIVERYTIME")private String deliverytime;  
	@Column(name = "STATUS")private Integer status; 
}
