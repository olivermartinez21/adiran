package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
@Component("catShippingCompanyConverter")
public class CatShippingCompanyConverter  implements IConverter<CatShippingCompanyModel, CatShippingCompanyDto>{

	@Override
	public CatShippingCompanyModel convert(CatShippingCompanyDto to) throws ConverterException {
		CatShippingCompanyModel entity = CatShippingCompanyModel.builder()
				.shippingCompanyId(to.getShippingCompanyId())
				.description(to.getDescription())
				.labor(to.getDescription())
				.build();
		return entity;
	}

	@Override
	public CatShippingCompanyDto convert(CatShippingCompanyModel entity) throws ConverterException {
		CatShippingCompanyDto to = CatShippingCompanyDto.builder()
				.shippingCompanyId(entity.getShippingCompanyId())
				.description(entity.getDescription())
				.labor(entity.getDescription())
				.build();
		return to;
	}

}
