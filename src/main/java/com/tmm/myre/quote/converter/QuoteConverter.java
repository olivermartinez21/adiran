package com.tmm.myre.quote.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.quote.dto.QuoteDto;
import com.tmm.myre.quote.model.QuoteModel;
@Component("quoteConverter")
public class QuoteConverter implements IConverter<QuoteModel, QuoteDto>{

	@Override
	public QuoteModel convert(QuoteDto to) throws ConverterException {
		QuoteModel entity = QuoteModel.builder()
				.quoteId(to.getQuoteId())
				.workCode(to.getWorkCode())
				.hours(to.getHours())
				.labor(to.getLabor())
				.material(to.getMaterial())
				.tarifa(to.getTarifa())
				.exchange(to.getExchange())
				.inspectionId(to.getInspectionId())
				.build();
		return entity;
	}

	@Override
	public QuoteDto convert(QuoteModel entity) throws ConverterException {
		QuoteDto to = QuoteDto.builder()
				.quoteId(entity.getQuoteId())
				.workCode(entity.getWorkCode())
				.hours(entity.getHours())
				.labor(entity.getLabor())
				.material(entity.getMaterial())
				.tarifa(entity.getTarifa())
				.exchange(entity.getExchange())
				.inspectionId(entity.getInspectionId())
				.build();
		return to;
	}

}
