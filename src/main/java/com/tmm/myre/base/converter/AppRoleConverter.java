/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.dto.AppRoleDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.AppRole;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Component("appRoleConverter")
public class AppRoleConverter implements IConverter<AppRole, AppRoleDto>{

	@Override
	public AppRole convert(AppRoleDto to) throws ConverterException {
		AppRole entity = AppRole.builder().roleId(to.getRoleId()).roleName(to.getRoleName()).build();
		return entity;
	}

	@Override
	public AppRoleDto convert(AppRole entity) throws ConverterException {
		AppRoleDto to = AppRoleDto.builder().roleId(entity.getRoleId()).roleName(entity.getRoleName()).build();
		return to;
	}

}