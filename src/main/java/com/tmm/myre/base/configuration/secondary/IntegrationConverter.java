package com.tmm.myre.base.configuration.secondary;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.event.dto.EventInformationDto;
@Component("integrationConverter")
public class IntegrationConverter implements IConverter<IntegrationModel, EventInformationDto>{

	@Override
	public IntegrationModel convert(EventInformationDto to) throws ConverterException {
		IntegrationModel entity = IntegrationModel.builder()
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
				.build();
		
		return entity;
	}

	@Override
	public EventInformationDto convert(IntegrationModel entity) throws ConverterException {
		EventInformationDto to = EventInformationDto.builder()
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
				.build();
		
		return to;
	}

}
