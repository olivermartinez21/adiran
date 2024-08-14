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

import com.tmm.myre.base.model.AppRole;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Repository("appRoleRepository")
public interface IAppRoleRepository extends JpaRepository<AppRole, Integer>{

	@Query(value = "SELECT r.ROLE_NAME FROM USER_ROLE ur, APP_ROLE r WHERE ur.ROLE_ID = r.ROLE_ID AND ur.USER_ID = :userId", nativeQuery = true)
	List<String> getRoleNames(@Param("userId") Integer integer);
	
	/**
	 * @param string
	 * @return
	 */
	@Query(value = "SELECT * FROM APP_ROLE WHERE ROLE_NAME = :roleName", nativeQuery = true)
	AppRole verifyActiveRole(@Param("roleName") String roleName);

	/**
	 * @return
	 */
	@Query(value = "SELECT MAX(ROLE_ID) + 1 FROM APP_ROLE", nativeQuery = true)
	Integer getNext();

	/**
	 * @param roleId
	 * @return
	 */
	@Query(value = "SELECT ROLE_NAME FROM APP_ROLE WHERE ROLE_ID = :roleId", nativeQuery = true)
	String getDescription(@Param("roleId") Integer roleId);
}
