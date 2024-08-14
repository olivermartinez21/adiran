package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatFinalClientDto;

public interface ICatFinalClientService {

	List<CatFinalClientDto> catFinalClient() throws ConverterException;



}
