/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.userRegister.dto.UserRegisterDto;
import com.tmm.myre.userRegister.service.core.IUserInformationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Slf4j
@Controller
public class MainController extends AbstractMyreController {
	
	@Autowired
	private IUserInformationService userInformationService;
	
	@GetMapping("login")
	public String login() {
		return "login";
	}

	@GetMapping({"", "/", "index"})
	public String logged() {
		/**log.info("Succesuful Logged");
		if(getWarehouse().equals("PANTACO")) {
			return "pantacoIndex"; 
		} else {
		    return "index";
		}/**/
		return "index";
	}
	
	@PostMapping("login/register")
	@ResponseBody
	public ResponseManagement appointmentValidation(@ModelAttribute("userRegisterDto") UserRegisterDto userRegisterDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return userInformationService.saveUserInformation(userRegisterDto);
		} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.UPDATE);
		}/**/
		return response;
	}

}