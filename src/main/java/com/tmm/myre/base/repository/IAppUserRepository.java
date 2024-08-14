/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.base.model.AppUser;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Repository("appUserRepository")
public interface IAppUserRepository extends JpaRepository<AppUser, Integer>{

	AppUser findByUserName(String username);

	/**
	 * @return
	 */
	@Query(value = "SELECT * FROM APP_USER WHERE ENABLED = 1", nativeQuery = true)
	List<AppUser> findAllActive();

	/**
	 * @param userName
	 * @return
	 */
	@Query(value = "SELECT * FROM APP_USER WHERE USER_NAME = :userName", nativeQuery=true)
	AppUser verifyActiveUser(@Param("userName") String userName);

	/**
	 * @return
	 */
	@Query(value = "SELECT MAX(USER_ID) + 1 FROM APP_USER", nativeQuery = true)
	Integer getNext();

	@Query(value = "SELECT APP_USER.USER_ID, APP_USER.USER_NAME, APP_USER.ENCRYTED_PASSWORD ,APP_USER.ENABLED FROM APP_USER, USER_ROLE WHERE USER_ROLE.USER_ID = APP_USER.USER_ID and USER_ROLE.ROLE_ID= 1", nativeQuery = true)
	List<AppUser> findAllActiveCustomers();
	
	// AND APP_USER.ENABLED = 1
}