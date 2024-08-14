package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatRepairDto;
import com.tmm.myre.catalog.model.CatRepairModel;
@Component("catRepairConverter")
public class CatRepairConverter implements IConverter<CatRepairModel, CatRepairDto> {

	@Override
	public CatRepairModel convert(CatRepairDto to) throws ConverterException {
		CatRepairModel entity = CatRepairModel.builder()
				.repairId(to.getRepairId())
				.repairCode(to.getRepairCode())
				.repairDescription(to.getRepairDescription())
				.englishDescription(to.getEnglishDescription())
				.build();
		return entity;
	}

	@Override
	public CatRepairDto convert(CatRepairModel entity) throws ConverterException {
		CatRepairDto to = CatRepairDto.builder()
				.repairId(entity.getRepairId())
				.repairCode(entity.getRepairCode())
				.repairDescription(entity.getRepairDescription())
				.englishDescription(entity.getEnglishDescription())
				.build();
		return to;
	}

}
