package com.tmm.myre.containers.service.core;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ContainerHistoricDto;
import com.tmm.myre.containers.model.ContainerHistoricModel;

import java.util.List;

public interface IContainerHistoricService {

	
	ContainerHistoricDto searchContainer(String container) throws ConverterException;

    List<ContainerHistoricModel> getHistoricUnits() throws ConverterException;

    ContainerHistoricDto getOne(String containerId) throws ConverterException;
}
