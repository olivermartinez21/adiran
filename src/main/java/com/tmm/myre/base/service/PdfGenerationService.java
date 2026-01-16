/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tmm.myre.assignments.dto.PreOrderDeliveryRequestDto;
import com.tmm.myre.assignments.model.BookingModel;
import com.tmm.myre.assignments.repository.IBookingRepository;
import com.tmm.myre.catalog.model.CatComponentModel;
import com.tmm.myre.catalog.model.CatDamageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatComponentRepository;
import com.tmm.myre.catalog.repository.ICatDamageRepository;
import com.tmm.myre.catalog.repository.ICatRepairRepository;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.model.InspectionModel;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.quote.model.QuoteModel;
import com.tmm.myre.quote.repository.IQuoteRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 11 ago. 2021
 * 
 */

@Slf4j
@Service("pdfGenerationService")
public class PdfGenerationService implements IPdfGenerationService {
	
	
	@Autowired
	private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;
	
	@Autowired
	private IContainerRepository containerRepository;
	
	@Autowired
	private IInspectionRepository inspectionRepository;
	
	@Autowired
	private ICatComponentRepository catComponentRepository;
	
	@Autowired
	private ICatDamageRepository catDamageRepository;
	
	@Autowired
	private ICatRepairRepository catRepairRepository;
	
	@Autowired
	private IQuoteRepository quoteRepository;
	
	@Autowired
	private IAssignmentRepository assignmentRepository;
    @Autowired
    private IBookingRepository bookingRepository;


	@Override
	public byte[] pdfCita(AppointmentDto appointmentDto,List<ContainerDto> containers) {
	
	Document document = new Document(PageSize.A4, 15f, 15f, 30f, 25f);
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	Image logo = null;
	
	try {
		byte[] imagenAdemsa = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/LogoTMMLogistics.png").toURI()));
		logo = Image.getInstance(imagenAdemsa);
	} catch(Exception ex) {
		log.error(ex.toString());
	}
	
		try {
			/***********************************************************************************************************************/
			PdfWriter.getInstance(document, out);
			Font bold = new Font(FontFamily.COURIER, 9, Font.BOLD,BaseColor.WHITE);
			Font regularBlack = new Font(FontFamily.COURIER, 10f, Font.BOLD);	
			Font regularWhite = new Font(FontFamily.COURIER, 10f, Font.BOLD,BaseColor.WHITE);	
			
            
            document.open();
            
            /***********************************************************************************************************************/

            PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,4,2});
			
			
			PdfPCell hcell = new PdfPCell();
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.addElement(logo);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			Phrase p = new Phrase("Carretera Panamericana km 74 Sur Parque industrial ALTEC,\r\n"
					+ "Aguascalientes, Aguascalientes, C.P. 20290 TEL: (44) 9971 1252\r\n"
					+ "", bold);
			
			hcell = new PdfPCell(p);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell();
			p = new Phrase("NO° CITA: "+appointmentDto.getFolio(), bold);
			hcell = new PdfPCell(p);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(hcell);
			
		
			document.add(table);
            /***********************************************************************************************************************/
			
			document.add(new Paragraph(" \n"
					+ " "));
			//document.add(new Paragraph(" "));
			
