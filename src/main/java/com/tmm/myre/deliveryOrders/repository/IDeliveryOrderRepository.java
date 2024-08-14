package com.tmm.myre.deliveryOrders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
@Repository("deliveryOrderRepository")
public interface IDeliveryOrderRepository extends JpaRepository<DeliveryOrderModel, String> {

	@Query(value = "SELECT * FROM MYRE_DELIVERY_ORDER WHERE LOCATION=:warehouse", nativeQuery = true)
	List<DeliveryOrderModel> getByLocation(String warehouse);
	
	@Query(value = "SELECT * FROM MYRE_DELIVERY_ORDER WHERE ASSIGNMENT_ID=:assigntmentId", nativeQuery = true)
	List<DeliveryOrderModel> getOrderByAssignmentId(String assigntmentId);

	@Query(value = "SELECT count(*) FROM  MYRE_DELIVERY_ORDER where File_content IS NOT NULL", nativeQuery = true)
	int getcountpdf();

}
