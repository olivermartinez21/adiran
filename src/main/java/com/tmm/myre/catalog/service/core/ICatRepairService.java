package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatDamageDto;
import com.tmm.myre.catalog.dto.CatRepairDto;

public interface ICatRepairService {

	List<CatRepairDto> getRepairInformation() throws ConverterException;

	


}
