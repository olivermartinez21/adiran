package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatNomenclaturaDto;
import com.tmm.myre.catalog.model.CatNomenclaturaModel;
@Component("catNomenclaturaConverter")
public class CatNomenclaturaConverter implements IConverter<CatNomenclaturaModel, CatNomenclaturaDto> {

	@Override
	public CatNomenclaturaModel convert(CatNomenclaturaDto to) throws ConverterException {
		CatNomenclaturaModel entity = CatNomenclaturaModel.builder()
				.nomenclaturaId(to.getNomenclaturaId())
				.containerType(to.getContainerType())
				.size(to.getSize())
				.nomenclatura(to.getNomenclatura())
				.transman(to.getTransman())
				.build();
		return entity;
	}

	@Override
	public CatNomenclaturaDto convert(CatNomenclaturaModel entity) throws ConverterException {
		CatNomenclaturaDto to = CatNomenclaturaDto.builder()
				.nomenclaturaId(entity.getNomenclaturaId())
				.containerType(entity.getContainerType())
				.size(entity.getSize())
				.nomenclatura(entity.getNomenclatura())
				.transman(entity.getTransman())
				.build();
		return to;
	}

}
