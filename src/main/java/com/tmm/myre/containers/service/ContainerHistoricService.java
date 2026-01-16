package com.tmm.myre.containers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.converter.ContainerHistoricConverter;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ContainerHistoricDto;
import com.tmm.myre.containers.model.ContainerHistoricModel;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerHistoricRepository;
import com.tmm.myre.containers.service.core.IContainerHistoricService;


import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service("containerHistoricService")
public class ContainerHistoricService implements IContainerHistoricService{
	
	@Autowired
	IContainerHistoricRepository containerHistoricRepository;
	
	@Autowired
	ContainerHistoricConverter containerHistoricConverter;
	
	@Override
	public ContainerHistoricDto searchContainer(String container) throws ConverterException {
		ContainerHistoricModel container1 = containerHistoricRepository.getlastRegister(container);
		return containerHistoricConverter.convert(container1);
	}

	@Override
	public List<ContainerHistoricModel> getHistoricUnits() throws ConverterException{
		List<ContainerHistoricModel> containerHistoric = containerHistoricRepository.findWhereQuoteNotEmpty();
		return containerHistoric;
	}

	@Override
	public ContainerHistoricDto getOne(String containerId) throws ConverterException {
		ContainerHistoricModel container = containerHistoricRepository.findByContainerId(containerId);
		return containerHistoricConverter.convert(container);
	}


}
