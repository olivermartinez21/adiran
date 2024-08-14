/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserDto implements ITransferObject {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String userName;
	private String currentDate;
	private String warehouseId;
	private String userRole;
	}