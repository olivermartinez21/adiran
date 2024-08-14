/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.administration.dto;

import java.util.Date;

import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserTableDto extends AbstractManagement implements ITransferObject {

	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	private String userName;
	private String roleList;
	private String password;
	private Integer enabled;

	@Builder
	public UserTableDto(Integer userId, String userName, String roleList, String password,
			Integer idUser, String operation, Date managmentDate, String log, Integer enabled) {
		
		super(idUser, operation, managmentDate, log);
		
		this.userId = userId;
		this.userName = userName;
		this.roleList = roleList;
		this.password = password;
		this.enabled = enabled;
	}
}
