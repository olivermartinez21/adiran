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

import com.tmm.myre.base.dto.ComboBox;
import com.tmm.myre.base.model.UserRole;
import com.tmm.myre.base.repository.IAppRoleRepository;
import com.tmm.myre.base.repository.IUserRoleRepository;
import com.tmm.myre.base.service.core.IUserRoleService;



/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 1 sep. 2020
 */
@Service("userRoleService")
public class UserRoleService implements IUserRoleService{

	@Autowired
	private IAppRoleRepository appRoleRepository;
	
	@Autowired
	private IUserRoleRepository userRoleRepository;
	
	@Override
	public List<ComboBox> getComboByUser(String userId) {
		List<ComboBox> combo = new ArrayList<ComboBox>();
		List<UserRole> entities = userRoleRepository.findByUser(userId);
		for(UserRole entity : entities) {
			combo.add(ComboBox.builder().id(entity.getRoleId().toString()).description(appRoleRepository.getDescription(entity.getRoleId())).build());
		}
		return combo;
	}

	
}
