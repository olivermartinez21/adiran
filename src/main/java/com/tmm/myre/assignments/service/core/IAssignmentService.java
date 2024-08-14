package com.tmm.myre.assignments.service.core;


import java.util.List;

import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;

public interface IAssignmentService {

	List<AssignmentDto> assignmentInformation(String bookingId) throws ConverterException;

	ResponseManagement assignation(AssignmentDto assignmentDto) throws ConverterException;

	List<AssignmentDto> assignmentInformationEdit(String deliveryOrderId, String warehouse)  throws ConverterException;

	ResponseManagement deleteAssignemnt(String assignmentId)throws ConverterException;


}
