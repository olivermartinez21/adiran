package com.tmm.myre.deliveryOrders.service;

import java.util.ArrayList;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.model.AssignmentsModel;
import com.tmm.myre.assignments.model.BookingModel;
import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.assignments.repository.IAssignmentsRepository;
import com.tmm.myre.assignments.repository.IBookingRepository;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.deliveryOrders.converter.DeliveryOrderConverter;
import com.tmm.myre.deliveryOrders.dto.DeliveryOrderDto;
import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
import com.tmm.myre.deliveryOrders.repository.IDeliveryOrderRepository;
import com.tmm.myre.deliveryOrders.service.core.IDeliveryOrderService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service("deliveryOrderService")
public class DeliveryOrderService implements IDeliveryOrderService {

	
	@Autowired
	private DeliveryOrderConverter deliveryOrderConverter;
	
	@Autowired
	private IDeliveryOrderRepository deliveryOrderRepository;
	
	@Autowired
	private IAssignmentRepository assignmentRepository;
	
	@Autowired
	private IBookingRepository bookingRepository;
	
	@Autowired
	private IPdfGenerationService pdfGenerationService;
	
	
	
	@Override
	public List<DeliveryOrderDto> getDataTable(String warehouse) throws ConverterException  {
		 List<DeliveryOrderDto> list = new ArrayList<DeliveryOrderDto>();
		 List<DeliveryOrderModel> entities = deliveryOrderRepository.getByLocation(warehouse);
			 for(DeliveryOrderModel entity : entities) {
					list.add(deliveryOrderConverter.convert(entity));
				}
		 
		return list;
	}


	
	@Override
	public ResponseManagement createDeliveryOrder(DeliveryOrderDto deliveryOrderDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			BookingModel assignment = bookingRepository.getById(deliveryOrderDto.getAssignmentId());
			int use = Integer.parseInt(deliveryOrderDto.getQuantityOfUnits())+Integer.parseInt(assignment.getQuantityUnitsUse());
			assignment.setQuantityUnitsUse(String.valueOf(use));
			if(use>Integer.parseInt(assignment.getQuantityUnits())) {
				response.setMessage("El BOOKING ESTA COMPLETO");	
				response.setSuccess(false);	
			}else {
				
				if(deliveryOrderDto.getAssignmentList() != null) {
					
					int remain = Integer.parseInt(assignment.getQuantityUnits())-use;
					deliveryOrderDto.setBooking(assignment.getBooking());
					//deliveryOrderDto.setOwner(assignment.getShippingCompany());
					deliveryOrderDto.setRemainingUnits(String.valueOf(remain));
					deliveryOrderDto.setDeliveryOrderId(UuidProvider.getUUID());
					deliveryOrderRepository.save(deliveryOrderConverter.convert(deliveryOrderDto));
					
					List<DeliveryOrderModel> actualizar = deliveryOrderRepository.getOrderByAssignmentId(deliveryOrderDto.getAssignmentId());
					
					for(DeliveryOrderModel entity : actualizar) {
						entity.setRemainingUnits(String.valueOf(remain));
						deliveryOrderRepository.save(entity);
						
					}
					
					for(String assignmentSelect : deliveryOrderDto.getAssignmentList()) {
						AssignmentModel assignmet = assignmentRepository.getById(assignmentSelect);
						assignmet.setStatus(2);
						assignmet.setDeliveryOrderId(deliveryOrderDto.getDeliveryOrderId());
						assignmentRepository.save(assignmet);
					}
					
					response.setSuccess(true);	
				}else {
					response.setMessage("Por favor Selecciona las unidades para esta Orden");
					response.setSuccess(false);
				}
				
				
			}
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}



	@Override
	public DeliveryOrderDto getInformation(String deliveryOrderId) throws ConverterException {
		DeliveryOrderDto information = deliveryOrderConverter.convert(deliveryOrderRepository.getById(deliveryOrderId));
		return information;
	}



	@Override
	public ResponseManagement editDeliveryOrder(DeliveryOrderDto deliveryOrderDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			
			DeliveryOrderModel order = deliveryOrderRepository.getById(deliveryOrderDto.getDeliveryOrderId());
			deliveryOrderDto.setDeliveryOrderId(order.getDeliveryOrderId());
			deliveryOrderDto.setAssignmentId(order.getAssignmentId());
			
			int use = Integer.parseInt(deliveryOrderDto.getQuantityOfUnits())-Integer.parseInt(order.getQuantityOfUnits());
			
			if(use>0) {
				
				if(use<=Integer.parseInt(order.getRemainingUnits())) {
					use = Integer.parseInt(order.getRemainingUnits())-use;
					deliveryOrderDto.setRemainingUnits(String.valueOf(use));
					order = deliveryOrderConverter.convert(deliveryOrderDto);
					deliveryOrderRepository.save(order);
					response.setSuccess(true);
				}else {
					response.setMessage("el booking no tiene suficientes unidades para asginar");	
					response.setSuccess(false);	
				}
				
			}else {
				
				order = deliveryOrderConverter.convert(deliveryOrderDto);
				deliveryOrderRepository.save(order);
				response.setSuccess(true);
			}
			
			
			
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		
		return response;
	}



	@Override
	public List<DeliveryOrderDto> getDataTableByAssignmentID(String bookingId) throws ConverterException {
		List<DeliveryOrderDto> list = new ArrayList<DeliveryOrderDto>();
		 List<DeliveryOrderModel> entities = deliveryOrderRepository.getOrderByAssignmentId(bookingId);
			 for(DeliveryOrderModel entity : entities) {
					list.add(deliveryOrderConverter.convert(entity));
				}
		 
		return list;
	}



	@Override
	public ResponseManagement deleteDeliveryOrder(String deliveryOrderId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		
		try {
			List<AssignmentModel> assignments = assignmentRepository.getAssignmentByDeliveryId(deliveryOrderId);
			
			for(AssignmentModel assignment :assignments ) {
				assignment.setStatus(1);
				assignment.setDeliveryOrderId("");
				assignmentRepository.save(assignment);
			}
			
			
			
			deliveryOrderRepository.deleteById(deliveryOrderId);
			
			
			
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}
	
	@Override
	public ResponseManagement printDeliveryOrder(String deliveryOrderId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		
		try {
			
			int ordernum = deliveryOrderRepository.getcountpdf();
			DeliveryOrderModel order = deliveryOrderRepository.getById(deliveryOrderId);
			order.setFileName("AGS-ODE-0"+(ordernum+1));
			order.setFileType(order.getFileName());
			deliveryOrderRepository.save(order);
			order.setFileContent(pdfGenerationService.pdfOrder(order));
			response.setPdf(deliveryOrderId);
			deliveryOrderRepository.save(order);
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}


}
