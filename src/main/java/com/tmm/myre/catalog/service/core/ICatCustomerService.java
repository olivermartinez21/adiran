package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatCustomerDto;

public interface ICatCustomerService {

	List<CatCustomerDto> catShipping() throws ConverterException;

	List<CatCustomerDto> catClients()throws ConverterException;

	CatCustomerDto getAddressClient(String customerId) throws ConverterException;


}
