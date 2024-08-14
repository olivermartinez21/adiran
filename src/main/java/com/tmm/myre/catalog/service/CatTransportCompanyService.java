package com.tmm.myre.catalog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatTransportCompanyConverter;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;
import com.tmm.myre.catalog.model.CatTransportCompanyModel;
import com.tmm.myre.catalog.repository.ICatTransportCompanyRepository;
import com.tmm.myre.catalog.service.core.ICatTransportCompanyService;
@Service("catTransportCompanyService")
public class CatTransportCompanyService implements  ICatTransportCompanyService{

	@Autowired
	private ICatTransportCompanyRepository catTransportCompanyRepository;
	
	@Autowired
	private CatTransportCompanyConverter catTransportCompanyConverter;
	
	
	@Override
	public List<CatTransportCompanyDto> catTransport() throws ConverterException {
			List<CatTransportCompanyDto> list = new ArrayList<CatTransportCompanyDto>();
			List<CatTransportCompanyModel> entities = catTransportCompanyRepository.findAll();
			for(CatTransportCompanyModel entity : entities) {
				list.add(catTransportCompanyConverter.convert(entity));
			}
			return list;
	}

}
