package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;
import com.tmm.myre.catalog.model.CatTransportCompanyModel;
@Component("catTransportCompanyConverter")
public class CatTransportCompanyConverter  implements IConverter<CatTransportCompanyModel, CatTransportCompanyDto>{

	@Override
	public CatTransportCompanyModel convert(CatTransportCompanyDto to) throws ConverterException {
		CatTransportCompanyModel entity = CatTransportCompanyModel.builder()
				.transportId(to.getTransportId())
				.transportCode(to.getTransportCode())
				.transportName(to.getTransportName())
				.build();
		return entity;
	}

	@Override
	public CatTransportCompanyDto convert(CatTransportCompanyModel entity) throws ConverterException {
		CatTransportCompanyDto to = CatTransportCompanyDto.builder()
				.transportId(entity.getTransportId())
				.transportCode(entity.getTransportCode())
				.transportName(entity.getTransportName())
				.build();
		return to;
	}

}
