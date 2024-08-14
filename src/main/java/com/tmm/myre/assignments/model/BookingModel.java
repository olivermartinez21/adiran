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
@Table(name = "MYRE_BOOKINGS", schema = "MYRE")
public class BookingModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "BOOKING_ID")private String bookingId;
	@Column(name = "TYPE")private String type;
	@Column(name = "SIZE")private String size;
	@Column(name = "BOOKING")private String booking;
	@Column(name = "STATUS")private String status;
	@Column(name = "QUALITY")private String quality;
	@Column(name = "SHIPPYNG_COMPANY")private String shippingCompany;
	@Column(name = "QUANTITY_UNITS")private String quantityUnits;
	@Column(name = "QUANTITY_OF_UNITS_USE")private String quantityUnitsUse;
	@Column(name = "FINAL_CLIENT")private String finalClient;
	@Column(name = "BILL_TO")private String billTo;
	@Column(name = "LOCATION")private String location;
	@Column(name = "WORK_ORDER")private String workOrder;
	@Column(name = "EXPIRATION_DATE")private Date expirationDate;
	@Column(name = "RELEASE_DATE")private Date releaseDate;
	@Column(name = "CREATION_DATE")private Date creationDate;
}
