package com.tmm.myre.appointments.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.appointments.model.AppointmentModel;
@Repository("appointmentRepository")
public interface IAppointmentRepository extends JpaRepository<AppointmentModel, String> {
	
	
	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE USER = :clientId", nativeQuery = true)
	List<AppointmentModel> getUseAppointmentTable(@Param("clientId") Integer clientId);

	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE STATUS = 1", nativeQuery = true)
	List<AppointmentModel> findAllActiveAppointment();

	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE STATUS = 2 OR STATUS = 3", nativeQuery = true)
	List<AppointmentModel> findAllInspectorAppointment();
	
	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE STATUS = 4 OR STATUS = 5 OR STATUS = 6", nativeQuery = true)
	List<AppointmentModel> findAllRepairsAppointment();

	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE STATUS = 4 OR STATUS = 5 OR STATUS = 6", nativeQuery = true)
	List<AppointmentModel> findAlltechnicalAppointment();
	
	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE DATE BETWEEN :startDate AND :lastDate and LOCATION=:warehouse", nativeQuery = true)
	List<AppointmentModel> getAppointmentsByDate(@Param("startDate") Date startDate,@Param("lastDate")  Date lastDate,@Param("warehouse")  String warehouse);

	@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE LOCATION=:warehouse", nativeQuery = true)
	List<AppointmentModel> findLocation(@Param("warehouse")String warehouse);

	@Query(value = "SELECT COUNT(*) FROM MYRE_APPOINTMENTS WHERE LOCATION=:warehouse", nativeQuery = true)
	int countWarehouse(String warehouse);

	//-------------------------------------------------------------------------
	/*@Query(value = "SELECT * FROM MYRE_APPOINTMENTS WHERE USER = :clientId AND LOCATION = :warehouse", nativeQuery = true)
	List<AppointmentModel> getUseAppointmentTable(@Param("userId") Integer userId, @Param("warehouse") String warehouse);*/


}
