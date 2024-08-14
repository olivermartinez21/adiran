package com.tmm.myre.assignments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.assignments.model.AssignmentsModel;
import com.tmm.myre.assignments.model.BookingModel;

@Repository("bookingRepository")
public interface IBookingRepository extends JpaRepository<BookingModel, String> {

	
	@Query(value = "SELECT * FROM MYRE_BOOKINGS WHERE LOCATION=:warehouse", nativeQuery = true)
	List<BookingModel> getByLocation(String warehouse);

	@Query(value = "SELECT * FROM MYRE_BOOKINGS WHERE LOCATION=:warehouse and BOOKING=:booking", nativeQuery = true)
	BookingModel getByBookingName(String booking, String warehouse);
	
	@Query(value = "SELECT COUNT(*) FROM MYRE.MYRE_BOOKINGS WHERE BOOKING = :booking", nativeQuery = true)
	String bookingValidation(String booking);

}
