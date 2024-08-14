package com.tmm.myre.catalog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatNomenclaturaConverter;
import com.tmm.myre.catalog.dto.CatNomenclaturaDto;
import com.tmm.myre.catalog.model.CatNomenclaturaModel;
import com.tmm.myre.catalog.repository.ICatNomenclaturaRepository;
import com.tmm.myre.catalog.service.core.ICatNomenclaturaService;
import com.tmm.myre.containers.controller.PregateController;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("catNomenclaturaService")
public class CatNomenclaturaService implements ICatNomenclaturaService{
	
	
	@Autowired
	private ICatNomenclaturaRepository catNomenclaturaRepository;
	
	@Autowired
	private CatNomenclaturaConverter catNomenclaturaConverter;
	

	@Override
	public List<CatNomenclaturaDto> getNomenclaturaByContainerType(String containerType,String size) throws ConverterException {
		List<CatNomenclaturaDto> list = new ArrayList<CatNomenclaturaDto>();
		List<CatNomenclaturaModel> entities ;
		
		if(containerType.equals("GS")) {
		entities = catNomenclaturaRepository.findGS(containerType);
		}else {
		entities = catNomenclaturaRepository.findBYTransportType(containerType,size);
		}
		
		for(CatNomenclaturaModel entity : entities) {
			list.add(catNomenclaturaConverter.convert(entity));
		}
		return list;
	}


}
