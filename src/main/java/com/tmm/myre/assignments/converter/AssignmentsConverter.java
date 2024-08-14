package com.tmm.myre.assignments.converter;


import org.springframework.stereotype.Component;

import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.model.AssignmentsModel;
import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;

@Component("assignmentsConverter")
public class AssignmentsConverter implements IConverter<AssignmentsModel, AssignmentsDto>{

	@Override
	public AssignmentsModel convert(AssignmentsDto to) throws ConverterException {
		AssignmentsModel entity = AssignmentsModel.builder()
				.assigntmentId(to.getAssigntmentId())
		.booking(to.getBooking())
		.gradeQuality(to.getGradeQuality())
		.unitType(to.getUnitType())
		.unitSize(to.getUnitSize())
		.quantityOfUnits(to.getQuantityOfUnits())
		.quantityOfUnitsUse(to.getQuantityOfUnitsUse())
		.finalClient(to.getFinalClient())
		.location(to.getLocation())
		.ownerCobrateA(to.getOwnerCobrateA())
		.conditionAssignment(to.getConditionAssignment())
		.wo(to.getWo())
		.expirationDate(to.getExpirationDate())
		.dateOfRelease(to.getDateOfRelease())
		.observations(to.getObservations())
		.technology(to.getTechnology())
		.ventilation(to.getVentilation())
		.temperature(to.getTemperature())
		.humidity(to.getHumidity())
		.co2(to.getCo2())
		.o2(to.getO2())
		.nitrogen(to.getNitrogen())
		.shippingCompany(to.getShippingCompany())
		.dateEdi(to.getDateEdi())
		.status(to.getStatus())
				.build();
		return entity;
	}

	@Override
	public AssignmentsDto convert(AssignmentsModel entity) throws ConverterException {
		AssignmentsDto to = AssignmentsDto.builder()
				.assigntmentId(entity.getAssigntmentId())
				.booking(entity.getBooking())
				.gradeQuality(entity.getGradeQuality())
				.unitType(entity.getUnitType())
				.unitSize(entity.getUnitSize())
				.quantityOfUnits(entity.getQuantityOfUnits())
				.quantityOfUnitsUse(entity.getQuantityOfUnitsUse())
				.finalClient(entity.getFinalClient())
				.location(entity.getLocation())
				.ownerCobrateA(entity.getOwnerCobrateA())
				.conditionAssignment(entity.getConditionAssignment())
				.wo(entity.getWo())
				.expirationDate(entity.getExpirationDate())
				.dateOfRelease(entity.getDateOfRelease())
				.observations(entity.getObservations())
				.technology(entity.getTechnology())
				.ventilation(entity.getVentilation())
				.temperature(entity.getTemperature())
				.humidity(entity.getHumidity())
				.co2(entity.getCo2())
				.o2(entity.getO2())
				.nitrogen(entity.getNitrogen())
				.shippingCompany(entity.getShippingCompany())
				.dateEdi(entity.getDateEdi())
				.status(entity.getStatus())
				.build();
		return to;
	}

}
