package com.tmm.myre.assignments.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.appointments.controller.AppointmentController;
import com.tmm.myre.assignments.converter.AssignmentConverter;
import com.tmm.myre.assignments.converter.AssignmentsConverter;
import com.tmm.myre.assignments.converter.AssignmentsFullConverter;
import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.model.AssignmentsFullModel;
import com.tmm.myre.assignments.model.AssignmentsModel;
import com.tmm.myre.assignments.model.BookingModel;
import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.assignments.repository.IAssignmentsFullRepository;
import com.tmm.myre.assignments.repository.IAssignmentsRepository;
import com.tmm.myre.assignments.repository.IBookingRepository;
import com.tmm.myre.assignments.service.core.IAssignmentService;
import com.tmm.myre.assignments.service.core.IAssignmentsService;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
import com.tmm.myre.deliveryOrders.repository.IDeliveryOrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("assignmentService")
public class AssignmentService implements IAssignmentService {
	
	@Autowired
	private IAssignmentRepository assignmentRepository;
	
	@Autowired
	private AssignmentConverter assignmentConverter;
	
	@Autowired
	private IBookingRepository bookingRepository;
	
	@Autowired
	private IDeliveryOrderRepository deliveryOrderRepository;
	
	@Autowired
	private IContainerRepository containerRepository;
	
	
	@Override
	public List<AssignmentDto> assignmentInformation(String bookingId) throws ConverterException {
		 List<AssignmentDto> list = new ArrayList<AssignmentDto>();
		 List<AssignmentModel> entities = assignmentRepository.getByBookingId(bookingId);
			 for(AssignmentModel entity : entities) {
					list.add(assignmentConverter.convert(entity));
				}
		 
		return list;
	}
	
	@Override
	public List<AssignmentDto> assignmentInformationEdit(String deliveryId,String warehouse) throws ConverterException {
		List<AssignmentDto> list = new ArrayList<AssignmentDto>();
		 List<AssignmentModel> entities = assignmentRepository.getAssignmentByDeliveryId(deliveryId);
			 for(AssignmentModel entity : entities) {
					list.add(assignmentConverter.convert(entity));
				}
		 
		return list;
	}
	
	@Override
	public ResponseManagement assignation(AssignmentDto assignmentDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().success(false).operation(KeyConstants.INSERT).build();
		try {
			
			AssignmentModel assignment = assignmentRepository.getById(assignmentDto.getId());
			assignmentDto.setBookingId(assignment.getBookingId());
			assignmentDto.setId(assignment.getId());
			assignmentDto.setNo(assignment.getNo());
			assignmentDto.setDeliveryOrderId(assignment.getDeliveryOrderId());
			assignmentDto.setStatus(assignment.getStatus());
			assignment = assignmentConverter.convert(assignmentDto);
			
			BookingModel booking = bookingRepository.getById(assignment.getBookingId());
			
			if(!assignment.getUnitNumber().isEmpty()) {
				int num = containerRepository.existsBd(assignment.getUnitNumber());
				if(num!=0) {
					ContainerModel container = containerRepository.getByUnit(assignment.getUnitNumber());
					container.setStatus(5);
					container.setBokking(booking.getBooking());
					containerRepository.save(container);
					assignmentRepository.save(assignment);
					response.setSuccess(true);
				}else {
					response.setMessage("no se encontro la unidad asignada asegurate que este disponible para asignar");
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
	public ResponseManagement deleteAssignemnt(String assignmentId) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		
		try {
			AssignmentModel assignment = assignmentRepository.getById(assignmentId);
			assignment.setStatus(1);
			assignmentRepository.save(assignment);
			
			BookingModel booking = bookingRepository.getById(assignment.getBookingId());
			booking.setQuantityUnitsUse(String.valueOf(Integer.parseInt(booking.getQuantityUnitsUse())-1));
			bookingRepository.save(booking);
			
			DeliveryOrderModel order = deliveryOrderRepository.getById(assignment.getDeliveryOrderId());
			order.setQuantityOfUnits(String.valueOf(Integer.parseInt(order.getQuantityOfUnits())-1));
			order.setRemainingUnits(String.valueOf(Integer.parseInt(order.getRemainingUnits())+1));
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



