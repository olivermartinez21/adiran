package com.tmm.myre.assignments.converter;


import org.springframework.stereotype.Component;

import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.assignments.model.AssignmentsFullModel;
import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
@Component("assignmentsFullConverter")
public class AssignmentsFullConverter  implements IConverter<AssignmentsFullModel, AssignmentsFullDto>{

	@Override
	public AssignmentsFullModel convert(AssignmentsFullDto to) throws ConverterException {
		AssignmentsFullModel entity = AssignmentsFullModel.builder()
		.assigntmentFullId(to.getAssigntmentFullId())
		.consecutiveNumber(to.getConsecutiveNumber())
		.unit(to.getUnit())
		.size(to.getSize()) 
		.plantDestination(to.getPlantDestination()) 
		.platform(to.getPlatform())  
		.dateOfDelivery(to.getDateOfDelivery())  
		.deliverytime(to.getDeliverytime())  
		.status(to.getStatus()) 
		.build();
		return entity;
	}

	@Override
	public AssignmentsFullDto convert(AssignmentsFullModel entity) throws ConverterException {
		AssignmentsFullDto to = AssignmentsFullDto.builder()
				.assigntmentFullId(entity.getAssigntmentFullId())
				.consecutiveNumber(entity.getConsecutiveNumber())
				.unit(entity.getUnit())
				.size(entity.getSize()) 
				.plantDestination(entity.getPlantDestination()) 
				.platform(entity.getPlatform())  
				.dateOfDelivery(entity.getDateOfDelivery())  
				.deliverytime(entity.getDeliverytime())  
				.status(entity.getStatus()) 
				.build();
		return to;
	}

}
