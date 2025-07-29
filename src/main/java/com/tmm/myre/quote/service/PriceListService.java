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

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.tmm.myre.catalog.converter.JobcodeHistoricConverter;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.dto.JobcodeHistoricDto;
import com.tmm.myre.catalog.model.CatJobcodeModel;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.model.JobcodeHistoricModel;
import com.tmm.myre.catalog.repository.ICatJobcodeRepository;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.catalog.repository.IJobcodeHistoricRepository;
import com.tmm.myre.quote.dto.PriceListExcelDto;
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
	public ResponseManagement createLisprice(MultipartFile file) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();	
		try {
			List<PriceListExcelDto> priceList = Poiji.fromExcel(file.getInputStream(), PoijiExcelType.XLSX, PriceListExcelDto.class);
			log.info("Price List Size: " + priceList.size());

			for (PriceListExcelDto dto : priceList) {
				if (dto.getId() == null || dto.getId().trim().isEmpty()) {
					// Nuevo código de trabajo
					Long maxId = catJobcodeRepository.findMaxJobcodeId();
					String newId = String.valueOf((maxId != null ? maxId : 0) + 1);

					CatJobcodeModel newJobcode = CatJobcodeModel.builder()
					    .jobcodeId(newId)
					    .jobcodeRepair(dto.getJobCodeRepair())
					    .jobcodeDescription(dto.getJobCodeDescription())
					    .jobcodeMaterial(dto.getJobCodeMaterial())
					    .jobcodeHh(dto.getJobCodeHours() != null ? dto.getJobCodeHours().toString() : null)
					    .jobcodeExchange(dto.getJobCodeCurrency())
					    .jobcodeShippingId(dto.getJobCodeClient())
					    .build();
					catJobcodeRepository.save(newJobcode);
				} else {
					// Modificación: buscar el registro existente
					CatJobcodeModel oldJobcode = catJobcodeRepository.findById(dto.getId()).orElse(null);
					if (oldJobcode != null) {
						// Guardar en histórico
						JobcodeHistoricModel historic = JobcodeHistoricModel.builder()
								.jobcodeId(oldJobcode.getJobcodeId())
								.jobcodeRepair(oldJobcode.getJobcodeRepair())
								.jobcodeDescription(oldJobcode.getJobcodeDescription())
								.jobcodeMaterial(oldJobcode.getJobcodeMaterial())
								.jobcodeHh(oldJobcode.getJobcodeHh())
								.jobcodeExchange(oldJobcode.getJobcodeExchange())
								.jobcodeShippingId(oldJobcode.getJobcodeShippingId())
								.build();
						jobcodeHistoricRepository.save(historic);
					}

					// Actualizar el registro existente
					CatJobcodeModel newJobcode = CatJobcodeModel.builder()
					    .jobcodeId(dto.getId()) // Usar el mismo ID para actualizar el registro existente
					    .jobcodeRepair(dto.getJobCodeRepair())
					    .jobcodeDescription(dto.getJobCodeDescription())
					    .jobcodeMaterial(dto.getJobCodeMaterial())
					    .jobcodeHh(dto.getJobCodeHours() != null ? dto.getJobCodeHours().toString() : null)
					    .jobcodeExchange(dto.getJobCodeCurrency())
					    .jobcodeShippingId(dto.getJobCodeClient())
					    .build();
					catJobcodeRepository.save(newJobcode);
				}
			}
			response.setSuccess(true);
			response.setMessage("Carga y actualización completada");
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
