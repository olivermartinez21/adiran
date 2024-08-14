package com.tmm.myre.event.converter;


import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.event.dto.EventInformationDto;
import com.tmm.myre.event.model.EventInformationModel;
@Component("eventInformationConverter")
public class EventInformationConverter implements IConverter<EventInformationModel, EventInformationDto> {

	@Override
	public EventInformationModel convert(EventInformationDto to) throws ConverterException {
		EventInformationModel entity = EventInformationModel.builder()
		.eventId(to.getEventId())
		.eventType(to.getEventType()) 
		.eventDate(to.getEventDate())
		.estimateRequired(to.getEstimateRequired())
		.inspected(to.getInspected()) 
		.inspectedBy(to.getInspectedBy()) 
		.booking(to.getBooking())
		.fillState(to.getFillState())
		.alternateUnit(to.getAlternateUnit())
		.associatedUnit(to.getAssociatedUnit())
		.transportType(to.getTransportType())
		.sapSaleOrder(to.getSapSaleOrder())
		.unitQuality(to.getUnitQuality())
		.sealNumber(to.getSealNumber())
		.customerIdentifier(to.getCustomerIdentifier())
		.type(to.getType())
		.model(to.getModel())
		.location(to.getLocation())
		.container(to.getContainer())
		.modelYear(to.getModelYear())
		.containerId(to.getContainerId())
		.transmit(to.getTransmit())
		.build();
		return entity;
	}

	@Override
	public EventInformationDto convert(EventInformationModel entity) throws ConverterException {
		EventInformationDto to = EventInformationDto.builder()
				.eventId(entity.getEventId())
				.eventType(entity.getEventType()) 
				.eventDate(entity.getEventDate())
				.estimateRequired(entity.getEstimateRequired())
				.inspected(entity.getInspected()) 
				.inspectedBy(entity.getInspectedBy()) 
				.booking(entity.getBooking())
				.fillState(entity.getFillState())
				.alternateUnit(entity.getAlternateUnit())
				.associatedUnit(entity.getAssociatedUnit())
				.transportType(entity.getTransportType())
				.sapSaleOrder(entity.getSapSaleOrder())
				.unitQuality(entity.getUnitQuality())
				.sealNumber(entity.getSealNumber())
				.customerIdentifier(entity.getCustomerIdentifier())
				.type(entity.getType())
				.model(entity.getModel())
				.location(entity.getLocation())
				.container(entity.getContainer())
				.modelYear(entity.getModelYear())
				.containerId(entity.getContainerId())
				.transmit(entity.getTransmit())
				.build();
		return to;
	}

}
