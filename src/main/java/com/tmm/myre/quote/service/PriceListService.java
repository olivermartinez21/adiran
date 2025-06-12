package com.tmm.myre.quote.service;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tmm.myre.catalog.converter.JobcodeHistoricConverter;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.dto.JobcodeHistoricDto;
import com.tmm.myre.catalog.model.CatJobcodeModel;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.model.JobcodeHistoricModel;
import com.tmm.myre.catalog.repository.ICatJobcodeRepository;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.catalog.repository.IJobcodeHistoricRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ICatJobcodeRepository catJobcodeRepository;

	@Autowired
	private ICatShippingCompanyReposirtory catShippingCompanyRepository;

	@Autowired
	private IJobcodeHistoricRepository jobcodeHistoricRepository;

	@Autowired
	private JobcodeHistoricConverter jobcodeHistoricConverter;


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

	@Override
	public void updateJobcode(Map<String, Object> data) {
		String jobcodeId = (String) data.get("jobcodeId");
		CatJobcodeModel jobcode = catJobcodeRepository.findById(jobcodeId)
				.orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

		// 1. Guarda el estado anterior en histórico
		JobcodeHistoricModel historicDto = JobcodeHistoricModel.builder()
				.jobcodeId(jobcode.getJobcodeId())
				.jobcodeRepair(jobcode.getJobcodeRepair())
				.jobcodeDescription(jobcode.getJobcodeDescription())
				.jobcodeMaterial(jobcode.getJobcodeMaterial())
				.jobcodeHh(jobcode.getJobcodeHh())
				.jobcodeExchange(jobcode.getJobcodeExchange())
				.jobcodeShippingId(jobcode.getJobcodeShippingId())
				.build();

		jobcodeHistoricRepository.save(historicDto);

		// Actualiza todos los campos recibidos (excepto el id)
		data.forEach((key, value) -> {
			if (!key.equals("jobcodeId")) {
				try {
					Field field = CatJobcodeModel.class.getDeclaredField(key);
					field.setAccessible(true);
					field.set(jobcode, value != null ? value.toString() : null);
				} catch (Exception e) {
					// Puedes loggear si algún campo no existe
				}
			}
		});

		catJobcodeRepository.save(jobcode);
	}

//	@Override
//	public ResponseManagement updateLaborByShippingCompanyId(CatShippingCompanyDto catShippingCompanyDto) throws ConverterException{
//		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
//		try {
//			CatShippingCompanyModel shippingCompany = catShippingCompanyRepository.getById(catShippingCompanyDto.getShippingCompanyId());
//
//			shippingCompany.setLabor(catShippingCompanyDto.getLabor());
//			catShippingCompanyRepository.save(shippingCompany);
//			response.setSuccess(true);
//		} catch (Exception e) {
//			log.error("Error al obtener la compañía de envío: " + e.getMessage());
//			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
//			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
//			return response;
//		}
//		return response;
//	}

	@Override
	public String getExchangeByShippingCompanyId(String shippingCompanyId) {
	    CatJobcodeModel jobcodeModel = catJobcodeRepository.findFirstByJobcodeShippingIdOrderByJobcodeIdAsc(shippingCompanyId);
	    return jobcodeModel != null ? jobcodeModel.getJobcodeExchange() : null;
	}

	@Override
	public ResponseManagement updateLaborAndExchangeByShippingCompanyId(String shippingCompanyId, String maneuverCost, String labor, String exchange) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			CatShippingCompanyModel shippingCompany = catShippingCompanyRepository.getById(shippingCompanyId);
			shippingCompany.setLabor(labor);
			shippingCompany.setManeuverCost(maneuverCost);
			catShippingCompanyRepository.save(shippingCompany);
			catJobcodeRepository.updateExchange(shippingCompanyId, exchange);

			response.setSuccess(true);
		} catch (Exception e) {
			log.error("Error al actualizar labor y exchange: " + e.getMessage());
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

}
