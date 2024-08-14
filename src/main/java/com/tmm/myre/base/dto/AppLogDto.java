/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 7 sep. 2020
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppLogDto extends AbstractManagement implements ITransferObject {

	public static final long serialVersionUID = 1L;
	
	private String id;
	private Integer userId;
	private Date managementDate;
	private String operation;
	private String log;
}
