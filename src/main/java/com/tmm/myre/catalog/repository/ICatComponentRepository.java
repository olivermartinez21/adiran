package com.tmm.myre.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatComponentModel;
@Repository("ICatComponentRepository")
public interface ICatComponentRepository extends JpaRepository<CatComponentModel, String>{

	@Query(value = "SELECT * FROM MYRE_CAT_COMPONENT where secction =:section and DC=:containerType or RF=:containerType or GD=:containerType or CH=:containerType ", nativeQuery = true)
	List<CatComponentModel> findByContainerTypeAndSecction(@Param("section") String section, String containerType);

	@Query(value = "SELECT DISTINCT SECCTION FROM MYRE_CAT_COMPONENT where DC=:containerType or RF=:containerType or GD=:containerType or CH=:containerType", nativeQuery = true)
	List<?>  findByContainerType(@Param("containerType")String containerType);

}
