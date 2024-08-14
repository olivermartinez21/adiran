/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.model;

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
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APP_USER", schema = "MYRE")
public class AppUser implements IModel {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "USER_ID")
	private Integer userId;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "ENCRYTED_PASSWORD") 
	private String encrytedPassword;
	
	@Column(name = "ENABLED")
	private Integer enabled;

	
}
