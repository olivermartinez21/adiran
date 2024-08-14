package com.tmm.myre.appointments.service.core;

import java.sql.Date;
import java.util.List;
import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;

public interface IAppointmentService {

	ResponseManagement saveAppointment(AppointmentDto appointmentDto);

	List<AppointmentDto> getDataTable(Integer userId)  throws ConverterException ;

	AppointmentDto getSingleData(String appointmentId,Integer userId,String warehouse)  throws ConverterException;

	ResponseManagement updateAppointment(AppointmentDto appointmentDto);

	ResponseManagement appointmentValidation(String appointmentId);

	List<AppointmentDto> getDataTableByDate(Date startDate, Date lastDate , String warehouse) throws ConverterException ;

	List<AppointmentDto> getDataTable(Integer userId, String warehouse,String role) throws ConverterException;

	AppointmentDto getOne(String appointmentId) throws ConverterException;

	AppointmentDto getAppointmentData(String containerId)throws ConverterException;


}
