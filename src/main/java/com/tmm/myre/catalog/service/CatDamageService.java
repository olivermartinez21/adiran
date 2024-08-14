package com.tmm.myre.catalog.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatDamageConverter;
import com.tmm.myre.catalog.dto.CatDamageDto;
import com.tmm.myre.catalog.model.CatDamageModel;
import com.tmm.myre.catalog.repository.ICatDamageRepository;
import com.tmm.myre.catalog.service.core.ICatDamageService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("catDamageService")
public class CatDamageService  implements ICatDamageService {

	
	@Autowired
	private ICatDamageRepository catDamageRepository;
	
	@Autowired
	private CatDamageConverter catDamageConverter;
	
	@Override
	public List<CatDamageDto> getDamageInformationByContainerType(Integer containerType) throws ConverterException {
		List<CatDamageDto> list = new ArrayList<CatDamageDto>();
		List<CatDamageModel> entities = null;
		if(containerType==3||containerType==7) {
			entities = catDamageRepository.findAllbycontainerDC();
		}
		if(containerType==6) {
			entities = catDamageRepository.findAllbycontainerRF();
		}
		if(containerType==4) {
			entities = catDamageRepository.findAllbycontainerGD();
				}
		if(containerType==1) {
			entities = catDamageRepository.findAllbycontainerCH();
		}
		
		for(CatDamageModel entity : entities) {
			list.add(catDamageConverter.convert(entity));
		}
		return list;
	}

	@Override
	public List<CatDamageDto> findAll() throws ConverterException {
		List<CatDamageDto> list = new ArrayList<CatDamageDto>();
		List<CatDamageModel> entities = catDamageRepository.findAll();;
		for(CatDamageModel entity : entities) {
			list.add(catDamageConverter.convert(entity));
		}
		return list;
	}
}
