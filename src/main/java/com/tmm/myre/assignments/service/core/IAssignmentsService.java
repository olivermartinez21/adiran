package com.tmm.myre.assignments.service.core;

import java.util.List;

import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;

public interface IAssignmentsService {

	List<AssignmentsDto> getDataTable(String warehouse) throws ConverterException ;

	ResponseManagement createBooking(AssignmentsDto assignmentsDto) throws ConverterException;

	List<AssignmentsFullDto> getDataTableFull(String warehouse) throws ConverterException;

	ResponseManagement createBookingFull(AssignmentsFullDto assignmentsFullDto)throws ConverterException;

}
