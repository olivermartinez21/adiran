package com.tmm.myre.catalog.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatShippingCompanyConverter;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
@Service("catShippingCompanyService")
public class CatShippingCompanyService  implements ICatShippingCompanyService {

	

	@Autowired
	private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;
	
	@Autowired
	private CatShippingCompanyConverter catShippingCompanyConverter;
	
	
	@Override
	public List<CatShippingCompanyDto> catShipping() throws ConverterException {
			List<CatShippingCompanyDto> list = new ArrayList<CatShippingCompanyDto>();
			List<CatShippingCompanyModel> entities = catShippingCompanyReposirtory.findAll();
			for(CatShippingCompanyModel entity : entities) {
				list.add(catShippingCompanyConverter.convert(entity));
			}
			return list;
	}


}
