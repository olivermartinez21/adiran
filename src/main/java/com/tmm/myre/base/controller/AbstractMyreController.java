/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tmm.myre.base.dto.LoggedUserDto;
import com.tmm.myre.base.service.UserDetailService;
import com.tmm.myre.base.utils.DateManagement;



/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
public abstract class AbstractMyreController {

	public static final String EMPTY = "";
	
	public static final String PREFIX_ADMIN = "/administration/";
	
	public static final String PREFIX_APPOINTMENT = "/appointments/";
	
	public static final String PREFIX_CONTAINER = "/containers/";
	
	public static final String PREFIX_INSPECTION = "/inspections/";
	
	public static final String PREFIX_PREREGISTRATION = "/preRegistration/"; 
	
	public static final String PREFIX_PANTACO = "/pantaco/"; 
	
	public static final String PREFIX_ASSIGNMENTS = "/assignments/"; 
	
	public static final String PREFIX_DELIVERY_ORDERS = "/deliveryOrders/"; 
	
	public static final String PREFIX_QUOTES = "/quotes/"; 
	
	
	@Autowired
	private UserDetailService userDetailService;
	
	
	public String getFileExtension(String fileType) {
		switch(fileType) {
		case "image/jpeg":
			return ".jpg";
		case "text/xml":
			return ".xml";
		case "application/pdf":
			return ".pdf";
		default: return "";
		}
	}
	
	
	
	@ModelAttribute("loggedUser")
	public LoggedUserDto getLoggedUser() {
		LoggedUserDto loggedUserDto = new LoggedUserDto();
		loggedUserDto.setCurrentDate(DateManagement.today("/"));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			loggedUserDto.setUserName(auth.getName());
			try {
	            loggedUserDto.setUserId(Long.valueOf(loggedUserDto.getUserName() != null ? userDetailService.getUserId(loggedUserDto.getUserName()).toString() : "9999")); //anonymoususer
	        } catch (NumberFormatException e) {
	        	loggedUserDto.setUserId(1L);
	        }
		} else {
			loggedUserDto.setUserName("anonymus");
		}
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		for(GrantedAuthority authority : authorities) {
		if( authority.toString().contains("CLIENTE NAVIERA")) {
				loggedUserDto.setUserRole("CLIENTE NAVIERA");
			} else if( authority.toString().contains("CLIENTE AA")) {
				loggedUserDto.setUserRole("CLIENTE AA");
			} else if( authority.toString().contains("CAPTURISTA")) {
				loggedUserDto.setUserRole("CAPTURISTA");
			}else if( authority.toString().contains("SUPERVISOR DE CAPTURA")) {
				loggedUserDto.setUserRole("SUPERVISOR DE CAPTURA ");
			}else if( authority.toString().contains("INSPECTOR")) {
				loggedUserDto.setUserRole("INSPECTOR");
			}else if( authority.toString().contains("SUPERVISOR DE PATIO")) {
				loggedUserDto.setUserRole("SUPERVISOR DE PATIO");
			}else if( authority.toString().contains("ALMACENISTA")) {
				loggedUserDto.setUserRole("ALMACENISTA");
			}else if( authority.toString().contains("CONTADOR")) {
				loggedUserDto.setUserRole("CONTADOR");
			}else if( authority.toString().contains("JEFE DE OPERACIONES")) {
				loggedUserDto.setUserRole("JEFE DE OPERACIONES");
			}else if( authority.toString().contains("CONSULTA")) {
				loggedUserDto.setUserRole("CONSULTA");
			}else if( authority.toString().contains("ADMINISTRACION CORPORATIVA")) {
				loggedUserDto.setUserRole("ADMINISTRACION CORPORATIVA");
			}else if(authority.toString().contains("ADMIN")) {
				loggedUserDto.setUserRole("ADMINSHOW");
			}
		}
		loggedUserDto.setWarehouseId(getWarehouse());
		return loggedUserDto;
	}
	
	public String getWarehouse() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		for(GrantedAuthority authority : authorities) {
			
			if(authority.toString().contains("AGUASCALIENTES")) {
				return "AGUASCALIENTES";
			} else if(authority.toString().contains("ALTAMIRA")) {
				return "ALTAMIRA";
			} if(authority.toString().contains("ENSENADA")) {
				return "ENSENADA";
			}if(authority.toString().contains("VERACRUZ")) {
				return "VERACRUZ";
			}if(authority.toString().contains("PANTACO")) {
				return "PANTACO";
			}if(authority.toString().contains("MANZANILLO")) {
				return "MANZANILLO";
			}
		}
		return null;
	}
	
	
	public String getUser() {
		String user = "anonymous";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    user = authentication.getName();
		}
		return user;
	}
	
	public Integer getUserId(String userName) {
		return userDetailService.getUserId(userName);
	}
	
	public String setOperationError(String operation) {
		switch(operation) {
		case "INSERT": return "ERROR AL CREAR EL REGISTRO";
		case "UPDATE": return "ERROR AL ACTIALIZAR EL REGISTRO";
		case "DELETE": return "ERROR AL BORRAR EL REGISTRO";
		}
		return null;
	}
}

