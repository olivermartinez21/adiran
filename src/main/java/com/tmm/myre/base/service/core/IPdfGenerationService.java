/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.service.core;

import java.util.List;

import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.model.InspectionModel;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 11 ago. 2021
 * 
 */
public interface IPdfGenerationService {
	
	/**
	 * @param certificateEntryDto
	 * @param entryInfo
	 * @param clientInfo
	 * @param fiscalInfo
	 * @return
	 */
	byte[] pdfCita(AppointmentDto appointmentDto,List<ContainerDto>  containers);

	byte[] pdfEir(String containerId, String dataUrl);

	byte[] pdfOrder(DeliveryOrderModel deliveryOrderId);

	byte[] pdfEirOut(String containerId, String dataUrl);

	byte[] pdfQuote(String containerId, List<InspectionModel> inspectionDto);
}
