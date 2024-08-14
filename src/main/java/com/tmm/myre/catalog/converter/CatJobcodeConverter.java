package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.catalog.model.CatJobcodeModel;

@Component("catJobcodeConverter")
public class CatJobcodeConverter implements IConverter<CatJobcodeModel, CatJobcodeDto>{

	@Override
	public CatJobcodeModel convert(CatJobcodeDto to) throws ConverterException {
		CatJobcodeModel entity = CatJobcodeModel.builder()
				.jobcodeId(to.getJobcodeId())
				.jobcodeRepair(to.getJobcodeRepair())
				.jobcodeDescription(to.getJobcodeDescription())
				.jobcodeMaterial(to.getJobcodeMaterial())
				.jobcodeHh(to.getJobcodeHh())
				.jobcodeExchange(to.getJobcodeExchange())
				.build();
		return entity;
	}

	@Override
	public CatJobcodeDto convert(CatJobcodeModel entity) throws ConverterException {
		CatJobcodeDto to = CatJobcodeDto.builder()
				.jobcodeId(entity.getJobcodeId())
				.jobcodeRepair(entity.getJobcodeRepair())
				.jobcodeDescription(entity.getJobcodeDescription())
				.jobcodeMaterial(entity.getJobcodeMaterial())
				.jobcodeHh(entity.getJobcodeHh())
				.jobcodeExchange(entity.getJobcodeExchange())
				.build();
		return to;
	}

}
