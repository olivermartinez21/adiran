/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.administration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmm.myre.administration.dto.UserTableDto;
import com.tmm.myre.administration.service.core.IUserManagementService;
import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.AppRoleDto;
import com.tmm.myre.base.dto.AppUserDto;
import com.tmm.myre.base.dto.ComboBox;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.dto.UserRoleDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.service.core.IAppRoleService;
import com.tmm.myre.base.service.core.IAppUserService;
import com.tmm.myre.base.service.core.IUserRoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */

@Slf4j
@Controller
@RequestMapping(CustomerAdministrationController.HOME)
public class CustomerAdministrationController extends AbstractMyreController{

	public static final String HOME = PREFIX_ADMIN + "customersAdministration";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IUserManagementService userManagementService;
	
	@Autowired
	private IAppRoleService appRoleService;
	
	@Autowired
	private IAppUserService appUserService;
	
	@Autowired
	private IUserRoleService userRoleService;
	
	
	@ModelAttribute("roleCombo")
	private List<AppRoleDto> getAppRoleCombo() {
		try {
			return appRoleService.findAll();
		} catch(ConverterException ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@ModelAttribute("userCombo")
	private List<AppUserDto> getAppUserCombo() {
		try {
			return appUserService.findAll();
		} catch(ConverterException ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<UserTableDto> getDataTable() {
		try {
			return userManagementService.getCustomersTable();
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}

	
	@GetMapping("getRoles")
	@ResponseBody
	public List<ComboBox> getRoles(@RequestParam("userId") String userId) {
		try {
			return userRoleService.getComboByUser(userId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@PostMapping("addNewUser")
	@ResponseBody
	public ResponseManagement addNewUser(@ModelAttribute("userTableDto") UserTableDto userTableDto) {
		ResponseManagement response = new ResponseManagement();
		userTableDto.setIdUser(getUserId(getUser()));
		try {
			response = userManagementService.saveNewUser(userTableDto);
		} catch(Exception ex) {
			log.info(ex.toString());
			response.setSuccess(false);
			response.setErrorCode("500");
			response.setMessage(setOperationError(userTableDto.getOperation()));
		}
		return response;
	}
	
	@PostMapping("addNewRole")
	@ResponseBody
	public ResponseManagement addNewRole(@ModelAttribute("appRoleDto") AppRoleDto appRoleDto) {
		ResponseManagement response = new ResponseManagement();
		appRoleDto.setIdUser(getUserId(getUser()));
		try {
			response = userManagementService.saveNewRole(appRoleDto);
		} catch(Exception ex) {
			log.info(ex.toString());
			response.setSuccess(false);
			response.setErrorCode("500");
			response.setMessage(setOperationError(appRoleDto.getOperation()));
		}
		return response;
	}
	
	@PostMapping("assignRole")
	@ResponseBody
	public ResponseManagement assignRole(@ModelAttribute("userRoleDto") UserRoleDto userRoleDto) {
		ResponseManagement response = new ResponseManagement();
		userRoleDto.setIdUser(getUserId(getUser()));
		try {
			response = userManagementService.assignRole(userRoleDto);
		} catch(Exception ex) {
			log.info(ex.toString());
			response.setSuccess(false);
			response.setErrorCode("500");
			response.setMessage(setOperationError(userRoleDto.getOperation()));
		}
		return response;
	}
	
	@PostMapping("removeRole")
	@ResponseBody
	public ResponseManagement removeRole(@ModelAttribute("userRoleDto") UserRoleDto userRoleDto) {
		ResponseManagement response = new ResponseManagement();
		userRoleDto.setIdUser(getUserId(getUser()));
		try {
			response = userManagementService.removeRole(userRoleDto);
		} catch(Exception ex) {
			log.info(ex.toString());
			response.setSuccess(false);
			response.setErrorCode("500");
			response.setMessage(setOperationError(userRoleDto.getOperation()));
		}
		return response;
	}
	
	@PostMapping("removeUser")
	@ResponseBody
	public ResponseManagement removeUser(@ModelAttribute("userTableDto") UserTableDto userTableDto) {
		ResponseManagement response = new ResponseManagement();
		userTableDto.setIdUser(getUserId(getUser()));
		try {
			response = userManagementService.removeUser(userTableDto);
		} catch(Exception ex) {
			log.info(ex.toString());
			response.setSuccess(false);
			response.setErrorCode("500");
			response.setMessage(setOperationError(userTableDto.getOperation()));
		}
		return response;
	}
	
	@PostMapping("activateUser")
	@ResponseBody
	public ResponseManagement activateUser(@ModelAttribute("userTableDto") UserTableDto userTableDto) {
		ResponseManagement response = new ResponseManagement();
		userTableDto.setIdUser(getUserId(getUser()));
		try {
			response = userManagementService.activateUser(userTableDto);
		} catch(Exception ex) {
			log.info(ex.toString());
			response.setSuccess(false);
			response.setErrorCode("500");
			response.setMessage(setOperationError(userTableDto.getOperation()));
		}
		return response;
	}
	
	
}
