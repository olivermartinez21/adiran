package com.tmm.myre.containers.dto;

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
public class ContainerHistoricDto extends AbstractManagement implements ITransferObject{

private static final long serialVersionUID = 1L;
	
	private String  containerId;
	private Date  registerDate;
	private String  location;
	private String  container;
	private Integer  containerType;
	private String  nomenclatura;
	private String  containerSize;
	private String  shippingCompany;
	private String  booking;
	private String  bookingQuantity;
	private Integer  eventType;
	
	private String  modelYear;
	
	
	private Date  dateInspection;
	
	
	private String assignedTo;
	private String startDate;
	private String finalDate;
	private String daysStay;
	
	
	private String  definition;
	private String  coments;
	private Integer  status;
	private String clasification;
	private String inspectionList;
	
	private String destination;
	private String chassis;
	private String associateUnit;
	private String associateUnitGenset;
	private String mark;
	private String setPoint;
	private String ventilation;
	
	private String horometro;
	private String generatorType;
	private String technology;
	
	
	private String diesel;
	private String cms;
	private String hours;
	
	private String  appointmentId;
	private String rol;
	
	//pregate
	private String  condition;
	private String  aptTo;
	private String  conditionPregate;
	private String  typeServicePregate;
	private String  billTo;
	private String  transportId;
	private String  buque;
	private String  bl;
	private String  operatorName;
	private String  plate;
	private String  economicNumber;
	private Integer  transmit;
	private String  containerTypeEvent;
	private String  qualityEvent;
	private String  eirName;
	
	
	private String  temperature;
	private String  humiity;
	private String  co2;
	private String  o2;
	private String  ni;
	private String  eirOutName;
	private String  qualityStamp;
	private String  securityStamp;
	
	private int num;
	private int filas;
	
	private String imageList;
	
	private Integer  statusQute;
	
	private String quoteName;
	
	private String comnetsQuote;
	
	byte[] eir;
	
	byte[] eirOut;
	
	byte[] quote;
	
	private String dataUrl;
	
	
	
	
	
	@Builder
	public ContainerHistoricDto( 
			Integer idUser, String operation, Date managmentDate, String log, String containerId, String container, Integer containerType, String containerSize, String shippingCompany, String appointmentId, Date dateInspection, String vessel, String origin, String aa, String definition, String coments, String inspectionList, String travel, Integer status, String destination, String chassis, String associateUnit, String mark, String setPoint, String ventilation, String clasification, String modelYear, String bookingQuantity, String condition, String typeServicePregate, String billTo, String transportId, String buque, String bl, String operatorName, String plate, String economicNumber, String conditionPregate, String aptTo, String nomenclatura, Integer transmit, String location, String containerTypeEvent, String qualityEvent, String associateUnitGenset, byte[] eir, String eirName, int num, String dataUrl, String temperature, String humiity, String co2, String eirOutName, String ni, String o2, String securityStamp, String qualityStamp, byte[] eirOut, String imageList, Integer statusQute, String quoteName, byte[] quote, String assignedTo, String startDate, String finalDate, String daysStay, String booking, String comnetsQuote) {
		
		super(idUser, operation, managmentDate, log);
		
		
		this.containerId = containerId;
		this.container= container;
		this.containerType = containerType;
		this.containerSize =  containerSize;
		this.shippingCompany =   shippingCompany;
		this.appointmentId = appointmentId;
		
		this.dateInspection =dateInspection;
		this.definition =   definition;
		this.coments = coments;
		this.status = status;
		this.inspectionList = inspectionList;
		
		this.destination = destination;
		this.chassis = chassis;
		this.associateUnit = associateUnit;
		this.mark = mark;
		this.setPoint = setPoint;
		this.ventilation = ventilation;
		this.clasification = clasification;
		this.modelYear = modelYear;
		this.bookingQuantity = bookingQuantity;
		this.booking = booking;
		this.condition=condition;
		this.typeServicePregate=typeServicePregate;
		this.billTo=billTo;
		this.transportId=transportId;
		this.buque=buque;
		this.bl=bl;
		this.operatorName=operatorName;
		this.plate=plate;
		this.economicNumber=economicNumber;
		this.conditionPregate=conditionPregate;
		this.aptTo=aptTo;
		this.nomenclatura=nomenclatura;
		this.transmit=transmit;
		this.location=location;
		this.containerTypeEvent=containerTypeEvent;
		this.qualityEvent=qualityEvent;
		this.associateUnitGenset=associateUnitGenset;
		this.eir=eir;
		this.eirName=eirName;
		this.num=num;
		this.dataUrl=dataUrl;
		
		this.temperature=temperature;
		this.humiity=humiity;
		this.co2=co2;
		this.o2=o2;
		this.ni=ni;
		this.eirOutName=eirOutName;
		this.eirOut=eirOut;
		this.qualityStamp=qualityStamp;
		this.securityStamp=securityStamp;
		this.imageList=imageList;
		this.statusQute=statusQute;
		this.quoteName=quoteName;
		this.quote=quote;
		
		this.assignedTo=assignedTo;
		this.startDate=startDate;
		this.finalDate=finalDate;
		this.daysStay=daysStay;
		this.comnetsQuote=comnetsQuote;
		
	}
}
