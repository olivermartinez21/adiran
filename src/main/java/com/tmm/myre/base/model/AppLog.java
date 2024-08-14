/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "APP_LOG", schema = "MYRE")
public class AppLog implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID") private String id;
	@Column(name = "USER_ID") private Integer userId;
	@Column(name = "MANAGEMENT_DATE") private Date managementDate;
	@Column(name = "OPERATION") private String operation;
	@Column(name = "LOG") private String log;
}
