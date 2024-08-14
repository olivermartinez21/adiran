package com.tmm.myre.assignments.service.core;

import java.util.List;

import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.assignments.dto.BookingDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.deliveryOrders.dto.DeliveryOrderDto;

public interface IBookingService {

	List<BookingDto> getDataTable(String warehouse) throws ConverterException ;

	ResponseManagement createBooking(BookingDto bookingDto) throws ConverterException;



}
