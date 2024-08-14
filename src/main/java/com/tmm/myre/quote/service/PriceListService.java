package com.tmm.myre.quote.service;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.catalog.dto.CatListPriceCarrierDto;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.quote.service.core.IPriceListService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("priceListService")
public class PriceListService implements IPriceListService {
	@Override
	public ResponseManagement createLisprice(MultipartFile file, String type) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();	
		try {
			
			int firstRow=1, sheetSelected = 0;;
			if(type.equals("Thermo King") ) {
				firstRow=4;
				sheetSelected=1;
			}
			if(type.equals("Star Cool") ) {
				firstRow=2;
			}
			
			List<CatListPriceCarrierDto> cat = new ArrayList<CatListPriceCarrierDto>();
			CatListPriceCarrierDto lista = new CatListPriceCarrierDto();
			
			
			List<String> datos = new ArrayList<String>();
			InputStream inp =  new BufferedInputStream(file.getInputStream());
			Workbook workbook = WorkbookFactory.create(inp);
			Sheet sheet = workbook.getSheetAt(sheetSelected);
			int  count= 0;
			for(int i = firstRow; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				datos.clear();
				count=0;
					for(int j = 0; j <= row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j); 
						
						switch (j) {
						case 0:
							
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						case 4:
							
							break;
						case 5:
							
							break;
						case 6:
							
							break;
						case 7:
							
							break;
						case 8:
							
							break;
						case 9:
							
							break;
						case 10:
							
							break;
						case 11:
							
							break;
						case 12:
							
							break;
						case 13:
							
							break;
							

						default:
							break;
						}
						
					}
					
					
					if(count>=4) {
						break;
					}
			}
			
			/*List<CatListPriceCarrierDto> cat = new ArrayList<CatListPriceCarrierDto>();
			List<List<String>> lista = new ArrayList<List<String>>();
			List<String> datos = new ArrayList<String>();
			InputStream inp =  new BufferedInputStream(file.getInputStream());
			Workbook workbook = WorkbookFactory.create(inp);
			Sheet sheet = workbook.getSheetAt(sheetSelected);
			int  count= 0,count2=0;
			for(int i = firstRow; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				datos.clear();
				count=0;
					for(int j = 0; j <= row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j); 
						
						if(cell!=null) {
							String value = "";
							try {
								 value = cell.getStringCellValue();
							} catch(Exception ex) {
								 value = String.valueOf((cell.getNumericCellValue()));
							}
							datos.add(value);
							   if(value=="") {
								   count++;
							   	}
						}else {
							datos.add("");
						}
						
						
						
					}
					lista.add(new ArrayList<String>(datos));
					if(count>=4) {
						count2++;
						break;
					}
			}
			
			log.info(cat+"------------------");*/
			
			
		} catch (Exception e) {
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
	  
		
		return response;
	}
	

}
