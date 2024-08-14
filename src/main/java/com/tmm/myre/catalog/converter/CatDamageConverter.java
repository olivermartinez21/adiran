package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatDamageDto;
import com.tmm.myre.catalog.model.CatDamageModel;
@Component("catDamageConverter")
public class CatDamageConverter implements IConverter<CatDamageModel, CatDamageDto>{

	@Override
	public CatDamageModel convert(CatDamageDto to) throws ConverterException {
		CatDamageModel entity = CatDamageModel.builder()
				.damageId(to.getDamageId())
				.iiclCode(to.getIiclCode())
				.damageDescription(to.getDamageDescription())
				.description(to.getDescription())
				.dc(to.getDc())
				.rf(to.getRf())
				.gd(to.getGd())
				.ch(to.getCh())
				.build();
		return entity;
	}

	@Override
	public CatDamageDto convert(CatDamageModel entity) throws ConverterException {
		CatDamageDto to = CatDamageDto.builder()
				.damageId(entity.getDamageId())
				.damageDescription(entity.getDamageDescription())
				.iiclCode(entity.getIiclCode())
				.description(entity.getDescription())
				.dc(entity.getDc())
				.rf(entity.getRf())
				.gd(entity.getGd())
				.ch(entity.getCh())
				.build();
		return to;
	}

}
