/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.dto.AppLogDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.AppLog;




/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 7 sep. 2020
 */
@Component
public class AppLogConverter implements IConverter<AppLog, AppLogDto>{

	@Override
	public AppLog convert(AppLogDto to) throws ConverterException {
		AppLog entity = AppLog.builder()
				.id(to.getId())
				.userId(to.getUserId())
				.managementDate(to.getManagementDate())
				.operation(to.getOperation())
				.log(to.getLog())
				.build();
		return entity;
	}

	@Override
	public AppLogDto convert(AppLog entity) throws ConverterException {
		AppLogDto to = AppLogDto.builder()
				.id(entity.getId())
				.userId(entity.getUserId())
				.managementDate(entity.getManagementDate())
				.operation(entity.getOperation())
				.log(entity.getLog())
				.build();
		return to;
	}

}
