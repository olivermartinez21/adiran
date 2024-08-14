package com.tmm.myre.deliveryOrders.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.deliveryOrders.dto.DeliveryOrderDto;
import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
@Component("deliveryOrderConverter")
public class DeliveryOrderConverter implements IConverter<DeliveryOrderModel, DeliveryOrderDto>{

	@Override
	public DeliveryOrderModel convert(DeliveryOrderDto to) throws ConverterException {
		DeliveryOrderModel entity = DeliveryOrderModel.builder()
		.deliveryOrderId(to.getDeliveryOrderId())
		.booking(to.getBooking())
		.owner(to.getOwner())
		.typeOfService(to.getTypeOfService())
		.remainingUnits(to.getRemainingUnits())
		.billTo(to.getBillTo())
		.carrierCompany(to.getCarrierCompany())
		.operator(to.getOperator())
		.economicNumber(to.getEconomicNumber())
		.workOrder(to.getWorkOrder())
		.quantityOfUnits(to.getQuantityOfUnits())
		.location(to.getLocation())
		.assignmentId(to.getAssignmentId())
		.fileName(to.getFileName())
		.fileType(to.getFileType())
		.fileContent(to.getFileContent())
		.containerType(to.getContainerType())
		.build();
		return entity;
	}

	@Override
	public DeliveryOrderDto convert(DeliveryOrderModel entity) throws ConverterException {
		DeliveryOrderDto to = DeliveryOrderDto.builder()
				.deliveryOrderId(entity.getDeliveryOrderId())
				.booking(entity.getBooking())
				.owner(entity.getOwner())
				.typeOfService(entity.getTypeOfService())
				.remainingUnits(entity.getRemainingUnits())
				.billTo(entity.getBillTo())
				.carrierCompany(entity.getCarrierCompany())
				.operator(entity.getOperator())
				.economicNumber(entity.getEconomicNumber())
				.workOrder(entity.getWorkOrder())
				.quantityOfUnits(entity.getQuantityOfUnits())
				.location(entity.getLocation())
				.assignmentId(entity.getAssignmentId())
				.fileName(entity.getFileName())
				.fileType(entity.getFileType())
				.fileContent(entity.getFileContent())
				.containerType(entity.getContainerType())
				.build();
		return to;
	}

}
