package com.tmm.myre.containers.converter;



import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;

@Component("containerConverter")
public class ContainerConverter implements IConverter<ContainerModel, ContainerDto> {

	@Override
	public ContainerModel convert(ContainerDto to) throws ConverterException {
		ContainerModel entity=ContainerModel.builder()
				.containerId(to.getContainerId())
				.registerDate(to.getRegisterDate())
				.location(to.getLocation())
				.container(to.getContainer())
				.containerType(to.getContainerType())
				.contaierSize(to.getContainerSize())
				.shippingCompany(to.getShippingCompany())
				.appointmentId(to.getAppointmentId())
				.status(to.getStatus())
				.bokking(to.getBooking())
				.bookingQuantity(to.getBookingQuantity())
				.eventType(to.getEventType())
				.dateInspection(to.getDateInspection())
				.status(to.getStatus())
				.condition(to.getCondition())
				.clasification(to.getClasification())
				.coments(to.getComents())
				.definition(to.getDefinition())
				.destination(to.getDestination())
				.chassis(to.getChassis())
				.associateUnit(to.getAssociateUnit())
				.mark(to.getMark())
				.horometro(to.getHorometro())
				.technology(to.getTechnology())
				.generatorType(to.getGeneratorType())
				.setPoint(to.getSetPoint())
				.ventilation(to.getVentilation())
				.modelYear(to.getModelYear())
				.nomenclatura(to.getNomenclatura())
				.conditionPregate(to.getConditionPregate())
				.typeServicePregate(to.getTypeServicePregate())
				.billTo(to.getBillTo())
				.transportId(to.getTransportId())
				.buque(to.getBuque())
				.bl(to.getBl())
				.operatorName(to.getOperatorName())
				.plate(to.getPlate())
				.economicNumber(to.getEconomicNumber())
				.aptTo(to.getAptTo())
				.eir(to.getEir())
				.eirName(to.getEirName())
				.eirOut(to.getEirOut())
				.eirOutName(to.getEirOutName())
				.statusQute(to.getStatusQute())
				.quote(to.getQuote())
				.quoteName(to.getQuoteName())
				.daysStay(to.getDaysStay())
				.startDate(to.getStartDate())
				.finalDate(to.getFinalDate())
				.assignedTo(to.getAssignedTo())
				.comnetsQuote(to.getComnetsQuote())
				.aprovedQuote(to.getAprovedQuote())
				.originPregate(to.getOriginPregate())
				.build();
		
		return entity;
	}

	@Override
	public ContainerDto convert(ContainerModel entity) throws ConverterException {
		ContainerDto to=ContainerDto.builder()
				.containerId(entity.getContainerId())
				.registerDate(entity.getRegisterDate())
				.location(entity.getLocation())
				.container(entity.getContainer())
				.containerType(entity.getContainerType())
				.containerSize(entity.getContaierSize())
				.shippingCompany(entity.getShippingCompany())
				.appointmentId(entity.getAppointmentId()) 
				.status(entity.getStatus())
				.condition(entity.getCondition())
				.clasification(entity.getClasification())
				.booking(entity.getBokking())
				.bookingQuantity(entity.getBookingQuantity())
				.eventType(entity.getEventType())
				.dateInspection(entity.getDateInspection())
				.status(entity.getStatus())
				.coments(entity.getComents())
				.definition(entity.getDefinition())
				.destination(entity.getDestination())
				.chassis(entity.getChassis())
				.associateUnit(entity.getAssociateUnit())
				.mark(entity.getMark())
				.horometro(entity.getHorometro())
				.technology(entity.getTechnology())
				.generatorType(entity.getGeneratorType())
				.setPoint(entity.getSetPoint())
				.ventilation(entity.getVentilation())
				.modelYear(entity.getModelYear())
				.nomenclatura(entity.getNomenclatura())
				.conditionPregate(entity.getConditionPregate())
				.typeServicePregate(entity.getTypeServicePregate())
				.billTo(entity.getBillTo())
				.transportId(entity.getTransportId())
				.buque(entity.getBuque())
				.bl(entity.getBl())
				.operatorName(entity.getOperatorName())
				.plate(entity.getPlate())
				.economicNumber(entity.getEconomicNumber())
				.aptTo(entity.getAptTo())
				.eir(entity.getEir())
				.eirName(entity.getEirName())
				.eirOut(entity.getEirOut())
				.eirOutName(entity.getEirOutName())
				.statusQute(entity.getStatusQute())
				.quote(entity.getQuote())
				.quoteName(entity.getQuoteName())
				.daysStay(entity.getDaysStay())
				.startDate(entity.getStartDate())
				.finalDate(entity.getFinalDate())
				.assignedTo(entity.getAssignedTo())
				.comnetsQuote(entity.getComnetsQuote())
				.aprovedQuote(entity.getAprovedQuote())
				.originPregate(entity.getOriginPregate())
				.build();
		return to;
	}

}
