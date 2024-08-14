package com.tmm.myre.inspections.service.core;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.inspections.dto.InspectionDto;

public interface IInspectionsService {

	List<InspectionDto> getInspections(String appointmentId) throws ConverterException;

	List<InspectionDto> getDataTable(String containerId , Integer userId)  throws ConverterException ;

	ResponseManagement inspectionValidation(String inspectionId) throws ConverterException;

	List<InspectionDto> getInspectionsByContainer(String containerId)throws ConverterException;

	ResponseManagement deleteInspecction(String inspecctionId)throws ConverterException;

	ResponseManagement saveNewInspection(InspectionDto inspectionDto)throws ConverterException;

	ResponseManagement editInspection(InspectionDto inspecctionDto)throws ConverterException;

	ResponseManagement addImages(List<MultipartFile> file, String inspectionUpdate)throws ConverterException;

	ResponseManagement addDamage(List<MultipartFile> file, String inspection)throws ConverterException;

	ResponseManagement repairDamage(String inspectionId)throws ConverterException;

	int validationInspection(String containerId, int status)throws ConverterException;

	ResponseManagement changeStatus(ContainerDto contianerDto)throws ConverterException;

	ResponseManagement deleteInspection(String inspectionId) throws ConverterException;
}
