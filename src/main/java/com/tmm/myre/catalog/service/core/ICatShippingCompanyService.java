package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;

public interface ICatShippingCompanyService {

	List<CatShippingCompanyDto> catShipping() throws ConverterException;


	CatShippingCompanyModel getLaborByShippingCompany(Integer shippingCompanyId);
}
