package com.tmm.myre.inspections.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.model.InspectionModel;
@Component("inspectionConverter")
public class InspectionConverter implements IConverter<InspectionModel, InspectionDto> {

	@Override
	public InspectionModel convert(InspectionDto to) throws ConverterException {
		InspectionModel entity = InspectionModel.builder()
				.inspectionId(to.getInspectionId())
				.part(to.getPart())
				.component(to.getComponent())
				.repair(to.getRepair())
				.location(to.getLocation())
				.damage(to.getDamage())
				.damageCode(to.getDamageCode())
				.reference(to.getReference())
				.customerType(to.getCustomerType())
				.photo(to.getPhoto())
				.status(to.getStatus())
				.damageGenSet(to.getDamageGenSet())
				.containerId(to.getContainerId())
				.length(to.getLength())
				.width(to.getWidth())
				.depth(to.getDepth())
				.otherLength(to.getOtherLength())
				.quantity(to.getQuantity())
				.build();
		return entity;
	}

	@Override
	public InspectionDto convert(InspectionModel entity) throws ConverterException {
		InspectionDto to = InspectionDto.builder()
				.inspectionId(entity.getInspectionId())
				.part(entity.getPart())
				.damage(entity.getDamage())
				.component(entity.getComponent())
				.repair(entity.getRepair())
				.location(entity.getLocation())
				.damageCode(entity.getDamageCode())
				.reference(entity.getReference())
				.customerType(entity.getCustomerType())
				.photo(entity.getPhoto())
				.status(entity.getStatus())
				.damageGenSet(entity.getDamageGenSet())
				.containerId(entity.getContainerId())
				.length(entity.getLength())
				.width(entity.getWidth())
				.depth(entity.getDepth())
				.otherLength(entity.getOtherLength())
				.quantity(entity.getQuantity())
				.build();
		return to;
	}

}
