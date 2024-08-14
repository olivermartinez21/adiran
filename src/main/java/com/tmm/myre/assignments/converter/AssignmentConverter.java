package com.tmm.myre.assignments.converter;


import org.springframework.stereotype.Component;

import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;

@Component("assignmentConverter")
public class AssignmentConverter implements IConverter<AssignmentModel, AssignmentDto>{

	@Override
	public AssignmentModel convert(AssignmentDto to) throws ConverterException {
		AssignmentModel entity = AssignmentModel.builder()
		.id(to.getId()) 
		.no(to.getNo()) 
		.type (to.getType())
		.size (to.getSize())
		.quality(to.getQuality()) 
		.unitNumber(to.getUnitNumber()) 
		.temperatuere(to.getTemperatuere())
		.ventilation(to.getVentilation())
		.humity(to.getHumity()) 
		.co2(to.getCo2()) 
		.o2(to.getO2())
		.nitrogen(to.getNitrogen())
		.bookingId(to.getBookingId())
		.deliveryOrderId(to.getDeliveryOrderId())
		.status(to.getStatus())
				.build();
		return entity;
	}

	@Override
	public AssignmentDto convert(AssignmentModel entity) throws ConverterException {
		AssignmentDto to = AssignmentDto.builder()
				.id(entity.getId()) 
				.no(entity.getNo()) 
				.type (entity.getType())
				.size (entity.getSize())
				.quality(entity.getQuality()) 
				.unitNumber(entity.getUnitNumber()) 
				.temperatuere(entity.getTemperatuere())
				.ventilation(entity.getVentilation())
				.humity(entity.getHumity()) 
				.co2(entity.getCo2()) 
				.o2(entity.getO2())
				.nitrogen(entity.getNitrogen())
				.bookingId(entity.getBookingId())
				.deliveryOrderId(entity.getDeliveryOrderId())
				.status(entity.getStatus())
				.build();
		return to;
	}

}
