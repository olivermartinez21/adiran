package com.tmm.myre.appointments.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;

@Component("appointmentConverter")
public class AppointmentConverter implements IConverter<AppointmentModel, AppointmentDto>{

	@Override
	public AppointmentModel convert(AppointmentDto to) throws ConverterException {
		AppointmentModel entity = AppointmentModel.builder()
				.appointmentId(to.getAppointmentId())
				.location(to.getLocation())
				.folio(to.getFolio())
				.date(to.getDate())
				.creationAppointmentDate(to.getCreationAppointmentDate())
				.telephone(to.getTelephone())
				.agency(to.getAgency())
				.customer(to.getCustomer())
				.customerType(to.getCustomerType())
				.containers(to.getContainers())
				.containersType(to.getContainersType())
				.shippingCompany(to.getShippingCompany())
				.invoincingType(to.getInvoincingType())
				.eventType(to.getEventType())
				.paymentType(to.getPaymentType())
				.companyName(to.getCompanyName())
				.rfc(to.getRfc())
				.fiscalAdress(to.getFiscalAdress())
				.user(to.getUser())
				.buque(to.getBuque())
				.origin(to.getOrigin())
				//.signature(to.getSignature())
				.status(to.getStatus())
				.paymentCheck(to.getPaymentCheck())
				.fileName(to.getFileName())
				.fileType(to.getFileType())
				.fileContent(to.getFileContent())
				.build();
		return entity;
	}

	@Override
	public AppointmentDto convert(AppointmentModel entity) throws ConverterException {
		AppointmentDto to = AppointmentDto.builder()
				.appointmentId(entity.getAppointmentId())
				.location(entity.getLocation())
				.folio(entity.getFolio())
				.date(entity.getDate())
				.creationAppointmentDate(entity.getCreationAppointmentDate())
				.telephone(entity.getTelephone())
				.agency(entity.getAgency())
				.customer(entity.getCustomer())
				.customerType(entity.getCustomerType())
				.containers(entity.getContainers())
				.containersType(entity.getContainersType())
				.shippingCompany(entity.getShippingCompany())
				.invoincingType(entity.getInvoincingType())
				.eventType(entity.getEventType())
				.paymentType(entity.getPaymentType())
				.companyName(entity.getCompanyName())
				.rfc(entity.getRfc())
				.fiscalAdress(entity.getFiscalAdress())
				.user(entity.getUser())
				.buque(entity.getBuque())
				.origin(entity.getOrigin())
				//.signature(entity.getSignature())
				.status(entity.getStatus())
				.paymentCheck(entity.getPaymentCheck())
				.fileName(entity.getFileName())
				.fileType(entity.getFileType())
				.fileContent(entity.getFileContent())
				.build();
		return to;
	}


}
