package com.tmm.myre.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatNomenclaturaModel;
@Repository("ICatNomenclaturaRepository")
public interface ICatNomenclaturaRepository extends JpaRepository<CatNomenclaturaModel, String> {

	@Query(value = "SELECT * FROM MYRE_CAT_NOMENCLATURAS where CONTAINER_TYPE=:containerType and SIZE=:size ", nativeQuery = true)
	List<CatNomenclaturaModel> findBYTransportType(@Param("containerType")String containerType, @Param("size")String size );

	@Query(value = "SELECT * FROM MYRE_CAT_NOMENCLATURAS where CONTAINER_TYPE=:containerType", nativeQuery = true)
	List<CatNomenclaturaModel> findGS(String containerType);

}
