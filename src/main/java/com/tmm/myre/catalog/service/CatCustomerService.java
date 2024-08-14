package com.tmm.myre.catalog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatCustomerConverter;
import com.tmm.myre.catalog.dto.CatCustomerDto;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.catalog.model.CatCustomerModel;
import com.tmm.myre.catalog.model.CatJobcodeModel;
import com.tmm.myre.catalog.repository.ICatCustomerReposirtory;
import com.tmm.myre.catalog.service.core.ICatCustomerService;
@Service("catCustomerService")
public class CatCustomerService  implements ICatCustomerService {

	
	@Autowired
	private ICatCustomerReposirtory catCustomerReposirtory;
	
	@Autowired
	private CatCustomerConverter catCustomerConverter;
	
	@Override
	public List<CatCustomerDto> catShipping() throws ConverterException {
		
		List<CatCustomerDto> list = new ArrayList<CatCustomerDto>();
		List<CatCustomerModel> entities = catCustomerReposirtory.findAllShipping();
		for(CatCustomerModel entity : entities) {
			list.add(catCustomerConverter.convert(entity));
		}
		return list;
	}

	@Override
	public List<CatCustomerDto> catClients() throws ConverterException {
		List<CatCustomerDto> list = new ArrayList<CatCustomerDto>();
		List<CatCustomerModel> entities = catCustomerReposirtory.findAll();
		for(CatCustomerModel entity : entities) {
			list.add(catCustomerConverter.convert(entity));
		}
		return list;
	}
	
	@Override
	public CatCustomerDto getAddressClient(String customerId) throws ConverterException {
		CatCustomerModel info = catCustomerReposirtory.addressClient(customerId);
		return catCustomerConverter.convert(info);
		
	}

}
