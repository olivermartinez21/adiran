package com.tmm.myre.base.configuration.secondary;


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
@Table(name = "GateEventsIntegration", schema = "Integracion")
public class IntegrationModel implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "eventDetailsIdentifier")private Integer eventDetailsIdentifier;
	@Column(name = "eventType")private String eventType; 
	@Column(name = "eventDate")private Date eventDate;
	@Column(name = "estimateRequired")private String estimateRequired;
	@Column(name = "inspected")private String inspected; 
	@Column(name = "inspectedBy")private String inspectedBy; 
	@Column(name = "booking")private String booking;
	@Column(name = "fillState")private String fillState;
	@Column(name = "alternateUnit")private String alternateUnit;
	@Column(name = "associatedUnit")private String associatedUnit;
	@Column(name = "transportType")private String transportType;
	@Column(name = "sapSaleOrder")private String sapSaleOrder;
	@Column(name = "unitQuality")private String unitQuality;
	@Column(name = "sealNumber")private String sealNumber;
	@Column(name = "customerIdentifier")private String customerIdentifier;
	@Column(name = "type")private String type;
	@Column(name = "model")private String model;
	@Column(name = "location")private String location;
	@Column(name = "container")private String container;
	@Column(name = "modelYear")private String modelYear;
	@Column(name = "gateEventIdentifier")private Integer gateEventIdentifier;
	
}