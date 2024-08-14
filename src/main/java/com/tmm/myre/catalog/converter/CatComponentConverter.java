package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatComponentDto;
import com.tmm.myre.catalog.model.CatComponentModel;
@Component("catComponentConverter")
public class CatComponentConverter implements IConverter<CatComponentModel, CatComponentDto>{

	@Override
	public CatComponentModel convert(CatComponentDto to) throws ConverterException {
		CatComponentModel entity = CatComponentModel.builder()
				.componentId(to.getComponentId())
				.secction(to.getSecction())
				.component(to.getComponent())
				.dc(to.getDc())
				.rf(to.getRf())
				.gd(to.getGd())
				.ch(to.getCh())
				.build();
		return entity;
	}

	@Override
	public CatComponentDto convert(CatComponentModel entity) throws ConverterException {
		CatComponentDto to = CatComponentDto.builder()
				.componentId(entity.getComponentId())
				.secction(entity.getSecction())
				.component(entity.getComponent())
				.dc(entity.getDc())
				.rf(entity.getRf())
				.gd(entity.getGd())
				.ch(entity.getCh())
				.build();
		return to;
	}

}
