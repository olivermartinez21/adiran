package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatDamageDto;

public interface ICatDamageService {

	List<CatDamageDto> getDamageInformationByContainerType(Integer containerType) throws ConverterException;

	List<CatDamageDto> findAll() throws ConverterException;

}
