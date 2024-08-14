package com.tmm.myre.catalog.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatJobcodeModel;

@Repository("ICatJobcodeRepository")
public interface ICatJobcodeRepository extends JpaRepository<CatJobcodeModel, String>{
	
	@Query(value = "SELECT * FROM  MYRE_CAT_JOBCODES WHERE JOBCODE_ID = :jobcodeId ;", nativeQuery = true)
	CatJobcodeModel jobcodeDescription(String jobcodeId);

}
