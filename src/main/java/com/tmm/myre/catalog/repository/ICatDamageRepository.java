package com.tmm.myre.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatDamageModel;
@Repository("ICatDamageRepository")
public interface ICatDamageRepository extends JpaRepository<CatDamageModel, String>{
	
	@Query(value = "SELECT * FROM  MYRE_CAT_DAMAGE where DC='p';", nativeQuery = true)
	List<CatDamageModel> findAllbycontainerDC();

	@Query(value = "SELECT * FROM  MYRE_CAT_DAMAGE where RF='p';", nativeQuery = true)
	List<CatDamageModel> findAllbycontainerRF();

	@Query(value = "SELECT * FROM  MYRE_CAT_DAMAGE where GD='p';", nativeQuery = true)
	List<CatDamageModel> findAllbycontainerGD();

	@Query(value = "SELECT * FROM  MYRE_CAT_DAMAGE where CH='p';", nativeQuery = true)
	List<CatDamageModel> findAllbycontainerCH();

	@Query(value = "SELECT DESCRIPTION FROM  MYRE_CAT_DAMAGE where DAMAGE_ID = :damage", nativeQuery = true)
	String getDescription(@Param("damage")Integer damage);

}
