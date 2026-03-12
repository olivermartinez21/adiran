package com.tmm.myre.inspections.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.inspections.model.InspectionModel;
import com.tmm.myre.photo.model.PhotoModel;
@Repository("inspectorRepository")
public interface IInspectionRepository extends JpaRepository<InspectionModel, String>{

	
	@Query(value = "SELECT * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId and DAMAGE_GENSET IS NULL ", nativeQuery = true)
	List<InspectionModel> findAllByContainer(String containerId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=1", nativeQuery = true)
	int countInspections(String containerId);
	
	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=:status", nativeQuery = true)
	int countInspectionsValidation(String containerId, int status);
	
	@Query(value = "SELECT  * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=2", nativeQuery = true)
	List<InspectionModel> getAllInspectionsByContainerId(String containerId);

	@Query(value = "SELECT  * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=3 AND EXTENT_LARGE IS NULL", nativeQuery = true)
	List<InspectionModel> getAllInspectionsByContainerIdStatus(String containerId);

	@Query(value = "SELECT  * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS_QUOTE=1", nativeQuery = true)
	int getInspectionsMerchant(String containerId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=3", nativeQuery = true)
	int getCountInspectionsRepair(String containerId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND CUSTOMER_TYPE=1", nativeQuery = true)
	int getInspectionMerchant(String containerId);
	
	@Query(value = "SELECT * FROM MYRE_INSPECTIONS where INSPECTION_ID=:inspectionId", nativeQuery = true)
	InspectionModel getInspectionForLabor(String inspectionId);
	
	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId", nativeQuery = true)
	int countInspectionsRequest(String containerId);

	//Union de tablas
	@Query(value = "SELECT i.INSPECTION_ID, i.PART, i.COMPONET, i.DAMAGE, i.LOCATION, i.REPAIR, " +
			"i.DAMAGE_GENSET, i.DAMAGE_CODE, i.REFERENCE, i.CUSTOMER_TYPE, i.CUSTOMER_NAME, i.IMAGE, i.STATUS, " +
			"i.CONTAINER_ID, i.LENGTH, i.WIDTH, i.DEPTH, i.OTHER_LENGTH, i.EXTENT_LARGE, " +
			"i.EXTENT_HEIGHT, i.EXTENT_DEPTH, i.EXTENT_OTHER_LENGTH, i.QUANTITY, " +
			"q.QUOTE_ID, q.WORK_CODE, q.REPAIR_DESCRIPTION, q.HOURS, q.LABOR, q.MATERIAL, q.TARIFA, q.EXCHANGE " +
			"FROM MYRE.MYRE_INSPECTIONS i " +
			"LEFT JOIN MYRE.MYRE_QUOTES q ON i.INSPECTION_ID = q.INSPECTION_ID " +
			"WHERE i.CONTAINER_ID = :containerId",
			nativeQuery = true)
	List<Object[]> findInspectionsWithQuotes(@Param("containerId") String containerId);

	@Query(value = "SELECT * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId", nativeQuery = true)
	List<InspectionModel> findAllByContainerId(String containerId);



}
