package com.tmm.myre.appointments.dto;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Lob;

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
public class AppointmentDto extends AbstractManagement implements ITransferObject {
	
	private static final long serialVersionUID = 1L;
	
	private String  appointmentId;
	private String location;
	private String  folio;
	private Date  date;
	private Date  creationAppointmentDate;
	private String  telephone;
	private String  agency;
	private String  customer;
	private Integer  customerType;
	private Integer  containers;
	private Integer  containersType;
	private String  shippingCompany;
	private Integer  invoincingType;
	private String  user;
	private Integer  eventType;
	private Integer  paymentType;
	private String  buque;
	private String  origin;
	//private String signature;
	private Integer  status;
	private String companyName;
	private String fiscalAdress;
	private String rfc;
	private String price="";
	
	private String paymentCheck;
	
	private String containersList;
	private String warehouse;
	
	private String fileName;
	private String fileType;
	private byte[] fileContent;
	
	
	
	@Builder
	public AppointmentDto( 
			Integer idUser, String operation, Date managmentDate, String log, String appointmentId, String folio, Date date, Date creationAppointmentDate, String telephone, String agency, String customer, Integer customerType, Integer containers, Integer containersType, String shippingCompany, Integer invoincingType, Integer eventType, String user, Integer status, String companyName, String fiscalAdress, String rfc, String payment, String containersList, String location, Integer paymentType, String origin, String buque, String paymentCheck, String warehouse, byte[] fileContent, String fileType, String fileName) {
		
		super(idUser, operation, managmentDate, log);
		
		
		this.appointmentId = appointmentId;
		this.location = location;
		this.folio = folio;
		this.date =  date;
		this.creationAppointmentDate =   creationAppointmentDate;
		this.telephone = telephone;
		this.agency = agency;
		this.customer = customer;
		this.customerType =   customerType;
		this.containers =   containers;
		this.containersType =   containersType;
		this.shippingCompany = shippingCompany;
		this.invoincingType =   invoincingType;
		this.eventType =   eventType;
		this.user = user;
		this.paymentType = paymentType;
		this.buque = buque;
		this.origin = origin;
		
		this.status =   status;
		this.companyName = companyName;
		this.fiscalAdress = fiscalAdress;
		this.rfc = rfc;
		this.containersList = containersList;
		this.paymentCheck = paymentCheck;
		this.warehouse = warehouse;
		
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileContent = fileContent;
		
	}
}
