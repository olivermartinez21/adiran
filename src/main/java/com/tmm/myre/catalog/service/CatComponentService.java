package com.tmm.myre.catalog.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatComponentConverter;
import com.tmm.myre.catalog.dto.CatComponentDto;
import com.tmm.myre.catalog.model.CatComponentModel;
import com.tmm.myre.catalog.repository.ICatComponentRepository;
import com.tmm.myre.catalog.service.core.ICatComponentService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("catComponentService")
public class CatComponentService implements ICatComponentService{
	

	@Autowired
	private ICatComponentRepository catComponentRepository;
	
	@Autowired
	private CatComponentConverter catComponentConverter;

	@Override
	public List<CatComponentDto> getComponentIformationByContainerTypeAndSection(String containerType, String section) throws ConverterException {
		if(containerType.equals("HC")) {
			containerType="DC";
		}
		List<CatComponentDto> list = new ArrayList<CatComponentDto>();
		List<CatComponentModel> entities = catComponentRepository.findByContainerTypeAndSecction(section,containerType);
		for(CatComponentModel entity : entities) {
			list.add(catComponentConverter.convert(entity));
		}
		return list;
	}

	@Override
	public List<?> getSectionByContainerType(String containerType) throws ConverterException {
		if(containerType.equals("HC")) {
			containerType="DC";
		}
		List<?> list = catComponentRepository.findByContainerType(containerType);
		
		return list;
	}

	@Override
	public List<CatComponentDto> findAll() throws ConverterException {
		List<CatComponentDto> list = new ArrayList<CatComponentDto>();
		List<CatComponentModel> entities = catComponentRepository.findAll();
		for(CatComponentModel entity : entities) {
			list.add(catComponentConverter.convert(entity));
		}
		return list;
	}

	

}
