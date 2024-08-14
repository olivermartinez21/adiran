package com.tmm.myre.deliveryOrders.service.core;

import java.util.List;

import org.springframework.core.convert.ConversionException;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.deliveryOrders.dto.DeliveryOrderDto;

public interface IDeliveryOrderService {

	List<DeliveryOrderDto> getDataTable(String warehouse) throws ConverterException;

	ResponseManagement createDeliveryOrder(DeliveryOrderDto deliveryOrderDto)throws ConverterException;

	DeliveryOrderDto getInformation(String deliveryOrderId)throws ConverterException;

	ResponseManagement editDeliveryOrder(DeliveryOrderDto deliveryOrderDto)throws ConverterException;

	List<DeliveryOrderDto> getDataTableByAssignmentID(String assigntmentId)throws ConverterException;

	ResponseManagement deleteDeliveryOrder(String deliveryOrderId)throws ConverterException;

	ResponseManagement printDeliveryOrder(String deliveryOrderId)throws ConverterException;


}
