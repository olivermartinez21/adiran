package com.tmm.myre.inspections.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.inspections.dto.InspecctionOutDto;
import com.tmm.myre.inspections.model.InspecctionOutModel;

@Component("inspectionOutConverter")
public class InspectionOutConverter implements IConverter<InspecctionOutModel, InspecctionOutDto> {

	@Override
	public InspecctionOutModel convert(InspecctionOutDto to) throws ConverterException {
		InspecctionOutModel entity = InspecctionOutModel.builder()
				.inspectionId(to.getInspectionId())
				.photo(to.getPhoto())
				.containerId(to.getContainerId())
				.inspectionDateOut(to.getInspectionDateOut())
				.build();
		return entity;
	}

	@Override
	public InspecctionOutDto convert(InspecctionOutModel entity) throws ConverterException {
		InspecctionOutDto to = InspecctionOutDto.builder()
				.inspectionId(entity.getInspectionId())
				.photo(entity.getPhoto())
				.containerId(entity.getContainerId())
				.inspectionDateOut(entity.getInspectionDateOut())
				.build();
		return to;
	}


}
