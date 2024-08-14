package com.tmm.myre.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatCustomerModel;

@Repository("ICatCustomerReposirtory")
public interface ICatCustomerReposirtory extends JpaRepository<CatCustomerModel, String>{

	@Query(value = "SELECT * FROM MYRE_CAT_CUSTOMERS WHERE CUSTOMER_TYPE='CARRIER'", nativeQuery = true)
	List<CatCustomerModel> findAllShipping();
	
	@Query(value = "SELECT * FROM  MYRE_CAT_CUSTOMERS WHERE CUSTOMER_ID = :customerId ;", nativeQuery = true)
	CatCustomerModel addressClient(String customerId);

}
