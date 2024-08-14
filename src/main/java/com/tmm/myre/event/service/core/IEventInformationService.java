package com.tmm.myre.event.service.core;

import java.util.List;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.event.dto.EventInformationDto;

public interface IEventInformationService {

	ResponseManagement saveEventInformation(EventInformationDto eventInformationDto)throws ConverterException;

	List<EventInformationDto> getEvents(String containerId) throws ConverterException;

	ResponseManagement saveNewEvent(ContainerDto containerDto)throws ConverterException;

}
