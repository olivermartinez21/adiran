package com.tmm.myre.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.quote.model.QuoteModel;
@Repository("quoteRepository")
public interface IQuoteRepository extends JpaRepository<QuoteModel, String> {

	
	@Query(value = "SELECT COUNT(*) FROM  MYRE_QUOTES where INSPECTION_ID = :inspectionId", nativeQuery = true)
      int getByIdInspection(@Param("inspectionId")String inspectionId);
	
	@Query(value = "SELECT * FROM  MYRE_QUOTES where INSPECTION_ID = :inspectionId", nativeQuery = true)
    QuoteModel getByInspectionId(@Param("inspectionId")String inspectionId);


	
	
}
