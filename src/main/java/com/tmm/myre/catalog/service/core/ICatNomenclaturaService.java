package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatNomenclaturaDto;

public interface ICatNomenclaturaService {

	List<CatNomenclaturaDto> getNomenclaturaByContainerType(String containerType,String size) throws ConverterException;




}
