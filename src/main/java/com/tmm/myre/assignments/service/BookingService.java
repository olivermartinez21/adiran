package com.tmm.myre.assignments.service;

import java.util.ArrayList;

import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmm.myre.assignments.converter.AssignmentConverter;
import com.tmm.myre.assignments.converter.BookingConverter;
import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.dto.BookingDto;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.model.BookingModel;
import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.assignments.repository.IBookingRepository;
import com.tmm.myre.assignments.service.core.IAssignmentService;
import com.tmm.myre.assignments.service.core.IBookingService;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.containers.service.core.IContainerService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("bookingService")
public class BookingService implements IBookingService {

	@Autowired
	private IBookingRepository bookingRepository;
	
	@Autowired
	private BookingConverter bookingConverter;
	
	@Autowired
	private IAssignmentRepository assignmentRepository;
	
	@Autowired
	private AssignmentConverter assignmentConverter;
	
	@Autowired
	private IContainerRepository containerRepository;
	
	@Override
	public List<BookingDto> getDataTable(String warehouse) throws ConverterException {
		List<BookingDto> list = new ArrayList<BookingDto>();
		 List<BookingModel> entities = bookingRepository.getByLocation(warehouse);
			 for(BookingModel entity : entities) {
					list.add(bookingConverter.convert(entity));
				}
		 
		return list;
	}

	@Override
	public ResponseManagement createBooking(BookingDto bookingDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			
			String bookingValidation = bookingRepository.bookingValidation(bookingDto.getBooking());
			
			if (bookingValidation == "0") {
				ObjectMapper inspectionMapper = new ObjectMapper();
				List<AssignmentDto> assignments = inspectionMapper.readValue(bookingDto.getAsignmentList(), new TypeReference<List<AssignmentDto>>() { });
				bookingDto.setBookingId(UuidProvider.getUUID());
				bookingDto.setStatus("1");
				bookingDto.setType(assignments.get(0).getType());
				bookingDto.setSize(assignments.get(0).getSize());
				bookingDto.setQuality(assignments.get(0).getQuality());
				bookingDto.setQuantityUnitsUse("0");
				bookingDto.setCreationDate(DateManagement.todayDate());
				
				//bookingDto.setQuantityOfUnitsUse("0");
				
				 List<AssignmentModel> list = new ArrayList<AssignmentModel>();
				 List<String> containersEdit = new ArrayList<String>();
				 for(AssignmentDto entity : assignments) {
					 if(entity.getUnitNumber().length()>0) {
							if(containerRepository.existsByContainer(entity.getUnitNumber())>0) {
								containersEdit.add(entity.getUnitNumber());
								int validation=0;
								for(String unit : containersEdit) {
									if(Collections.frequency(containersEdit, unit)>1) {
										response.setMessage("Se repite el siguiente Numero de unidad "+entity.getUnitNumber());
										response.setSuccess(false);
										validation++;
										break;
									}
								}
								if(validation==0) {
									 entity.setBookingId(bookingDto.getBookingId());
									 entity.setId(UuidProvider.getUUID());
									 entity.setStatus(1);
									 list.add(assignmentConverter.convert(entity)); 
								}
								
							}else {
								response.setMessage("Asegurese de Elejir la unidad del inventario");
								response.setSuccess(false);
								break;
							}
							
					 }else {
						 entity.setBookingId(bookingDto.getBookingId());
						 entity.setId(UuidProvider.getUUID());
						 entity.setStatus(1);
						 list.add(assignmentConverter.convert(entity)); 
					 }
						 
					 
					
					}
				 
				 if(response.getMessage()==null) {
					 

					for(String unit : containersEdit) {
						ContainerModel unitStatus =  containerRepository.getByUnit(unit);
						unitStatus.setStatus(5);
						containerRepository.save(unitStatus);
					 }
					 
					 bookingRepository.save(bookingConverter.convert(bookingDto));
					 assignmentRepository.saveAll(list);
					 response.setSuccess(true);
				 }
				
			} else {
			
				response.setMessage("El Booking ya fue registrado");
				response.setSuccess(false);
			
		  }
			 
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}

}
