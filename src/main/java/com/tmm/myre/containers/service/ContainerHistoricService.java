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


}
