package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatCustomerDto;
import com.tmm.myre.catalog.model.CatCustomerModel;
@Component("catCustomerConverter")
public class CatCustomerConverter  implements IConverter<CatCustomerModel, CatCustomerDto>{

	@Override
	public CatCustomerModel convert(CatCustomerDto to) throws ConverterException {
		CatCustomerModel entity = CatCustomerModel.builder()
				.customerId(to.getCustomerId())
				.customerCode(to.getCustomerCode())
				.customerName(to.getCustomerName())
				.customerType(to.getCustomerType())
				.customerAddress(to.getCustomerAddress())
				.build();
		return entity;
	}

	@Override
	public CatCustomerDto convert(CatCustomerModel entity) throws ConverterException {
		CatCustomerDto to = CatCustomerDto.builder()
				.customerId(entity.getCustomerId())
				.customerCode(entity.getCustomerCode())
				.customerName(entity.getCustomerName())
				.customerType(entity.getCustomerType())
				.customerAddress(entity.getCustomerAddress())
				.build();
		return to;
	}

}
