package com.tmm.myre.containers.service.core;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.dto.InspectionShippingDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ResumentInformationDto;
import com.tmm.myre.inspections.dto.InspecctionOutDto;
import com.tmm.myre.inspections.dto.InspectionDto;

public interface IContainerService {

	List<ContainerDto> getContainers(String warehouse) throws ConverterException;

	ResponseManagement saveUpdateContainer(ContainerDto containerDto);

	List<ContainerDto> findAll() throws ConverterException;

	ResponseManagement containerValidation(String containerId,String condition ,String clasification) throws ConverterException;

	ResponseManagement saveEvent(String containerId, Integer userId);

	//ResponseManagement saveContainer(ContainerDto containerDto) throws ConverterException ;

	ResponseManagement savePregate(ContainerDto containerDto) throws ConverterException;

	List<ContainerDto> getIn(String appointmentId, Integer userId,String warehouse)throws ConverterException;

	List<ContainerDto> getOut(String appointmentId, Integer userId,String warehouse)throws ConverterException;

	List<ContainerDto> preGate(String appointmentId, Integer userId,String warehouse)throws ConverterException;

	ContainerDto getSingleData(String containerId) throws ConverterException;

	ResponseManagement saveContainer(ContainerDto containerDto, String warehouse)throws ConverterException;

	List<ResumentInformationDto> getDataResumen(Integer userId)throws ConverterException;

	List<ContainerDto> getContainersbyShippingCompany(Integer userId)throws ConverterException;

	List<ResumentInformationDto> getDataResumenUsers(Integer userId, String location);

	ContainerDto getInformationUnit(String unit)throws ConverterException;

	List<ContainerDto> getContainersStock(String location)throws ConverterException;

	ContainerDto getOne(String containerId) throws ConverterException;

	List<ContainerDto> getContainersAssignments()throws ConverterException;

	ResponseManagement updateContainerInformationOut(ContainerDto containerDto)throws ConverterException;

	ResponseManagement gateOutEvent(ContainerDto containerDto)throws ConverterException;

	List<ContainerDto> getContainersQuote(String warehouse , int status)throws ConverterException;


	ResponseManagement printQuote(String containerId, String warehouse) throws ConverterException;

	List<ContainerDto> getContainersQuoteAll(String warehouse) throws ConverterException;

	ResponseManagement changeStatus(String containerId,String warehouse)throws ConverterException;

	ResponseManagement saveInformationRepair(ContainerDto containerDto)throws ConverterException;

	ResponseManagement saveInspectionContainer(List<MultipartFile> file, ContainerDto containerDto, List<String> index)throws ConverterException;

	ResponseManagement newInspectionContainer(ContainerDto containerDto)throws ConverterException;

	ContainerDto getContainerInformation(String containerId)throws ConverterException;

	List<ContainerDto> getUnitsFilter(String type,String size,String clasification, String warehous)throws ConverterException;

	List<ContainerDto> getUnitsValidationAppointment(String warehouse, Integer userId)throws ConverterException;

	ResponseManagement changeStatusApproved(String containerId,String coments,Integer status)throws ConverterException;

	List<ContainerDto> getAllContainersQuote(String warehouse)throws ConverterException;

	InspectionShippingDto getPreLabor(String inspectionId); 





}
