/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.administration.service.core;

import java.util.List;

import com.tmm.myre.administration.dto.UserTableDto;
import com.tmm.myre.base.dto.AppRoleDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.dto.UserRoleDto;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */
public interface IUserManagementService {

	/**
	 * @return
	 */
	List<UserTableDto> getUserTable();
	
	List<UserTableDto> getCustomersTable();
	
	/**
	 * @param userTableDto
	 * @return
	 */
	ResponseManagement saveNewUser(UserTableDto userTableDto);

	/**
	 * @param appRoleDto
	 * @return
	 */
	ResponseManagement saveNewRole(AppRoleDto appRoleDto);

	/**
	 * @param userRoleDto
	 * @return
	 */
	ResponseManagement assignRole(UserRoleDto userRoleDto);

	/**
	 * @param userRoleDto
	 * @return
	 */
	ResponseManagement removeRole(UserRoleDto userRoleDto);

	/**
	 * @param userTableDto
	 * @return
	 */
	ResponseManagement removeUser(UserTableDto userTableDto);

	ResponseManagement activateUser(UserTableDto userTableDto);


	

	

	

}
