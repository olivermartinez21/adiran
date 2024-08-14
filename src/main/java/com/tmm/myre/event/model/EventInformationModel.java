package com.tmm.myre.event.model;


import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "MYRE_EVENT", schema = "MYRE")
public class EventInformationModel implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "EVENT_ID")private String eventId;
	@Column(name = "EVENT_TYPE")private String eventType; 
	@Column(name = "EVENT_DATE")private Date eventDate;
	@Column(name = "ESTIMATE_REQUIRED")private String estimateRequired;
	@Column(name = "INSPECTED")private String inspected; 
	@Column(name = "INSPECTED_BY")private String inspectedBy; 
	@Column(name = "BOOKING")private String booking;
	@Column(name = "FILL_STATE")private String fillState;
	@Column(name = "ALTERNATE_UNIT")private String alternateUnit;
	@Column(name = "ASSOCIATED_UNIT")private String associatedUnit;
	@Column(name = "TRANSPORT_TYPE")private String transportType;
	@Column(name = "SAP_SALE_ORDER")private String sapSaleOrder;
	@Column(name = "UNIT_QUALITY")private String unitQuality;
	@Column(name = "SEAL_NUMBER")private String sealNumber;
	@Column(name = "CUSTOMER_IDENTIFIER")private String customerIdentifier;
	@Column(name = "TYPE")private String type;
	@Column(name = "MODEL")private String model;
	@Column(name = "LOCATION")private String location;
	@Column(name = "CONTAINER")private String container;
	@Column(name = "MODEL_YEAR")private String modelYear;
	@Column(name = "CONTAINER_ID")private String containerId;
	@Column(name = "TRANSMIT")private String transmit;
}