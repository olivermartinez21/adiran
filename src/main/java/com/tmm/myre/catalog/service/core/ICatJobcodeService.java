package com.tmm.myre.catalog.service.core;

import java.util.List;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.containers.dto.ContainerHistoricDto;

public interface ICatJobcodeService {

	List<CatJobcodeDto> catJobcode() throws ConverterException;

	CatJobcodeDto getJobcodeDescription(String jobcodeId) throws ConverterException;

}
