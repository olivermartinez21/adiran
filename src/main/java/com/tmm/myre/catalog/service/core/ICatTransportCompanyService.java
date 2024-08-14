package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;

public interface ICatTransportCompanyService {

	List<CatTransportCompanyDto> catTransport() throws ConverterException ;

}
