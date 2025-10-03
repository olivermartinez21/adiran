package com.tmm.myre.catalog.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatShippingCompanyModel;

import java.util.List;

@Repository("ICatShippingCompanyReposirtory")
public interface ICatShippingCompanyReposirtory extends JpaRepository<CatShippingCompanyModel, String>{
	
	@Query(value = "SELECT * FROM MYRE_CAT_SHIPPINGCOMPANY WHERE CODE=:code", nativeQuery = true)
	CatShippingCompanyModel getidclient(String code);
	
	
	@Query(value = "SELECT * FROM MYRE.MYRE_CAT_SHIPPINGCOMPANY WHERE SHIPPINGCOMPANY_ID = :shipingId", nativeQuery = true)
	CatShippingCompanyModel getLabor(Integer shipingId);

	@Query(value = "SELECT * FROM MYRE.MYRE_CAT_SHIPPINGCOMPANY WHERE SHIPPINGCOMPANY_ID = :shipingId", nativeQuery = true)
	List<CatShippingCompanyModel> getShippingDesc(String shipingId);



	@Query(value = "SELECT * FROM MYRE.MYRE_CAT_SHIPPINGCOMPANY WHERE SHIPPINGCOMPANY_ID = :shippingCompanyId", nativeQuery = true)
	 CatShippingCompanyModel getPreLaborFinal(String shippingCompanyId);
	 
	 @Query(value = "SELECT *\r\n"
	 		+ "FROM MYRE.MYRE_CAT_SHIPPINGCOMPANY mcs\r\n"
	 		+ "WHERE mcs.SHIPPINGCOMPANY_ID = (\r\n"
	 		+ "    SELECT con.SHIPPING_COMPANY\r\n"
	 		+ "    FROM MYRE.MYRE_CONTAINERS con\r\n"
	 		+ "    WHERE con.SHIPPING_COMPANY = mcs.SHIPPINGCOMPANY_ID AND con.CONTAINER_ID = (SELECT mi.CONTAINER_ID FROM MYRE_INSPECTIONS mi where mi.INSPECTION_ID =:inspectionId )\r\n"
	 		+ ")", nativeQuery = true) 
	 CatShippingCompanyModel getPreLaborFinalFinal(String inspectionId);

	@Query(value = "SELECT * FROM MYRE.MYRE_CAT_SHIPPINGCOMPANY WHERE SHIPPINGCOMPANY_ID = 999", nativeQuery = true)
	CatShippingCompanyModel getPreLaborMerchant();

    CatShippingCompanyModel findByDescription(String customerName);
}
