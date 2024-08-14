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

import com.tmm.myre.base.model.UserRole;


/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Repository("userRoleRepository")
public interface IUserRoleRepository extends JpaRepository<UserRole, Integer>{
	
	@Query(value = "SELECT * FROM USER_ROLE WHERE USER_ID = :userId AND ROLE_ID = :roleId", nativeQuery = true)
	UserRole findByUserAndRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

	@Query(value = "SELECT MAX(ID) + 1 FROM USER_ROLE", nativeQuery = true)
	Integer getNext();

	/**
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT * FROM USER_ROLE WHERE USER_ID = :userId", nativeQuery = true)
	List<UserRole> findByUser(@Param("userId") String userId);

	
	@Query(value = "SELECT ROLE_ID FROM USER_ROLE WHERE USER_ID = :userId", nativeQuery = true)
	int getRole(@Param("userId") Integer userId);

}