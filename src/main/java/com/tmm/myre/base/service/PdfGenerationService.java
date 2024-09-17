/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
	private IQuoteRepository quoteRepositry;
	
	@Autowired
	private IAssignmentRepository assignmentRepository;
	
	
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
			List<InspectionModel> listInspections = inspectionRepository.getAllInspectionsByContainerId(containerId);
			
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
		log.info("Paso 1----------------");
		AssignmentModel assignmentSearch = assignmentRepository.getAssignment(order.getDeliveryOrderId());
		
		log.info(assignmentSearch.getUnitNumber() + "Numero de unidad----------------");
		
		log.info("Paso 2---------------");
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
				
				hcell = new PdfPCell(new Phrase(""+ containerList.getNomenclatura(), regularBlack));
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
				
				hcell = new PdfPCell(new Phrase("CONDICION: " , regularWhite));
		
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
			//------------------------------------------------------------------------------
			List<InspectionModel> listInspections = inspectionRepository.getAllInspectionsByContainerId(containerId);
			
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
		Image eirDry = null;
		Image firma = null;
		ContainerModel container  = containerRepository.getById(containerId) ;
		
		
		try {
			byte[] imagenAdemsa = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("static/images/LogoTMMLogisticsPixel.png").toURI()));
			logo = Image.getInstance(imagenAdemsa);
		} catch(Exception ex) {
			log.error(ex.toString());
		}
		
		
			try {
				/***********************************************************************************************************************/
				log.info("Si genero");
				PdfWriter.getInstance(document, out);
				Font bold = new Font(FontFamily.COURIER, 7.5f, Font.BOLD);
				Font regularBlack = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);	
				
				Font regular = new Font(FontFamily.COURIER, 7.5f, Font.NORMAL);	
				Font regularWhite = new Font(FontFamily.COURIER, 10f, Font.BOLD,BaseColor.WHITE);	
				
				Font regularBlackBig = new Font(FontFamily.COURIER, 26f, Font.NORMAL);
				
				 document.open();
				 
				 log.info("Abre Documento");
	            /***********************************************************************************************************************/
	            PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {2,6});
				
				
				PdfPCell hcell = new PdfPCell();
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.addElement(logo);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				Phrase p = new Phrase("ESTIMADO DE REPARACION", regularBlackBig);
				
				hcell = new PdfPCell(p);
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
			
				document.add(table);
	            /***********************************************************************************************************************/
				 document.add(new Paragraph(" \n"));
				table = new PdfPTable(2);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {6,6});
				
				//-------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				log.info("primera parte antes del get");
				hcell = new PdfPCell(new Phrase("NÚMERO DEL ESTIMADO:" + container.getQuoteName().toString(), regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				
				log.info("segunda parte antes del get");
				hcell = new PdfPCell(new Phrase("", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("FECHA DEL ESTIMADO:" + container.getDateInspection().toString(), regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				
				document.add(table);
				
				/***********************************************************************************************************************/
				PdfPTable table1 = new PdfPTable(2);
				table1.setWidthPercentage(100);
				table1.setWidths(new float[] {3,3});
				
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("CLIENTE:  " + container.getBillTo().toString() , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				
				String shipingCompany = null;
				
				switch (container.getShippingCompany()) {
				case "1":
					shipingCompany = "COSCO SHIPPING LINES MEXICO";
					break;
				case "2":
					shipingCompany = "MAERSK";
					break;
				case "3":
					shipingCompany = "HAPAG LLOYD";
					break;
				case "4":
					shipingCompany = "HAMBURG-SUD";
					break;
				case "5":
					shipingCompany = "MEDITERRANEAN SHIPPING CO";
					break;
				case "6":
					shipingCompany = "OCEAN NETWORK EXPRESS";
					break;
				case "7":
					shipingCompany = "CMA-CGM";
					break;
				case "8":
					shipingCompany = "CONTAINER SUDAMERICA";
					break;
				case "9":
					shipingCompany = "HANJIN SHIPPING CO";
					break;

				
				}
				
				hcell = new PdfPCell(new Phrase("PROPIETARIO:  " + shipingCompany, regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("RESPONSABLE DEL DAÑO:  " + container.getTypeServicePregate(), regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("UNIDAD:  " + container.getContainer().toString() , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("TAMAÑO:  " + container.getContaierSize() , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("TIPO DE EQUIPO: DC"  , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("NOMENCLATURA:  " + container.getNomenclatura() , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("EN TERMINAL: Si" , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(hcell);
				log.info("paso datos");
				//-------------------------------------------------------------------------------
				
				 /***********************************************************************************************************************/
				
				/***********************************************************************************************************************/
				PdfPTable table2 = new PdfPTable(2);
				table2.setWidthPercentage(100);
				table2.setWidths(new float[] {3,3});
				
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("FECHA DE AUTORIZACION: ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("NUMERO DE AUTORIZACION" , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("FECHA DE INICIO DE REPARACION" , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("FECHA FIN DE REPARACION" , regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(hcell);
				log.info("paso datos 2");
				//-------------------------------------------------------------------------------
				
				 /***********************************************************************************************************************/
				
				
				/***********************************************************************************************************************/
				PdfPTable table3 = new PdfPTable(1);
				table3.setWidthPercentage(100);
				table3.setWidths(new float[] {3});
				
				
				//-------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell();
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.addElement(table2);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table3.addCell(hcell);
				
				//-------------------------------------------------------------------------------
				
				hcell = new PdfPCell(new Phrase("\n\n DEPOSITO M&R", regularBlack));
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setVerticalAlignment(Element.ALIGN_CENTER);
				table3.addCell(hcell);
				
				
				
				 /***********************************************************************************************************************/
				
				document.add(new Paragraph(" \n"));
				
				table = new PdfPTable(2);
				table.setWidthPercentage(102);
				table.setWidths(new float[] {(float) 4,4});
				
				
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell();
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.addElement(table1);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				
				hcell = new PdfPCell();
				hcell.addElement(table3);
				hcell.setBorder(Rectangle.NO_BORDER);
				hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(hcell);
				//-------------------------------------------------------------------------------
				document.add(table);
				
				 /***********************************************************************************************************************/
				PdfPTable table4 = new PdfPTable(13);
				table4.setWidthPercentage(100);
				table4.setWidths(new float[] {2,2,2,2,2,2,2,2,2,2,2,2,2});
				 /***********************************************************************************************************************/
				
				//-------------------------------------------------------------------------------
				hcell = new PdfPCell(new Phrase("Daño", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Codigo de localizacion", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Componente", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Metodo de reparacion", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Largo cm", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Ancho cm", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Area cm", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Cantidad", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("Horas" , regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("Labor", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("Material", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("Total", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase("Moneda", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				log.info("Antes del al for");

				//-------------------------------------------------------------------------------
				for(InspectionModel inspection : inspections) {
					
					CatShippingCompanyModel labor = catShippingCompanyReposirtory.getLabor(container.getShippingCompany());
					
					log.info("Entra al for");
					QuoteModel quote = quoteRepositry.getByInspectionId(inspection.getInspectionId());

					hcell = new PdfPCell(new Phrase(catDamageRepository.getById(inspection.getDamage().toString()).getDescription(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					
					hcell = new PdfPCell(new Phrase(inspection.getLocation(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);

					hcell = new PdfPCell(new Phrase(catComponentRepository.getById(inspection.getComponent()).getComponent(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);

					hcell = new PdfPCell(new Phrase(quote.getRepairDescription(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getLength(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getWidth(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);

					hcell = new PdfPCell(new Phrase(inspection.getDepth(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					
					hcell = new PdfPCell(new Phrase(inspection.getQuantity(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					
					hcell = new PdfPCell(new Phrase(quote.getHours(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					
					double sumLabor = Double.parseDouble(labor.getLabor());
					double sumHh = Double.parseDouble(quote.getHours());
					
					double totalLabor = sumLabor+sumHh;
					
					String totalLaborStr = Double.toString(totalLabor);
					
					log.info(totalLaborStr + "totalLabor********************************");
					
					hcell = new PdfPCell(new Phrase(totalLaborStr.toString(), 	regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					
					hcell = new PdfPCell(new Phrase(quote.getMaterial(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					

					double valUno = Double.parseDouble(quote.getHours());
					double valDos = Double.parseDouble(quote.getLabor());
					double valTres = Double.parseDouble(quote.getMaterial());
					
					double quoteSum =  valUno + valDos + valTres;
					
					String quoteStr = Double.toString(quoteSum);
					
					hcell = new PdfPCell(new Phrase(quoteStr, regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);

					hcell = new PdfPCell(new Phrase(quote.getExchange(), regularBlack));
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setBorder(Rectangle.NO_BORDER);
					table4.addCell(hcell);
					
					
					
				}
				//-------------------------------------------------------------------------------
				

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase("TOTAL" , regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);

				hcell = new PdfPCell(new Phrase(" ", regularBlack));
				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hcell.setBackgroundColor(BaseColor.GRAY);
				hcell.setBorder(Rectangle.NO_BORDER);
				table4.addCell(hcell);
				
				//-------------------------------------------------------------------------------

				
				document.add(table4);
				
				log.info("Termino de generar");
				
				/***********************************************************************************************************************/
			document.close();
			
			
			
			
			} catch (DocumentException ex) {
			        	log.error(ex.toString());
			} 
			return out.toByteArray();
	}
	
	
	
	
}




	