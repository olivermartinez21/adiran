package com.tmm.myre.containers.model;


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

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MYRE_CONTAINERS", schema = "MYRE")
public class ContainerModel implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CONTAINER_ID")private String containerId;
	@Column(name = "REGISTER_DATE")private Date  registerDate;
	@Column(name = "LOCATION")private String  location;
	@Column(name = "CONTAINER")private String container;
	@Column(name = "CONTAINER_TYPE")private Integer containerType;
	@Column(name = "CONTAINER_ZISE")private String contaierSize;
	@Column(name = "SHIPPING_COMPANY")private String shippingCompany;
	@Column(name = "BOOKING")private String bokking;
	@Column(name = "BOOKING_QUANTITY")private String  bookingQuantity;
	@Column(name = "SERVICE_TYPE")private Integer eventType;
	@Column(name = "MODEL_YEAR")private String modelYear;
	@Column(name = "CONTAINER_CONDITION")private String condition;
	
	
	@Column(name = "DATE_INSPECTION")private Date  dateInspection;
	
	@Column(name = "VESSEL")private String assignedTo;
	@Column(name = "TRAVEL")private String startDate;
	@Column(name = "ORIGIN")private String finalDate;
	@Column(name = "AA")private String daysStay;
	
	@Column(name = "DEFINITION")private String definition;
	@Column(name = "COMENTS")private String coments;
	@Column(name = "STATUS")private Integer status;
	
	@Column(name = "HOROMOETRO")private String horometro;
	@Column(name = "GENERATOR_TYPE")private String generatorType;
	@Column(name = "TECHNOLOGY")private String technology;
	
	@Column(name = "CLASIFICATION")private String clasification;
	
	@Column(name = "DESTINATION")private String destination;
	@Column(name = "CHASSIS")private String chassis;
	@Column(name = "ASSOCIATE_UNIT")private String associateUnit;
	@Column(name = "MARK")private String mark;
	@Column(name = "SET_POINT")private String setPoint;
	@Column(name = "VENTILATION")private String ventilation;
	
	@Column(name = "DIESEL")private String diesel;
	@Column(name = "CMS")private String cms;
	@Column(name = "HOURS")private String hours;
	
	@Column(name = "APPOINTMENT_ID")private String appointmentId;
	@Column(name = "NOMENCALTURA")private String nomenclatura;
	@Column(name = "CONTAINER_CONDITION_PREGATE")private String conditionPregate;
	@Column(name = "SERVICE_TYPE_PREGATE")private String  typeServicePregate;
	@Column(name = "APT_TO")private String  aptTo;
	@Column(name = "BILL_TO")private String  billTo;
	@Column(name = "TRANSPORT_ID")private String  transportId;
	@Column(name = "BUQUE")private String  buque;
	@Column(name = "BL")private String  bl;
	@Column(name = "OPERATOR_NAME")private String  operatorName;
	@Column(name = "PLATE")private String  plate;
	@Column(name = "ECONOMIC_NUMBER")private String  economicNumber;
	@Column(name = "EIR_NAME")private String  eirName;
	
	
	@Column(name = "TEMPERATURE")private String  temperature;
	@Column(name = "HUMITY")private String  humiity;
	@Column(name = "CO2")private String  co2;
	@Column(name = "O2")private String  o2;
	@Column(name = "NI")private String  ni;
	@Column(name = "EIR_OUT_NAME")private String  eirOutName;
	@Column(name = "QUALITY_STAMP")private String  qualityStamp;
	@Column(name = "SECURITY_STAMP")private String  securityStamp;
	
	@Column(name = "STATUS_QUOTE")private Integer  statusQute;
	
	@Column(name = "QUOTE_NAME")private String  quoteName;
	@Column(name = "COMENTS_QUOTE")private String  comnetsQuote;
	@Column(name = "APROVED_QUOTE")private Date  aprovedQuote;
	@Column(name = "EIR_OUT")  //private String fileContent;
	@Lob
	private byte[] eirOut;
	@Column(name = "EIR")  //private String fileContent;
	@Lob
	private byte[] eir;
	
	@Column(name = "QUOTE")  //private String fileContent;
	@Lob
	private byte[] quote;	
	
	@Column(name = "ORIGIN_PREGATE")private String originPregate;
	
	
	
	
}
