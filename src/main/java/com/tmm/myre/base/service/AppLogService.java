/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.converter.AppLogConverter;
import com.tmm.myre.base.dto.AppLogDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.AppLog;
import com.tmm.myre.base.repository.IAppLogRepository;
import com.tmm.myre.base.service.core.IAppLogService;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.base.utils.UuidProvider;





/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */
@Service("appLogService")
public class AppLogService implements IAppLogService{

	@Autowired
	private IAppLogRepository appLogRepository;
	
	@Autowired
	private AppLogConverter appLogConverter;
	
	@Override
	public void registerLog(Integer idUser, String operation, String logText) {
		AppLog entity = AppLog.builder()
				.id(UuidProvider.getUUID())
				.userId(idUser)
				.operation(operation)
				.managementDate(DateManagement.todayDate())
				.log(logText)
				.build();
		appLogRepository.save(entity);
	}

	@Override
	public List<AppLogDto> findAll() throws ConverterException {
		List<AppLogDto> list = new ArrayList<AppLogDto>();
		List<AppLog> entities = appLogRepository.findAll();
		for(AppLog entity : entities) {
			list.add(appLogConverter.convert(entity));
		}
		return list;
	}

}