			table = new PdfPTable(4);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,(float) 2.5, (float) 3.2,2});
			//---------------------------------------------------------------------------------------
			
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			
			hcell = new PdfPCell(new Phrase("FOLIO: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(""+appointmentDto.getFolio(), regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			//document.add(table);
			//---------------------------------------------------------------------------------------
			
			/*hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("SALDO A FAVOR: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);*/
			
			//---------------------------------------------------------------------------------------
			
		/*	hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("MONTO: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);*/
			
			//---------------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("TIPO DE MOVIMIENTO: \n ", regularWhite));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(appointmentDto.getEventType() == 1 ? "ENTRADA" :  "SALIDA", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			//---------------------------------------------------------------------------------------
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("CLIENTE: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(""+appointmentDto.getCompanyName(), regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			//---------------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("AGENTE ADUANAL: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(""+appointmentDto.getAgency(), regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			//---------------------------------------------------------------------------------------
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("TRANSPORTE: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(""+appointmentDto.getCustomer(), regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			//---------------------------------------------------------------------------------------
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("FECHA DE DESCARGA: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(""+appointmentDto.getDate(), regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			//---------------------------------------------------------------------------------------
			/*hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("FECHA DE VIGENCIA: \n ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(""+appointmentDto.getDate(),regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("  ", regularBlack));
			//hcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);*/
			
			document.add(table);
			
			
			document.add(new Paragraph(" \n \n \n \n"));
			 /***********************************************************************************************************************/
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});
			
			hcell = new PdfPCell(new Phrase("UNIDADES", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			document.add(table);
			
			 /***********************************************************************************************************************/
			
			
			table = new PdfPTable(5);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2,2,2,2});
			
			hcell = new PdfPCell(new Phrase("NO° ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("UNIDAD ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("NAVIERA ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("TIPO ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("TAMAÑO ", regularWhite));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			//---------------------------------------------------------------------------------------
			int contador=1;
			for(ContainerDto container : containers){
				
				hcell = new PdfPCell(new Phrase(""+contador++, regularBlack));
				//hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(container.getContainer(), regularBlack));
				//hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(catShippingCompanyReposirtory.getById(container.getShippingCompany()).getDescription(), regularBlack));
				//hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table.addCell(hcell);

				hcell = new PdfPCell(new Phrase( container.getContainerType() == 3 ? "DC" :container.getContainerType() == 6 ? "RF"  :  container.getContainerType() == 4 ?  "--" : "GS" ));
				//hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(container.getContainerSize(), regularBlack));
				//hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table.addCell(hcell);
				
			}
			document.add(table);
			
			 /***********************************************************************************************************************/
			
		document.close();
		
		
		
		
		} catch (DocumentException ex) {
		        	log.error(ex.toString());
		}
		return out.toByteArray();
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override
	public byte[] pdfEir(String containerId , String dataUrl) {
	
	Document document = new Document(PageSize.A4, 15f, 15f, 30f, 25f);
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	Image logo = null;
	Image eirDry = null;
	Image firma = null;
	ContainerModel container  = containerRepository.getById(containerId) ;
	
	
	
	try {
		byte[] imagenAdemsa = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/LogoTMMLogisticsPixel.png").toURI()));
		logo = Image.getInstance(imagenAdemsa);
		byte[] iagenEirDry = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/EIR DRY.png").toURI()));
		eirDry = Image.getInstance(iagenEirDry);
		if(!dataUrl.isEmpty()) {
			String base64Image = dataUrl.split(",")[1];
			byte[] imageFirma = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			firma = Image.getInstance(imageFirma);
		}
	} catch(Exception ex) {
		log.error(ex.toString());
	}
	
	
		try {
			/***********************************************************************************************************************/
			PdfWriter.getInstance(document, out);
			Font bold = new Font(FontFamily.COURIER, 7.5f, Font.BOLD);
			Font regularBlack = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);	
			
			Font regular = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);	
			Font regularWhite = new Font(FontFamily.COURIER, 10f, Font.BOLD,BaseColor.WHITE);	
			
			
			 document.open();
			 
            
            /***********************************************************************************************************************/
           
            PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,4,2});
			
			
			PdfPCell hcell = new PdfPCell();
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.addElement(logo);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			Phrase p = new Phrase("Carretera Panamericana km 74 Sur Parque industrial ALTEC \r\n"
					+ "Aguascalientes, Aguascalientes C.P. 20290. TEL: (44) 9971 1252\r\n", regularBlack);
			
			hcell = new PdfPCell(p);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell();
			p = new Phrase("NO° "+container.getEirName(), regularBlack);
			hcell = new PdfPCell(p);
			hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(hcell);
			
		
			document.add(table);
            /***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});
			
			hcell = new PdfPCell(new Phrase("RECIBO DE INTERCAMBIO DE EQUIPO ENTRADA", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			document.add(table);
			
			 /***********************************************************************************************************************/
			table = new PdfPTable(4);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2,2,2});
			
			hcell = new PdfPCell(new Phrase("Equipo: "+container.getContainer(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Lugar: "+container.getLocation(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Propietario: "+ catShippingCompanyReposirtory.getById(container.getShippingCompany()).getDescription(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Fecha: "+DateManagement.getTodayTimestamp(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-----------------------------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("Tamaño: "+container.getContaierSize(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Tipo de Servicio: "+container.getTypeServicePregate(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Origen: "+container.getOriginPregate(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Cliente Final:", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-----------------------------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("Calidad: "+(container.getClasification().equals("1") ? "A":container.getClasification().equals("2") ? "B": container.getClasification().equals("3") ? "C": 
				container.getClasification().equals("4") ? "BL":container.getClasification().equals("5") ? "D":container.getClasification().equals("6") ? "FS":container.getClasification().equals("7") ? "FX":" "), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Tipo de equipo: "+ (container.getContainerType() == 1 ? " CH":container.getContainerType() == 2 ? " OP":container.getContainerType() == 3 ? " DC": container.getContainerType()== 4 ? "GS":
								container.getContainerType()== 5 ? "IS":container.getContainerType() == 6 ? " RF":container.getContainerType() == 7 ? " HC": " " ), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Condicion: "+ (container.getCondition().equals("1")  ? "DISPONIBLE":container.getCondition().equals("2")  ? "DAÑADO":container.getCondition().equals("3")  ? "DAÑADO/PTI ":
				container.getCondition().equals("4")  ? "EVACUACION":container.getCondition().equals("5")  ? "PTI":container.getCondition().equals("6")  ? "BLOQUEADO/GX":
					container.getCondition().equals("7")  ? "TOTAL LOOS":container.getCondition().equals("8")  ? "VENTA":container.getCondition().equals("9")  ? "ACCIDENTADO":" "), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("Cobrar a: "+container.getBillTo(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-----------------------------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("Buque: "+container.getBuque(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-----------------------------------------------------------------------------------------------------
			
			document.add(table);
			 /***********************************************************************************************************************/
			
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});
			
			hcell = new PdfPCell(new Phrase("TIPO DE DAÑO", regularBlack));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			document.add(table);
			
			 /***********************************************************************************************************************/
			table = new PdfPTable(4);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2,2,2});
			
			hcell = new PdfPCell(new Phrase("HUNDIDO (PI)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("ABOMBADO (PO)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("OLOR (OR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("RASPADO (SC)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("CORATDO (C)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("PERFORADO (F)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("DOBLADO (B)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("DESPRENDIDO (MN)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("ROTO (BR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("FLOJO (L)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("TORCIDO (BT)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("RAJADO (CR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("FALTANTE (M)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("OXIDADO (R)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("SUCIO (DY)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("REPARACION IMPROPIA (IR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			//-------------------------------------------------------------------------------
			
			document.add(table);
			
			/***********************************************************************************************************************/
			PdfPTable table1 = new PdfPTable(4);
			table1.setWidthPercentage(100);
			table1.setWidths(new float[] {2,2,2,2});
			
			//-------------------------------------------------------------------------------

				log.info("El contenedor si necsita mostrar inspecciones" + containerId);
				List<InspectionModel> listInspections = inspectionRepository.getAllInspectionsByContainerId(containerId);

				int contador = 1;
				for(InspectionModel inspection : listInspections) {

					log.info(inspection.toString()+" ---------------- Esta escribiendo--------");

					hcell = new PdfPCell(new Phrase(contador+".-"+ "SECCION", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBackgroundColor(BaseColor.YELLOW);
					table1.addCell(hcell);
					contador++;
					hcell = new PdfPCell(new Phrase("COMPONENTE", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBackgroundColor(BaseColor.YELLOW);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("DAÑO", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBackgroundColor(BaseColor.YELLOW);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("LOCALIZACION", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBackgroundColor(BaseColor.YELLOW);
					table1.addCell(hcell);

					//-------------------------------------------------------------------------------

					hcell = new PdfPCell(new Phrase(inspection.getPart()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(catComponentRepository.getById(inspection.getComponent()).getComponent()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(catDamageRepository.getById(inspection.getDamage().toString()).getDescription()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getLocation()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					//-------------------------------------------------------------------------------

					hcell = new PdfPCell(new Phrase("MÉTODO DE REPARACIÓN" , bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("MEDIDAS", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("RESPONSABILIDAD", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("COMENTARIOS", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					//-------------------------------------------------------------------------------

					hcell = new PdfPCell(new Phrase(catRepairRepository.getById(inspection.getRepair()).getRepairDescription()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					String unidadMedida = null;

					switch (inspection.getExtentOtherLarge()) {
						case 1:
							unidadMedida = "mm";
							break;
						case 2:
							unidadMedida = "cm";
							break;
						case 3:
							unidadMedida = "mts";
							break;
						case 4:
							unidadMedida = "pza";
							break;
						default:
							break;
					}

					hcell = new PdfPCell(new Phrase("Largo: "+inspection.getLength()+unidadMedida+", Ancho: "+ inspection.getWidth()+unidadMedida+ ", Profundo: "+inspection.getDepth()+unidadMedida+
							" Alto: "+inspection.getOtherLength()+unidadMedida +" Cantidad: " + inspection.getQuantity() , regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);


					hcell = new PdfPCell(new Phrase(inspection.getCustomerName(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getReference()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					log.info(" ---------------- termino escribiendo--------");
				}
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("TOTAL" , regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(hcell);


			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(hcell);
			
			
			//-------------------------------------------------------------------------------
			
			 /***********************************************************************************************************************/
			
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {(float) 2.5,6});
			
			
			
			hcell = new PdfPCell();
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.addElement(eirDry);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell();
			hcell.addElement(table1);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			document.add(table);
			
			
			 /***********************************************************************************************************************/
			
			
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});
			
			hcell = new PdfPCell(new Phrase("Certifico que el equipo arriba mencionado fue inspeccionado según se describe y que los resultados de dicha inspección son verdaderos. La parte que recibe "
					+ "acepta la responsabilidad, el cuidado y control de dicho equipo debiéndolo entregar en las mismas condiciones, salvo el buen uso y desgaste normal, "
					+ "reconociendo su responsabilidad por cualquier daño y/o pérdida ocacionada, compromentiéndose a reembolsar en su totalidad el monto en cuestión."
					+ "\n", regular));
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			document.add(table);
			
			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {4,4});
			
			hcell = new PdfPCell(new Phrase("ENTREGA: Este equipo fue entregado en buenas condiciones excepto lo anotado.", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("RECIBE: Este equipo fue recibido en buenas condiciones excepto lo anotado.", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			document.add(table);
			
			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n \n \n"));
			
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2});
			
			if(!dataUrl.isEmpty()) {
				//firma.scalePercent(50);
				hcell = new PdfPCell();
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.addElement(firma);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
			}
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("___________________________", regular));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("___________________________", regular));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("NOMBRE Y FIRMA DEL PERSONAL", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("NOMBRE/FIRMA/EMPRESA: " + container.getOperatorName(), regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			//-------------------------------------------------------------------------------
			
			
			document.add(table);
			
			/***********************************************************************************************************************/
		document.close();
		
		
		
		
		} catch (DocumentException ex) {
		        	log.error(ex.toString());
		} 
		return out.toByteArray();
}

	@Override
	public byte[] pdfOrder(DeliveryOrderModel order) {
		
		Document document = new Document(PageSize.A4, 15f, 15f, 30f, 25f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Image logo = null;
		log.info(order.getAssignmentId()+"Paso 1----------------");
		
		try {
			byte[] imagenAdemsa = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/LogoTMMLogisticsPixel.png").toURI()));
			logo = Image.getInstance(imagenAdemsa);
		} catch(Exception ex) {
			log.error(ex.toString());
		}
		log.info("Paso 1----------------" + order.getDeliveryOrderId());
		AssignmentModel assignmentSearch = assignmentRepository.getAssignment(order.getDeliveryOrderId());
		log.info("Paso 2---------------" + assignmentSearch.toString());
		log.info(assignmentSearch.getUnitNumber() + "Numero de unidad----------------");
		

		ContainerModel containerList = containerRepository.getPreLabor(assignmentSearch.getUnitNumber());
		
		try {
			
			PdfWriter.getInstance(document, out);
			Font bold = new Font(FontFamily.COURIER, 7.5f, Font.BOLD);
			Font regularBlack = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);	
			
			Font regular = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);	
			Font regularWhite = new Font(FontFamily.COURIER, 10f, Font.BOLD,BaseColor.WHITE);	
			
			Font tiltle = new Font(FontFamily.COURIER, 12f, Font.NORMAL);	
			
			log.info("Paso 3---------------");
			 document.open();
			 
            
            /***********************************************************************************************************************/
			 PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {2,2});
				
				logo.scalePercent(50);
				PdfPCell hcell = new PdfPCell();
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.addElement(logo);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				log.info("Paso 4---------------");
				Phrase p = new Phrase("ORDEN: "+order.getFileName(), tiltle);
				hcell = new PdfPCell(p);
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(hcell);
				
			
				document.add(table);
	            /***********************************************************************************************************************/
				document.add(new Paragraph(" \n"));
				table = new PdfPTable(1);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {2});
				
				log.info("Paso 5---------------");
				hcell = new PdfPCell(new Phrase("LOCALIDAD: "+order.getLocation(), tiltle));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				
				document.add(table);
				/***********************************************************************************************************************/
				document.add(new Paragraph(" \n"));
				document.add(new Paragraph(" \n"));
				table = new PdfPTable(1);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {2});
				
				hcell = new PdfPCell(new Phrase("ORDEN DE ENTREGA DE UNIDADES VACIAS ", tiltle));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				document.add(table);
				
				/***********************************************************************************************************************/
				document.add(new Paragraph(" \n"));
				table = new PdfPTable(2);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {2,2});
				
				hcell = new PdfPCell(new Phrase("BOOKING: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getBooking(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("PROPIETARIO: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getOwner(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("TIPO DE SERVICIO: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getTypeOfService(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("UNIDADES RESTANTES: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getRemainingUnits(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("COBRAR A: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getBillTo(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("EMPRESA TRANSPORTISTA: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getCarrierCompany(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------

				String contType = null;
				
				log.info("Switch------------------1");
				switch (containerList.getContainerType()) {
				case 1:
					contType = "CH";
					break;
				case 2:
					contType = "OT";
					break;
				case 3:
					contType = "DC";
					break;
				case 4:
					contType = "GS";
					break;
				case 5:
					contType = "IMO";
					break;
				case 6:
					contType = "RF";
					break;
				case 7:
					contType = "HC";
					break;

				}
				
				hcell = new PdfPCell(new Phrase("TIPO DE UNIDAD: " , regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+ contType, regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
//-------------------------------------------------------------------------------------
			 
				hcell = new PdfPCell(new Phrase("TAMAÑO: " , regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+ containerList.getContaierSize(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				String descripcionEstado = null;
				
				log.info("Switch------------------2");
				switch (containerList.getCondition()) {
	            case "1":
	                descripcionEstado = "DISPONIBLE";
	                break;
	            case "2":
	                descripcionEstado = "DAÑADO";
	                break;
	            case "3":
	                descripcionEstado = "DAÑADO/PTI";
	                break;
	            case "4":
	                descripcionEstado = "EVACUACION";
	                break;
	            case "5":
	                descripcionEstado = "PPTI";
	                break;
	            case "6":
	                descripcionEstado = "BLOQUEADO/GX";
	                break;
	            case "7":
	                descripcionEstado = "TOTAL LOOS";
	                break;
	            case "8":
	                descripcionEstado = "VENTA";
	                break;
	            case "9":
	                descripcionEstado = "ACCIDENTADO";
	                break;
	            default:
	                descripcionEstado = "Estado inválido";
	                break;
	        }

			String descripcionClasificacion = null;


			switch (containerList.getClasification()) {
				case "1":
					descripcionClasificacion = "A";
					break;
				case "2":
					descripcionClasificacion = "B";
					break;
				case "3":
					descripcionClasificacion = "C";
					break;
				case "4":
					descripcionClasificacion = "BL";
					break;
				case "5":
					descripcionClasificacion = "D";
					break;
				case "6":
					descripcionClasificacion = "FS";
					break;
				case "7":
					descripcionClasificacion = "FX";
					break;
				default:
					descripcionClasificacion = "Estado inválido";
					break;
			}
				
				hcell = new PdfPCell(new Phrase("GRADO-CALIDAD: " , regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("" + descripcionEstado+" "+descripcionClasificacion, regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------: 
				
				
				hcell = new PdfPCell(new Phrase("OPERADOR: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getOperator(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("WORK ORDER: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getWorkOrder(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("ECONOMICO: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getEconomicNumber(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("UNIDADES A ENTREGAR: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(""+order.getQuantityOfUnits(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("UNIDADES PREASIGNADAS: ", regularWhite));
		
				hcell.setBackgroundColor(BaseColor.DARK_GRAY);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("" + containerList.getContainer(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
//-------------------------------------------------------------------------------------
				
				
				document.add(table);
				
				/***********************************************************************************************************************/
				
			document.close();
			
		} catch (Exception ex) {
			log.error(ex.toString());
		}
		return out.toByteArray();
	}

	@Override
	public byte[] pdfPreOrderDelivery(PreOrderDeliveryRequestDto request) {

		Document document = new Document(PageSize.A4, 15f, 15f, 30f, 25f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Image logo = null;
		log.info(request.getAssignmentId()+"Paso 1----------------");

		try {
			byte[] imagenAdemsa = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/LogoTMMLogisticsPixel.png").toURI()));
			logo = Image.getInstance(imagenAdemsa);
		} catch(Exception ex) {
			log.error(ex.toString());
		}
		log.info("Paso 1----------------");
		AssignmentModel assignmentSearch = assignmentRepository.getAssignmentById(request.getAssignmentId());

		log.info(assignmentSearch.getUnitNumber() + "Numero de unidad----------------");

		log.info("Paso 2---------------" + assignmentSearch.getUnitNumber());
		ContainerModel containerList = containerRepository.getPreLabor(assignmentSearch.getUnitNumber());

		try {

			PdfWriter.getInstance(document, out);
			Font bold = new Font(FontFamily.COURIER, 7.5f, Font.BOLD);
			Font regularBlack = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);

			Font regular = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);
			Font regularWhite = new Font(FontFamily.COURIER, 10f, Font.BOLD,BaseColor.WHITE);

			Font tiltle = new Font(FontFamily.COURIER, 12f, Font.NORMAL);

			log.info("Paso 3---------------");
			document.open();


			/***********************************************************************************************************************/
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2});

			logo.scalePercent(50);
			PdfPCell hcell = new PdfPCell();
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.addElement(logo);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			log.info("Paso 4---------------");
			Phrase p = new Phrase("ORDEN: SIN VALIDES", tiltle);
			hcell = new PdfPCell(p);
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(hcell);


			document.add(table);
			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});

			log.info("Paso 5---------------");
			hcell = new PdfPCell(new Phrase("LOCALIDAD: AGUASCALIENTES", tiltle));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);


			document.add(table);
			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			document.add(new Paragraph(" \n"));
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});

			hcell = new PdfPCell(new Phrase("PRE ORDEN DE ENTREGA DE UNIDADES VACIAS ", tiltle));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			document.add(table);

			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2});

			hcell = new PdfPCell(new Phrase("BOOKING: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getBookingOrder(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("PROPIETARIO: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getShippingCompany(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("TIPO DE SERVICIO: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getTypeServiceOrder(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------

			//Descomentar si se quiere mostrar las unidades restantes
			/*hcell = new PdfPCell(new Phrase("UNIDADES RESTANTES: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+assignmentSearch.get, regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);*/
//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("COBRAR A: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getBillOrderTo(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("EMPRESA TRANSPORTISTA: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getCarrierCompanyOrder(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------

			String contType = null;

			log.info("Switch------------------1");
			switch (assignmentSearch.getType()) {
				case "1":
					contType = "CH";
					break;
				case "2":
					contType = "OT";
					break;
				case "3":
					contType = "DC";
					break;
				case "4":
					contType = "GS";
					break;
				case "5":
					contType = "IMO";
					break;
				case "6":
					contType = "RF";
					break;
				case "7":
					contType = "HC";
					break;

			}

			hcell = new PdfPCell(new Phrase("TIPO DE UNIDAD: " , regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+ contType, regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("TAMAÑO: " , regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+ assignmentSearch.getSize(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------
			String descripcionEstado = null;

			log.info("Switch------------------2");
			switch (assignmentSearch.getQuality()) {
				case "1":
					descripcionEstado = "A";
					break;
				case "2":
					descripcionEstado = "B";
					break;
				case "3":
					descripcionEstado = "C";
					break;
				case "4":
					descripcionEstado = "BL";
					break;
				case "5":
					descripcionEstado = "D";
					break;
				case "6":
					descripcionEstado = "FS";
					break;
				case "7":
					descripcionEstado = "FX";
					break;
				default:
					descripcionEstado = "Estado inválido";
					break;
			}

			hcell = new PdfPCell(new Phrase("Grado de la unidad: " , regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("" + descripcionEstado, regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------:


			hcell = new PdfPCell(new Phrase("OPERADOR: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getOperatorOrder(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("WORK ORDER: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getWorkOrderOrder(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("ECONOMICO: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+request.getEconomicNumberOrder(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------

			//Descomentar si se quiere mostrar las unidades a entregar
			/*hcell = new PdfPCell(new Phrase("UNIDADES A ENTREGAR: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(""+order.getQuantityOfUnits(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);*/
//-------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("UNIDADES PREASIGNADAS: ", regularWhite));

			hcell.setBackgroundColor(BaseColor.DARK_GRAY);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("" , regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
//-------------------------------------------------------------------------------------


			document.add(table);

			/***********************************************************************************************************************/

			document.close();

		} catch (Exception ex) {
			log.error(ex.toString());
		}
		return out.toByteArray();
	}

	@Override
	public byte[] pdfEirOut(String containerId , String dataUrl) {
	
	Document document = new Document(PageSize.A4, 15f, 15f, 30f, 25f);
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	Image logo = null;
	Image eirDry = null;
	Image firma = null;
	ContainerModel container  = containerRepository.getById(containerId) ;
	
	
	
	try {
		byte[] imagenAdemsa = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/LogoTMMLogisticsPixel.png").toURI()));
		logo = Image.getInstance(imagenAdemsa);
		byte[] iagenEirDry = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/EIR DRY.png").toURI()));
		eirDry = Image.getInstance(iagenEirDry);
		if(!dataUrl.isEmpty()) {
			String base64Image = dataUrl.split(",")[1];
			byte[] imageFirma = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			firma = Image.getInstance(imageFirma);
		}
	} catch(Exception ex) {
		log.error(ex.toString());
	}
	
	
		try {
			/***********************************************************************************************************************/
			PdfWriter.getInstance(document, out);
			Font bold = new Font(FontFamily.COURIER, 7.5f, Font.BOLD);
			Font regularBlack = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);

			Font regular = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);
			Font regularWhite = new Font(FontFamily.COURIER, 10f, Font.BOLD,BaseColor.WHITE);


			 document.open();


            /***********************************************************************************************************************/

            PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,4,2});


			PdfPCell hcell = new PdfPCell();
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.addElement(logo);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			Phrase p = new Phrase("Carretera Panamericana km 74 Sur Parque industrial ALTEC \r\n"
					+ "Aguascalientes, Aguascalientes C.P. 20290. TEL: (44) 9971 1252\r\n", regularBlack);

			hcell = new PdfPCell(p);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell();
			p = new Phrase("NO° "+container.getEirOutName(), regularBlack);
			hcell = new PdfPCell(p);
			hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(hcell);


			document.add(table);
            /***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});

			hcell = new PdfPCell(new Phrase("RECIBO DE INTERCAMBIO DE EQUIPO SALIDA", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			document.add(table);

			 /***********************************************************************************************************************/
			table = new PdfPTable(4);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2,2,2});

			hcell = new PdfPCell(new Phrase("Equipo: "+container.getContainer(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Lugar: "+container.getLocation(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Propietario: "+ catShippingCompanyReposirtory.getById(container.getShippingCompany()).getDescription(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Fecha: "+DateManagement.getTodayTimestamp(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-----------------------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("Tamaño: "+container.getContaierSize(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Tipo de Servicio: "+container.getTypeServicePregate(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Origen: "+container.getOriginPregate(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Cliente Final:" + container.getBillTo(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-----------------------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("Calidad: "+(container.getClasification().equals("1") ? "A":container.getClasification().equals("2") ? "B": container.getClasification().equals("3") ? "C":
				container.getClasification().equals("4") ? "BL":container.getClasification().equals("5") ? "D":container.getClasification().equals("6") ? "FS":container.getClasification().equals("7") ? "FX":" "), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Tipo de equipo: "+ (container.getContainerType() == 1 ? " CH":container.getContainerType() == 2 ? " OP":container.getContainerType() == 3 ? " DC": container.getContainerType()== 4 ? "GS":
				container.getContainerType()== 5 ? "IS":container.getContainerType() == 6 ? " RF":container.getContainerType() == 7 ? " HC": " " ), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Condicion: "+ (container.getCondition().equals("1")  ? "DISPONIBLE":container.getCondition().equals("2")  ? "DAÑADO":container.getCondition().equals("3")  ? "DAÑADO/PTI ":
				container.getCondition().equals("4")  ? "EVACUACION":container.getCondition().equals("5")  ? "PTI":container.getCondition().equals("6")  ? "BLOQUEADO/GX":
					container.getCondition().equals("7")  ? "TOTAL LOOS":container.getCondition().equals("8")  ? "VENTA":container.getCondition().equals("9")  ? "ACCIDENTADO":" "), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			if(container.getCondition().equals("4")){
				hcell = new PdfPCell(new Phrase("Cobrar a: "+container.getDefinition(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
			} else{
				BookingModel booking = bookingRepository.findByBooking(container.getBokking());
				hcell = new PdfPCell(new Phrase("Cobrar a: "+booking.getBillTo(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
			}


			//-----------------------------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("Buque: "+container.getBuque(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			if(container.getCondition().equals("4")){
				hcell = new PdfPCell(new Phrase("Booking: N/A", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
			} else{
				BookingModel booking = bookingRepository.findByBooking(container.getBokking());
				hcell = new PdfPCell(new Phrase("Booking: " + booking.getBooking(), regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
			}


			hcell = new PdfPCell(new Phrase("Transportista: " + container.getTransportId(), regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-----------------------------------------------------------------------------------------------------

			document.add(table);
			 /***********************************************************************************************************************/

			document.add(new Paragraph(" \n"));

			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});

			hcell = new PdfPCell(new Phrase("TIPO DE DAÑO", regularBlack));
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			document.add(table);

			 /***********************************************************************************************************************/
			table = new PdfPTable(4);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2,2,2});

			hcell = new PdfPCell(new Phrase("HUNDIDO (PI)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("ABOMBADO (PO)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("OLOR (OR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("RASPADO (SC)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("CORATDO (C)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("PERFORADO (F)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("DOBLADO (B)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("DESPRENDIDO (MN)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("ROTO (BR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("FLOJO (L)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("TORCIDO (BT)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("RAJADO (CR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-------------------------------------------------------------------------------

			hcell = new PdfPCell(new Phrase("FALTANTE (M)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("OXIDADO (R)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("SUCIO (DY)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("REPARACION IMPROPIA (IR)", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);

			//-------------------------------------------------------------------------------

			document.add(table);

			/***********************************************************************************************************************/
			PdfPTable table1 = new PdfPTable(4);
			table1.setWidthPercentage(100);
			table1.setWidths(new float[] {2,2,2,2});
			//------------------------------------------------------------------------------
			List<InspectionModel> listInspections = inspectionRepository.getAllInspectionsByContainerId(containerId);
			if(container.getCondition().equals("1")){
				log.info("El contenedor no necsita mostrar inspecciones");
			}else {
				for(InspectionModel inspection : listInspections) {

					log.info(inspection.toString()+" ---------------- Esta escribiendo--------");

					hcell = new PdfPCell(new Phrase("SECCION", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("COMPONENTE", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("DAÑO", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("LOCALIZACION", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					//-------------------------------------------------------------------------------

					hcell = new PdfPCell(new Phrase(inspection.getPart()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(catComponentRepository.getById(inspection.getComponent()).getComponent()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(catDamageRepository.getById(inspection.getDamage().toString()).getDescription()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getLocation()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					//-------------------------------------------------------------------------------

					hcell = new PdfPCell(new Phrase("MÉTODO DE REPARACIÓN" , bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("MEDIDAS", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("RESPONSABILIDAD", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("COMENTARIOS", bold));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					//-------------------------------------------------------------------------------

					hcell = new PdfPCell(new Phrase(catRepairRepository.getById(inspection.getRepair()).getRepairDescription()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase("Largo: "+inspection.getLength()+", Ancho: "+ inspection.getWidth() + ", Profundo: " +
							inspection.getDepth() + " Largo: "+inspection.getOtherLength() , regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getCustomerType() == 1 ? "MERCHANT" : "CARRIER"+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getReference()+"\n ", regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(hcell);


				}

				hcell = new PdfPCell(new Phrase("TOTAL" , regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(hcell);
			}

			//-------------------------------------------------------------------------------


			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase(" ", regularBlack));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(hcell);
			
			
			//-------------------------------------------------------------------------------
			
			 /***********************************************************************************************************************/
			
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {(float) 2.5,6});
			
			
			
			hcell = new PdfPCell();
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.addElement(eirDry);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			hcell = new PdfPCell();
			hcell.addElement(table1);
			//hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(hcell);
			
			document.add(table);
			
			
			 /***********************************************************************************************************************/
			
			
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(1);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2});
			
			hcell = new PdfPCell(new Phrase("Certifico que el equipo arriba mencionado fue inspeccionado según se describe y que los resultados de dicha inspección son verdaderos. La parte que recibe "
					+ "acepta la responsabilidad, el cuidado y control de dicho equipo debiéndolo entregar en las mismas condiciones, salvo el buen uso y desgaste normal, "
					+ "reconociendo su responsabilidad por cualquier daño y/o pérdida ocacionada, compromentiéndose a reembolsar en su totalidad el monto en cuestión."
					+ "\n", regular));
			hcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(hcell);
			
			document.add(table);
			
			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n"));
			
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {4,4});
			
			hcell = new PdfPCell(new Phrase("ENTREGA: Este equipo fue entregado en buenas condiciones excepto lo anotado.", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("RECIBE: Este equipo fue recibido en buenas condiciones excepto lo anotado.", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			document.add(table);
			
			/***********************************************************************************************************************/
			document.add(new Paragraph(" \n \n \n"));
			
			table = new PdfPTable(2);
			table.setWidthPercentage(102);
			table.setWidths(new float[] {2,2});
			
			if(!dataUrl.isEmpty()) {
				//firma.scalePercent(50);
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				hcell = new PdfPCell();
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.addElement(firma);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
			}
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("___________________________", regular));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("___________________________", regular));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			
			
			//-------------------------------------------------------------------------------
			
			hcell = new PdfPCell(new Phrase("NOMBRE Y FIRMA DEL PERSONAL", regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			hcell = new PdfPCell(new Phrase("NOMBRE/FIRMA/EMPRESA:" + container.getOperatorName(), regularBlack));
			hcell.setBorder(Rectangle.NO_BORDER);
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			
			//-------------------------------------------------------------------------------
			
			
			document.add(table);
			
			/***********************************************************************************************************************/
		document.close();
		
		
		
		
		} catch (DocumentException ex) {
		        	log.error(ex.toString());
		} 
		return out.toByteArray();
}

	@Override
	public byte[] pdfQuote(String containerId, List<InspectionModel> inspections) {
		Document document = new Document(PageSize.A4, 15f, 15f, 30f, 25f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Image logo = null;
		ContainerModel container = containerRepository.getById(containerId);

		// Carga el logo
		try {
			byte[] logoBytes = Files.readAllBytes(
					Paths.get(getClass().getClassLoader()
							.getResource("static/images/LogoTMMLogisticsPixel.png").toURI()));
			logo = Image.getInstance(logoBytes);
		} catch (Exception ex) {
			// log.error("No se pudo cargar el logo: " + ex);
		}

		try {
			PdfWriter.getInstance(document, out);

			Font fontLabel      = new Font(Font.FontFamily.COURIER, 7.5f, Font.BOLD);
			Font fontValue      = new Font(Font.FontFamily.COURIER, 7.5f, Font.NORMAL);
			Font fontTitle      = new Font(Font.FontFamily.COURIER, 12f,  Font.BOLD);
			document.open();

			// --- Encabezado con logo y título ---
			PdfPTable encabezado = new PdfPTable(2);
			encabezado.setWidthPercentage(100);
			encabezado.setWidths(new float[]{2, 6});

			PdfPCell cellLogo = new PdfPCell();
			cellLogo.setBorder(Rectangle.NO_BORDER);
			if (logo != null) cellLogo.addElement(logo);
			encabezado.addCell(cellLogo);

			PdfPCell title = new PdfPCell(new Phrase("ESTIMADO DE REPARACION", fontTitle));
			title.setBorder(Rectangle.NO_BORDER);
			title.setHorizontalAlignment(Element.ALIGN_LEFT);
			encabezado.addCell(title);
			document.add(encabezado);
			document.add(new Paragraph("\n"));

			// Agrupar inspecciones por responsable
			Map<String, List<InspectionModel>> byResp = inspections.stream()
					.collect(Collectors.groupingBy(InspectionModel::getCustomerName));

			boolean firstGroup = true;
			for (Map.Entry<String, List<InspectionModel>> entry : byResp.entrySet()) {
				if (!firstGroup) {
					document.newPage();
					// repetir encabezado de documento en página nueva
					document.add(encabezado);
					document.add(new Paragraph("\n"));
				}
				firstGroup = false;

				String responsable = entry.getKey();

				// Título de responsable
				PdfPTable grpHdr = new PdfPTable(1);
				grpHdr.setWidthPercentage(100);
				PdfPCell ghCell = new PdfPCell(
						new Phrase("NÚMERO ESTIMADO: " + container.getQuoteName()+
								"\n FECHA ESTIMADO: "+container.getDateInspection().toString(), fontValue));
				ghCell.setBorder(Rectangle.NO_BORDER);
				ghCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				grpHdr.addCell(ghCell);
				document.add(grpHdr);
				document.add(new Paragraph("\n"));

				// Construir info contenedor y autorizaciones lado a lado
				PdfPTable wrapper = new PdfPTable(2);
				wrapper.setWidthPercentage(100);
				wrapper.setWidths(new float[]{6,6});

				// Izquierda: datos del contenedor (2 columnas)
				PdfPTable contInfo = new PdfPTable(2);
				contInfo.setWidthPercentage(100);
				contInfo.setWidths(new float[]{3,7});
				contInfo.addCell(cell("NÚMERO ESTIMADO:", fontLabel, null));
				contInfo.addCell(cell(container.getQuoteName(), fontValue, null));
				contInfo.addCell(cell("FECHA ESTIMADO:", fontLabel, null));
				contInfo.addCell(cell(container.getDateInspection().toString(), fontValue, null));
				contInfo.addCell(cell("CLIENTE:", fontLabel, null));
				contInfo.addCell(cell(container.getBillTo(), fontValue, null));
				contInfo.addCell(cell("PROPIETARIO:", fontLabel, null));
				contInfo.addCell(cell(mapShipping(container.getShippingCompany()), fontValue, null));
				contInfo.addCell(cell("FACTURAR A:", fontLabel, null));
				if ("CARRIER".equals(responsable)) {
					contInfo.addCell(cell(mapShipping(container.getShippingCompany()), fontValue, null));
				} else {
					contInfo.addCell(cell(responsable, fontValue, null));
				}
				contInfo.addCell(cell("UNIDAD:", fontLabel, null));
				contInfo.addCell(cell(container.getContainer(), fontValue, null));
				contInfo.addCell(cell("TAMAÑO:", fontLabel, null));
				contInfo.addCell(cell(container.getContaierSize(), fontValue, null));
				contInfo.addCell(cell("TIPO EQUIPO:", fontLabel, null));
				contInfo.addCell(cell("DC", fontValue, null));
				contInfo.addCell(cell("NOMENCLATURA:", fontLabel, null));
				contInfo.addCell(cell(container.getNomenclatura(), fontValue, null));
				contInfo.addCell(cell("EN TERMINAL:", fontLabel, null));
				contInfo.addCell(cell("Sí", fontValue, null));

				PdfPCell left = new PdfPCell();
				left.setBorder(Rectangle.NO_BORDER);
				left.addElement(contInfo);
				wrapper.addCell(left);

				// Derecha: autorizaciones
				PdfPTable authTable = new PdfPTable(1);
				authTable.setWidthPercentage(100);
				authTable.addCell(cell("FECHA DE AUTORIZACIÓN:", fontLabel, null));
				authTable.addCell(cell("", fontValue, null));
				authTable.addCell(cell("NÚMERO DE AUTORIZACIÓN:", fontLabel, null));
				authTable.addCell(cell("", fontValue, null));
				authTable.addCell(cell("FECHA INICIO DE REPARACIÓN:", fontLabel, null));
				authTable.addCell(cell("", fontValue, null));
				authTable.addCell(cell("FECHA FIN DE REPARACIÓN:", fontLabel, null));
				authTable.addCell(cell("", fontValue, null));

				PdfPCell right = new PdfPCell();
				right.setBorder(Rectangle.NO_BORDER);
				right.addElement(authTable);
				wrapper.addCell(right);

				document.add(wrapper);
				document.add(new Paragraph("\n"));

				// Tabla de detalles
				PdfPTable table = new PdfPTable(13);
				table.setWidthPercentage(100);
				table.setWidths(new float[]{2,1,2,4,1,1,1,1,1,2,2,2,1});
				addTableHeaders(table, fontLabel);

				List<String> horas    = new ArrayList<>();
				List<String> labor    = new ArrayList<>();
				List<String> material = new ArrayList<>();
				List<String> quoteSum = new ArrayList<>();
				List<String> exchange = new ArrayList<>();

				for (InspectionModel insp : entry.getValue()) {
					QuoteModel q = quoteRepository.getByInspectionId(insp.getInspectionId());
					addInspectionRow(table, insp, q, container, fontValue);
					horas.add(q.getHours());
					labor.add(computeLabor(insp, q));
					material.add(q.getMaterial());
					quoteSum.add(computeQuote(q));
					exchange.add(q.getExchange());
				}

				addTotalsRow(table, horas, labor, material, quoteSum, exchange, fontLabel);
				document.add(table);
			}

			document.close();
		} catch (Exception e) {
			// log.error("Error generando PDF: " + e);
		}

		return out.toByteArray();
	}

	private void addTableHeaders(PdfPTable t, Font font) {
		String[] titles = {"Daño","Codigo de localizacion","Componente",
				"Metodo de reparacion","Largo cm","Ancho cm","Area cm","Cantidad",
				"Horas","Labor","Material","Total","Moneda"};
		for (String title : titles) {
			PdfPCell h = new PdfPCell(new Phrase(title, font));
			h.setHorizontalAlignment(Element.ALIGN_CENTER);
			h.setBackgroundColor(BaseColor.GRAY);
			h.setBorder(Rectangle.NO_BORDER);
			t.addCell(h);
		}
	}

	private void addInspectionRow(PdfPTable t, InspectionModel insp,
								  QuoteModel q, ContainerModel c, Font font) {

		CatDamageModel damageModel = catDamageRepository.getById(String.valueOf(insp.getDamage()));
		CatComponentModel componentModel = catComponentRepository.getById(String.valueOf(insp.getComponent()));

		t.addCell(cell(damageModel.getDescription(), font));
		t.addCell(cell(insp.getLocation(), font));
		t.addCell(cell(componentModel.getComponent(), font));
		t.addCell(cell(q.getRepairDescription(), font));
		t.addCell(cell(insp.getLength(), font));
		t.addCell(cell(insp.getWidth(), font));
		t.addCell(cell(insp.getDepth(), font));
		t.addCell(cell(insp.getQuantity(), font));
		t.addCell(cell(q.getHours(), font));
		t.addCell(cell(computeLabor(insp, q), font));
		t.addCell(cell(q.getMaterial(), font));
		t.addCell(cell(computeQuote(q), font));
		t.addCell(cell(q.getExchange(), font));
	}

	private void addTotalsRow(PdfPTable t,
							  List<String> horas, List<String> labor,
							  List<String> material, List<String> quote,
							  List<String> exchange, Font font) {
		// 7 celdas vacías hasta TOTAL
		for (int i = 0; i < 7; i++) t.addCell(cell("", font));
		PdfPCell tot = new PdfPCell(new Phrase("TOTAL", font));
		tot.setHorizontalAlignment(Element.ALIGN_CENTER);
		tot.setBorder(Rectangle.NO_BORDER);
		t.addCell(tot);

		// Sumas con dos decimales
		t.addCell(cell(String.format("%.2f", sum(horas)), font, BaseColor.GRAY));
		t.addCell(cell(String.format("%.2f", sum(labor)), font, BaseColor.GRAY));
		t.addCell(cell(String.format("%.2f", sum(material)), font, BaseColor.GRAY));
		t.addCell(cell(String.format("%.2f", sum(quote)), font, BaseColor.GRAY));

		// Última moneda
		String lastEx = exchange.isEmpty() ? "" : exchange.get(exchange.size()-1);
		t.addCell(cell(lastEx, font, BaseColor.GRAY));
	}

	private PdfPCell cell(String text, Font font) {
		return cell(text, font, null);
	}
	private PdfPCell cell(String text, Font font, BaseColor bg) {
		PdfPCell c = new PdfPCell(new Phrase(text != null ? text : "", font));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setBorder(Rectangle.NO_BORDER);
		if (bg != null) c.setBackgroundColor(bg);
		return c;
	}

	private double sum(List<String> values) {
		return values.stream()
				.mapToDouble(v -> Double.parseDouble(v))
				.sum();
	}

	private String computeLabor(InspectionModel insp, QuoteModel q) {
		double base = Double.parseDouble(q.getLabor());
		return String.valueOf(base);
	}

	private String computeQuote(QuoteModel q) {
		double l = Double.parseDouble(q.getLabor());
		double m = Double.parseDouble(q.getMaterial());
		return String.format("%.2f",  l + m);
	}
	private String mapShipping(String code) {
		switch (code) {
			case "1": return "COSCO SHIPPING LINES MEXICO";
			case "2": return "MAERSK";
			case "3": return "HAPAG LLOYD";
			case "4": return "HAMBURG-SUD";
			case "5": return "MEDITERRANEAN SHIPPING CO";
			case "6": return "OCEAN NETWORK EXPRESS";
			case "7": return "CMA-CGM";
			case "8": return "CONTAINER SUDAMERICA";
			case "9": return "HANJIN SHIPPING CO";
			default:  return "";
		}
	}
	
	
	
	
}




	