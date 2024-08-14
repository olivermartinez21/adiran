package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatComponentDto;

public interface ICatComponentService {

	List<CatComponentDto> getComponentIformationByContainerTypeAndSection(String containerType, String section) throws ConverterException;

	List<?> getSectionByContainerType(String containerType) throws ConverterException;

	List<CatComponentDto> findAll()  throws ConverterException;


}
