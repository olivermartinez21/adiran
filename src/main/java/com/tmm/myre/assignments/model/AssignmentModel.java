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
@Table(name = "MYRE_ASSIGNMENT", schema = "MYRE")
public class AssignmentModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	

	@Id
	@Column(name = "ASSIGNMENT_ID")private String id;
	@Column(name = "NO")private String no;
	@Column(name = "TYPE  ")private String type;
	@Column(name = "SIZE  ")private String size;
	@Column(name = "QUALITY")private String quality;
	@Column(name = "UNITNUMBER")private String unitNumber;
	@Column(name = "TEMPERATUERE ")private String temperatuere;
	@Column(name = "VENTILATION ")private String ventilation;
	@Column(name = "HUMITY")private String humity;
	@Column(name = "CO2")private String co2;
	@Column(name = "O2 ")private String o2;
	@Column(name = "NITROGEN ")private String nitrogen;
	@Column(name = "BOOKING_ID")private String bookingId;
	@Column(name = "DELIVERY_ORDER_ID")private String deliveryOrderId;
	@Column(name = "STATUS")private int status;
	
	
	
}
