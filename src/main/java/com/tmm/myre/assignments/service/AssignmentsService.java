package com.tmm.myre.assignments.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.assignments.converter.AssignmentsConverter;
import com.tmm.myre.assignments.converter.AssignmentsFullConverter;
import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.assignments.model.AssignmentsFullModel;
import com.tmm.myre.assignments.model.AssignmentsModel;
import com.tmm.myre.assignments.repository.IAssignmentsFullRepository;
import com.tmm.myre.assignments.repository.IAssignmentsRepository;
import com.tmm.myre.assignments.service.core.IAssignmentsService;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
@Service("assignmentsService")
public class AssignmentsService implements IAssignmentsService {
	
	@Autowired
	private IAssignmentsRepository assignmentsRepository;
	
	@Autowired
	private IAssignmentsFullRepository assignmentsFullRepository;
	
	@Autowired
	private AssignmentsConverter assignmentsConverter;
	
	@Autowired
	private AssignmentsFullConverter assignmentsFullConverter;

	@Override
	public List<AssignmentsDto> getDataTable(String warehouse) throws ConverterException {
		 List<AssignmentsDto> list = new ArrayList<AssignmentsDto>();
		 List<AssignmentsModel> entities = assignmentsRepository.getByLocation(warehouse);
			 for(AssignmentsModel entity : entities) {
					list.add(assignmentsConverter.convert(entity));
				}
		 
		return list;
	}

	@Override
	public ResponseManagement createBooking(AssignmentsDto assignmentsDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			
			assignmentsDto.setAssigntmentId(UuidProvider.getUUID());
			assignmentsDto.setStatus(1);
			assignmentsDto.setQuantityOfUnitsUse("0");
			
			assignmentsRepository.save(assignmentsConverter.convert(assignmentsDto));
			
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}

	@Override
	public List<AssignmentsFullDto> getDataTableFull(String warehouse) throws ConverterException {
		 List<AssignmentsFullDto> list = new ArrayList<AssignmentsFullDto>();
		 List<AssignmentsFullModel> entities = assignmentsFullRepository.getByLocation();
			 for(AssignmentsFullModel entity : entities) {
					list.add(assignmentsFullConverter.convert(entity));
				}
		 
		return list;
	}

	@Override
	public ResponseManagement createBookingFull(AssignmentsFullDto assignmentsFullDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			
			assignmentsFullDto.setAssigntmentFullId(UuidProvider.getUUID());
			assignmentsFullDto.setStatus(1);
			
			assignmentsFullRepository.save(assignmentsFullConverter.convert(assignmentsFullDto));
			
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}

}
