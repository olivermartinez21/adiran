package com.tmm.myre.assignments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.model.AssignmentsModel;

@Repository("assignmentRepository")
public interface IAssignmentRepository extends JpaRepository<AssignmentModel, String> {

	
	@Query(value = "SELECT * FROM MYRE_ASSIGNMENT WHERE LOCATION=:warehouse", nativeQuery = true)
	List<AssignmentsModel> getByLocation(String warehouse);

	@Query(value = "SELECT * FROM MYRE_ASSIGNMENT WHERE BOOKING_ID=:bookingId and STATUS=1", nativeQuery = true)
	List<AssignmentModel> getByBookingId(String bookingId);

	
	@Query(value = "SELECT * FROM MYRE_ASSIGNMENT WHERE BOOKING_ID=:bookingId and STATUS=2", nativeQuery = true)
	List<AssignmentModel> getByBooking(String bookingId);

	@Query(value = "SELECT * FROM MYRE_ASSIGNMENT WHERE DELIVERY_ORDER_ID=:deliveryId and STATUS=2", nativeQuery = true)
	List<AssignmentModel> getAssignmentByDeliveryId(String deliveryId);

	@Query(value = "SELECT * FROM  MYRE_ASSIGNMENT WHERE STATUS=2", nativeQuery = true)
	List<AssignmentModel> getAssignments();

	@Query(value = "SELECT * FROM  MYRE_ASSIGNMENT WHERE UNITNUMBER=:containerId", nativeQuery = true)
	AssignmentModel getbycontainer(String containerId);
	
	@Query(value = "SELECT * FROM  MYRE_ASSIGNMENT WHERE DELIVERY_ORDER_ID =:deliveryOrderId", nativeQuery = true)
	AssignmentModel getAssignment(String deliveryOrderId);
	
	@Query(value = "DELETE FROM MYRE_ASSIGNMENT WHERE UNITNUMBER =unitNumber ", nativeQuery = true)
	AssignmentModel daleteUnit(String unitNumber);

}
