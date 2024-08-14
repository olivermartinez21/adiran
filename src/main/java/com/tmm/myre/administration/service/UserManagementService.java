/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.administration.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.administration.dto.UserTableDto;
import com.tmm.myre.administration.service.core.IUserManagementService;
import com.tmm.myre.base.dto.AppRoleDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.dto.UserRoleDto;
import com.tmm.myre.base.model.AppRole;
import com.tmm.myre.base.model.AppUser;
import com.tmm.myre.base.model.UserRole;
import com.tmm.myre.base.repository.IAppRoleRepository;
import com.tmm.myre.base.repository.IAppUserRepository;
import com.tmm.myre.base.repository.IUserRoleRepository;
import com.tmm.myre.base.service.core.IAppLogService;
import com.tmm.myre.base.utils.EncrytedPasswordUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */

@Slf4j
@Service("userManagementService")
public class UserManagementService implements IUserManagementService{

	@Autowired
	private IAppUserRepository appUserRepository;
	
	@Autowired
	private IAppRoleRepository appRoleRepository;
	
	@Autowired
	private IUserRoleRepository userRoleRepository;
	
	@Autowired
	private IAppLogService appLogService;
	
	@Override
	public List<UserTableDto> getUserTable() {
		List<AppUser> users = appUserRepository.findAllActive();
		List<UserTableDto> list = new ArrayList<UserTableDto>();
		for(AppUser user : users) {
			list.add(UserTableDto.builder()
					.userId(user.getUserId())
					.userName(user.getUserName())
					.roleList(appRoleRepository.getRoleNames(user.getUserId()).toString())
					.build());
		}
		return list;
	}
	
	@Override
	public List<UserTableDto> getCustomersTable() {
		List<AppUser> users = appUserRepository.findAllActiveCustomers();
		List<UserTableDto> list = new ArrayList<UserTableDto>();
		for(AppUser user : users) {
			list.add(UserTableDto.builder()
					.userId(user.getUserId())
					.userName(user.getUserName())
					.roleList(appRoleRepository.getRoleNames(user.getUserId()).toString())
					.enabled(user.getEnabled())
					.build());
		}
		return list;
	}
	

	@Override
	public ResponseManagement saveNewUser(UserTableDto userTableDto) {
		ResponseManagement response = ResponseManagement.builder().operation(userTableDto.getOperation()).build();
		
		if(appUserRepository.verifyActiveUser(userTableDto.getUserName()) != null) {
			response.setErrorCode("409");
			response.setSuccess(false);
			response.setMessage("El nombre de usuario ya esta registrado. Intente nuevamente");
		} else {
			
			AppUser appUser = AppUser.builder()
					.userId(appUserRepository.getNext())
					.userName(userTableDto.getUserName())
					.encrytedPassword(EncrytedPasswordUtils.encrytePassword(userTableDto.getPassword()))
					.enabled(1)
					.build();
			
			UserRole userRole = UserRole.builder()
					.id(userRoleRepository.getNext())
					.userId(appUser.getUserId())
					.roleId(Integer.parseInt(userTableDto.getRoleList()))
					.build();
			userTableDto.setLog("AppUser(" + appUser.getUserId() + ", " + appUser.getUserName() + "), " + userRole.toString());
			
			try {
				appUserRepository.save(appUser);
				userRoleRepository.save(userRole);
				appLogService.registerLog(userTableDto.getIdUser(), userTableDto.getOperation(), userTableDto.getLog());
				response.setSuccess(true);
			} catch(Exception ex) {
				response.setErrorCode("500");
				response.setMessage("Error inesepardo al guardar el usuario, intente nuevamente, si el problema persiste reportelo a Desarrollo");
				log.info(ex.toString());
			}
		}
		return response;
	}

