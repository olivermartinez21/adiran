package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;

public interface ICatShippingCompanyService {

	List<CatShippingCompanyDto> catShipping() throws ConverterException;


}
