package com.tmm.myre.appointments.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "MYRE_APPOINTMENTS", schema = "MYRE")
public class AppointmentModel implements IModel {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "APPOINTMENT_ID")private String appointmentId;
	@Column(name = "LOCATION")private String location;
	@Column(name = "FOLIO")private String folio;
	@Column(name = "DATE")private Date date;
	@Column(name = "CREATION_APPOINTMENT_DATE")private Date creationAppointmentDate;
	@Column(name = "TELEPHONE")private String telephone;
	@Column(name = "AGENCY")private String agency;
	@Column(name = "CUSTOMER")private String customer;
	@Column(name = "CUSTOMER_TYPE")private Integer customerType;
	@Column(name = "CONTAINERS")private Integer containers;
	@Column(name = "CONTAINERS_TYPE")private Integer containersType;
	@Column(name = "SHIPPING_COMPANY")private String shippingCompany;
	@Column(name = "INVOINCING_TYPE")private Integer invoincingType;
	@Column(name = "USER")private String user;
	@Column(name = "SERVICE_TYPE")private Integer eventType;
	@Column(name = "PAYMENT_TYPE")private Integer paymentType;
	@Column(name = "COMPANY_NAME")private String companyName;
	@Column(name = "FISCAL_ADRESS")private String fiscalAdress;
	@Column(name = "RFC")private String rfc;
	@Column(name = "BUQUE")private String  buque;
	@Column(name = "ORIGIN")private String  origin;
	@Column(name = "PAYMENT_CHECK")private String paymentCheck;
	//@Column(name = "SIGNATURE")private String signature;
	@Column(name = "STATUS")private Integer status;
	
	@Column(name = "FILE_NAME") private String fileName;
	@Column(name = "FILE_TYPE") private String fileType;
	
	@Column(name = "FILE_CONTENT")  //private String fileContent;
	@Lob
	private byte[] fileContent;
	
	

}
