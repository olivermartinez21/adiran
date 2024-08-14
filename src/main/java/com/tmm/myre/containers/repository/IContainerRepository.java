package com.tmm.myre.containers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
@Repository("containerRepository")
public interface IContainerRepository extends JpaRepository<ContainerModel, String> {

	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :appointmentId AND STATUS=1", nativeQuery = true)
	List<ContainerModel> findAllByAppointment(@Param("appointmentId") String appointmentId);
	
	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :appointmentId AND STATUS=1", nativeQuery = true)
	int countContainers(@Param("appointmentId") String appointmentId);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :appointmentId AND STATUS=2 OR STATUS=3 OR STATUS=4 OR STATUS=5", nativeQuery = true)
	List<ContainerModel> findAllInspectionsContainer(String appointmentId);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :appointmentId", nativeQuery = true)
	List<ContainerModel> findContainersInspections(String appointmentId);
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :appointmentId ", nativeQuery = true)
	List<ContainerModel> findAllContainersById(String appointmentId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :containerId AND STATUS=4", nativeQuery = true)
	int countContainersByVistoBueno(String containerId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE CONTAINER=:container", nativeQuery = true)
	int serarchBd(String container);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION =:warehouse and STATUS = 3 OR STATUS = 50 ", nativeQuery = true)
	List<ContainerModel> findAllGateIn(String warehouse);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION =:warehouse and STATUS = 6", nativeQuery = true)
	List<ContainerModel> findAllGateOut(String warehouse);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE CONTAINER=:container and STATUS=0", nativeQuery = true)
	ContainerModel findBycontainer(String container);
	
	//Agregado Oliver
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE CONTAINER=:container and STATUS=7", nativeQuery = true)
	ContainerModel findBycontainerReturn(String container);
	
	//Agregado Oliver
	@Query(value = "SELECT STATUS FROM MYRE_CONTAINERS WHERE CONTAINER=:container", nativeQuery = true)
	int findBycontainerStatus(String container);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE CONTAINER=:container and STATUS=0", nativeQuery = true)
	int serarchBdByStatus(String container);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehouse and STATUS = 1 or  LOCATION=:warehouse and STATUS = 2", nativeQuery = true)
	List<ContainerModel> findAllbyWarehousePregate(@Param("warehouse")String warehouse);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID=:appointmentId and LOCATION=:warehouse and STATUS=1 or APPOINTMENT_ID=:appointmentId and LOCATION=:warehouse and STATUS=2", nativeQuery = true)
	List<ContainerModel> findAllPreGate(String appointmentId, String warehouse);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehouse and status = 4 or status = 5", nativeQuery = true)
	List<ContainerModel> findAllbyWarehouse(String warehouse);
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehouse and status = 4 and CONTAINER_CONDITION = 1", nativeQuery = true)
	List<ContainerModel> findAllbyWarehouseStock(String warehouse);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE SHIPPING_COMPANY=:shippingId", nativeQuery = true)
	List<ContainerModel> findAllByClient(String shippingId);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE SHIPPING_COMPANY=:company and CONTAINER_TYPE=:Type and NOMENCALTURA=:nomenclatura and CLASIFICATION =1 ", nativeQuery = true)
	String getdisponibles(@Param("company") String company, @Param("Type") Integer Type ,@Param("nomenclatura") String nomenclatura);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE SHIPPING_COMPANY=:company and CONTAINER_TYPE=:Type and NOMENCALTURA=:nomenclatura and CLASIFICATION =:clasification ", nativeQuery = true)
	Integer getCantidad(String company, Integer Type, String nomenclatura, Integer clasification);

	@Query(value = "SELECT DISTINCT CONTAINER_TYPE FROM MYRE_CONTAINERS where LOCATION =:location and STATUS =4", nativeQuery = true)
	List<?> getContainerTypes(String location);

	@Query(value = "SELECT DISTINCT NOMENCALTURA FROM MYRE_CONTAINERS where  LOCATION =:location and CONTAINER_TYPE=:containerType and STATUS =4", nativeQuery = true)
	List<?> getNomenclaturas(String location, String containerType);
	
	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS where  LOCATION =:location and CONTAINER_TYPE=:containerType and NOMENCALTURA=:Nomenclatura and CONTAINER_CONDITION=:condition and STATUS =4 ", nativeQuery = true)
	Integer getCount(String location, String containerType, String Nomenclatura, Integer condition);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE CONTAINER = :unit AND STATUS=4", nativeQuery = true)
	ContainerModel getByUnit(String unit);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE CONTAINER = :unitNumber AND STATUS=4", nativeQuery = true)
	int existsByContainer(String unitNumber);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE EIR_NAME IS NOT NULL", nativeQuery = true)
	int getCountEir();

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE CONTAINER = :unitNumber", nativeQuery = true)
	ContainerModel getByUnitAssigment(String unitNumber);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE EIR_OUT_NAME IS NOT NULL", nativeQuery = true)
	int getCountEirOut();

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE CONTAINER = :unitNumber", nativeQuery = true)
	int existsBd(String unitNumber);
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehouse and STATUS_QUOTE =:status", nativeQuery = true)
	List<ContainerModel> findAllbyWarehouseStatus(String warehouse, int status);

	@Query(value = "SELECT COUNT(*) FROM MYRE_CONTAINERS WHERE QUOTE_NAME IS NOT NULL", nativeQuery = true)
	int getCountQuote(String containerId);
	
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehouse and STATUS_QUOTE IS NOT NULL ORDER BY REGISTER_DATE DESC ", nativeQuery = true)
	List<ContainerModel> findAllbyWarehouseStatus2(String warehouse);

	@Query(value = " SELECT Count(*) FROM MYRE_CONTAINERS where CONTAINER_ID=:containerId AND STATUS_QUOTE=1 ", nativeQuery = true)
	int getInspectionsMerchant(String containerId);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE CONTAINER=:containerId", nativeQuery = true)
	//@Query(value = "SELECT CONTAINER_ID,CONTAINER_TYPE,CONTAINER_ZISE,CONTAINER_CONDITION FROM MYRE_CONTAINERS WHERE CONTAINER=:containerId", nativeQuery = true)
	ContainerModel getContainerInformation(String containerId);
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehous and CONTAINER_TYPE=:type and CONTAINER_ZISE=:size and CONTAINER_CONDITION =1 and CLASIFICATION=:clasification", nativeQuery = true)
	List<ContainerModel> getUnitsFilter(String warehous,String type, String size, String clasification);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE APPOINTMENT_ID = :appointmentId and STATUS_QUOTE=2", nativeQuery = true)
	List<ContainerModel> getUnitsValidationAppointment(String appointmentId);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE LOCATION=:warehouse and STATUS_QUOTE!=1 and STATUS_QUOTE!=2 and STATUS_QUOTE!=4", nativeQuery = true)
	List<ContainerModel> findAllcontainersQuote(String warehouse);
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS WHERE CONTAINER=:containerId", nativeQuery = true)
	ContainerModel getPreLabor(String containerId);



	

}
