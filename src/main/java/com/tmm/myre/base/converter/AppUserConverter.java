/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.dto.AppUserDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.AppUser;
import com.tmm.myre.base.utils.EncrytedPasswordUtils;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Component("appUserConverter")
public class AppUserConverter implements IConverter<AppUser, AppUserDto>{

	@Override
	public AppUser convert(AppUserDto to) throws ConverterException {
		AppUser entity = AppUser.builder()
				.userId(to.getUserId())
				.userName(to.getUserName())
				.encrytedPassword(EncrytedPasswordUtils.encrytePassword(to.getPassword()))
				.enabled(to.getEnabled())
				.build();
		return entity;
	}

	@Override
	public AppUserDto convert(AppUser entity) throws ConverterException {
		AppUserDto to = AppUserDto.builder()
		.userId(entity.getUserId())
		.userName(entity.getUserName())
		.enabled(entity.getEnabled())
		.build();
		return to;
	}

}