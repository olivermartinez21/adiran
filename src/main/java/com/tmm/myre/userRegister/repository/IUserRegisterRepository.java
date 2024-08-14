package com.tmm.myre.userRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.userRegister.model.UserRegisterModel;
@Repository("userRegisterRepository")
public interface IUserRegisterRepository extends JpaRepository<UserRegisterModel, String> {

	@Query(value = "SELECT * FROM MYRE_USER_INFORMATION WHERE ID_USER = :clientId and LOCATION=:warehouse", nativeQuery = true)
	UserRegisterModel getInformationUser(@Param("clientId") Integer clientId ,@Param("warehouse") String warehouse);

	@Query(value = "SELECT * FROM MYRE_USER_INFORMATION WHERE ID_USER = :userId", nativeQuery = true)
	UserRegisterModel getcode(@Param("userId") Integer userId);

}
