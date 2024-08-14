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
public class BookingDto extends AbstractManagement implements ITransferObject  {

	
private static final long serialVersionUID = 1L;
	
	private String bookingId;
	private String type;
	private String size;
	private String booking;
	private String status;
	private String quality;
	private String shippingCompany;
	private String quantityUnits;
	private String quantityUnitsUse;
	private String finalClient;
	private String billTo;
	private String location;
	private String workOrder;
	private Date expirationDate;
	private Date releaseDate;
	private String asignmentList;
	private Date creationDate;
	

}
