package com.tmm.myre.catalog.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatFinalClientConverter;
import com.tmm.myre.catalog.dto.CatFinalClientDto;
import com.tmm.myre.catalog.model.CatFinalClientModel;
import com.tmm.myre.catalog.repository.ICatFinalClientRepository;
import com.tmm.myre.catalog.service.core.ICatFinalClientService;

@Service("catFinalClientService")
public class CatFinalClientService implements ICatFinalClientService{

	
	
	@Autowired
	private CatFinalClientConverter catFinalClientConverter;
	
	@Autowired
	private ICatFinalClientRepository catFinalClientRepository;

	@Override
	public List<CatFinalClientDto> catFinalClient() throws ConverterException{
		List<CatFinalClientDto> list = new ArrayList<>();
		List<CatFinalClientModel> entities = catFinalClientRepository.findAll();
		entities.sort(Comparator.comparing(CatFinalClientModel::getClientName));
		for (CatFinalClientModel entity : entities) {
			list.add(catFinalClientConverter.convert(entity));
		}
		return list;
	}
	

}
