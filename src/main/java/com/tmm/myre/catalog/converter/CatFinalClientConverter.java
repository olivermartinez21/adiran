package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatFinalClientDto;
import com.tmm.myre.catalog.dto.CatRepairDto;
import com.tmm.myre.catalog.model.CatFinalClientModel;
import com.tmm.myre.catalog.model.CatRepairModel;
@Component("catFinalClientConverter")
public class CatFinalClientConverter implements IConverter<CatFinalClientModel, CatFinalClientDto> {

	@Override
	public CatFinalClientModel convert(CatFinalClientDto to) throws ConverterException {
		CatFinalClientModel entity = CatFinalClientModel.builder()
				.finalClientId(to.getFinalClientId())
				.clientName(to.getClientName())
				.build();
		return entity;
	}

	@Override
	public CatFinalClientDto convert(CatFinalClientModel entity) throws ConverterException {
		CatFinalClientDto to = CatFinalClientDto.builder()
				.finalClientId(entity.getFinalClientId())
				.clientName(entity.getClientName())
				.build();
		return to;
	}

}