	@Override
	public ResponseManagement saveNewRole(AppRoleDto appRoleDto) {
		ResponseManagement response = ResponseManagement.builder().operation(appRoleDto.getOperation()).build();
		if(appRoleRepository.verifyActiveRole("ROLE_" + appRoleDto.getRoleName().toUpperCase()) != null) {
			response.setErrorCode("409");
			response.setMessage("El rol de usuario ya esta registrado. Intente nuevamente");
			response.setSuccess(false);
		} else {
			AppRole entity = AppRole.builder()
					.roleId(appRoleRepository.getNext())
					.roleName("ROLE_" + appRoleDto.getRoleName().toUpperCase())
					.build();
			appRoleDto.setLog(appRoleDto.toString());
			
			try {
				appRoleRepository.save(entity);
				appLogService.registerLog(appRoleDto.getIdUser(), appRoleDto.getOperation(), entity.toString());
				response.setSuccess(true);
			} catch(Exception ex) {
				response.setErrorCode("500");
				response.setMessage("Error inesepardo al guardar el rol: " + ex.toString());
				response.setSuccess(false);
				log.info(ex.toString());
				
			}
		}
		return response;
	}

	@Override
	public ResponseManagement assignRole(UserRoleDto userRoleDto) {
		ResponseManagement response = ResponseManagement.builder().operation(userRoleDto.getOperation()).build();
		
		UserRole entity = UserRole.builder()
				.id(userRoleRepository.getNext())
				.userId(userRoleDto.getUserId())
				.roleId(userRoleDto.getRoleId())
				.build();
		
		try {
			userRoleRepository.save(entity);
			appLogService.registerLog(userRoleDto.getIdUser(), userRoleDto.getOperation(), entity.toString());
			response.setSuccess(true);
		} catch(Exception ex) {
			response.setErrorCode("500");
			response.setMessage("Error al asignar el rol: " + ex.toString());
			response.setSuccess(false);
			log.info(ex.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement removeRole(UserRoleDto userRoleDto) {
		ResponseManagement response = ResponseManagement.builder().operation(userRoleDto.getOperation()).build();
		UserRole entity = userRoleRepository.findByUserAndRole(userRoleDto.getUserId(), userRoleDto.getRoleId());
		try {
			userRoleRepository.delete(entity);
			appLogService.registerLog(userRoleDto.getIdUser(), userRoleDto.getOperation(), entity.toString());
			response.setSuccess(true);
		} catch(Exception ex) {
			response.setErrorCode("500");
			response.setMessage("Error al asignar el rol: " + ex.toString());
			response.setSuccess(false);
			log.info(ex.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement removeUser(UserTableDto userTableDto) {
		ResponseManagement response = ResponseManagement.builder().operation(userTableDto.getOperation()).build();
		AppUser entity = appUserRepository.getById(userTableDto.getUserId());
		userTableDto.setLog(entity.toString() + " -> ");
		entity.setEnabled(0);
		try {
			appUserRepository.save(entity);
			appLogService.registerLog(userTableDto.getIdUser(), userTableDto.getOperation(), userTableDto.getLog() + entity.toString());
			response.setSuccess(true);
		} catch(Exception ex) {
			response.setErrorCode("500");
			response.setMessage("Error al asignar el rol: " + ex.toString());
			response.setSuccess(false);
			log.info(ex.toString());
		}
		return response;
	}
	
	@Override
	public ResponseManagement activateUser(UserTableDto userTableDto) {
		ResponseManagement response = ResponseManagement.builder().operation(userTableDto.getOperation()).build();
		AppUser entity = appUserRepository.getById(userTableDto.getUserId());
		userTableDto.setLog(entity.toString() + " -> ");
		entity.setEnabled(1);
		try {
			appUserRepository.save(entity);
			appLogService.registerLog(userTableDto.getIdUser(), userTableDto.getOperation(), userTableDto.getLog() + entity.toString());
			response.setSuccess(true);
		} catch(Exception ex) {
			response.setErrorCode("500");
			response.setMessage("Error al asignar el rol: " + ex.toString());
			response.setSuccess(false);
			log.info(ex.toString());
		}
		return response;
	}

	

}
