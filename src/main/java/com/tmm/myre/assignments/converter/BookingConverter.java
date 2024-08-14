package com.tmm.myre.assignments.converter;



import org.springframework.stereotype.Component;

import com.tmm.myre.assignments.dto.BookingDto;
import com.tmm.myre.assignments.model.BookingModel;
import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;

@Component("bookingConverter")
public class BookingConverter implements IConverter<BookingModel, BookingDto>{

	@Override
	public BookingModel convert(BookingDto to) throws ConverterException {
		BookingModel entity = BookingModel.builder()
				.bookingId(to.getBookingId())
				.type(to.getType())
				.size(to.getSize())
				.booking(to.getBooking())
				.status(to.getStatus())
				.quality(to.getQuality())
				.shippingCompany(to.getShippingCompany())
				.quantityUnits(to.getQuantityUnits())
				.quantityUnitsUse(to.getQuantityUnitsUse())
				.finalClient(to.getFinalClient())
				.billTo(to.getBillTo())
				.location(to.getLocation())
				.workOrder(to.getWorkOrder())
				.expirationDate(to.getExpirationDate())
				.releaseDate(to.getReleaseDate())
				.creationDate(to.getCreationDate())
				.build();
		return entity;
	}

	@Override
	public BookingDto convert(BookingModel entity) throws ConverterException {
		BookingDto to = BookingDto.builder()
				.bookingId(entity.getBookingId())
				.type(entity.getType())
				.size(entity.getSize())
				.booking(entity.getBooking())
				.status(entity.getStatus())
				.quality(entity.getQuality())
				.shippingCompany(entity.getShippingCompany())
				.quantityUnits(entity.getQuantityUnits())
				.quantityUnitsUse(entity.getQuantityUnitsUse())
				.finalClient(entity.getFinalClient())
				.billTo(entity.getBillTo())
				.location(entity.getLocation())
				.workOrder(entity.getWorkOrder())
				.expirationDate(entity.getExpirationDate())
				.releaseDate(entity.getReleaseDate())
				.creationDate(entity.getCreationDate())
				.build();
		return to;
	}

}
