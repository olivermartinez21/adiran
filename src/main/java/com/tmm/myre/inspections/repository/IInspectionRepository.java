package com.tmm.myre.inspections.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	@Query(value = "SELECT  * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=3", nativeQuery = true)
	List<InspectionModel> getAllInspectionsByContainerIdStatus(String containerId);

	@Query(value = "SELECT  * FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS_QUOTE=1", nativeQuery = true)
	int getInspectionsMerchant(String containerId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND STATUS=3", nativeQuery = true)
	int getCountInspectionsRepair(String containerId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_INSPECTIONS where CONTAINER_ID=:containerId AND CUSTOMER_TYPE=1", nativeQuery = true)
	int getInspectionMerchant(String containerId);
	
	@Query(value = "SELECT * FROM MYRE_INSPECTIONS where INSPECTION_ID=:inspectionId", nativeQuery = true)
	InspectionModel getInspectionForLabor(String inspectionId);



}
