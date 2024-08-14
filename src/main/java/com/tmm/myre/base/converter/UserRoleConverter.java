/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.dto.UserRoleDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.UserRole;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Component("userRoleConverter")
public class UserRoleConverter implements IConverter<UserRole, UserRoleDto>{

	@Override
	public UserRole convert(UserRoleDto to) throws ConverterException {
		UserRole entity = UserRole.builder().id(to.getId()).userId(to.getUserId()).roleId(to.getRoleId()).build();
		return entity;
	}

	@Override
	public UserRoleDto convert(UserRole entity) throws ConverterException {
		UserRoleDto to = UserRoleDto.builder().id(entity.getId()).userId(entity.getUserId()).roleId(entity.getRoleId()).build();
		return to;
	}
}
