package com.tmm.myre.quote.service;

import java.util.List;

import com.tmm.myre.catalog.model.CatJobcodeModel;
import com.tmm.myre.catalog.model.JobcodeHistoricModel;
import com.tmm.myre.catalog.repository.ICatJobcodeRepository;
import com.tmm.myre.catalog.repository.IJobcodeHistoricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.inspections.model.InspectionModel;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.quote.converter.QuoteConverter;
import com.tmm.myre.quote.dto.QuoteDto;
import com.tmm.myre.quote.model.QuoteModel;
import com.tmm.myre.quote.repository.IQuoteRepository;
import com.tmm.myre.quote.service.core.IQuoteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("quoteService")
public class QuoteService implements IQuoteService {
	
	@Autowired
	private IQuoteRepository  quoteRepository ;
	
	@Autowired
	private QuoteConverter quoteConverter;
	
	@Autowired
	private IInspectionRepository inspectionRepository; 
	
	@Autowired
	private IContainerRepository containerRepository;

	@Autowired
	private ICatJobcodeRepository catJobcodeRepository;

	@Autowired
	private IJobcodeHistoricRepository jobcodeHistoricRepository;
	
	@Override
	public ResponseManagement saveInformationQuote(QuoteDto quoteDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		
		try {
			log.info(quoteDto.toString()+"********************************************");	
			InspectionModel inspection = inspectionRepository.getById(quoteDto.getInspectionId());
			inspection.setStatus(3);
			inspectionRepository.save(inspection);

			int exist = quoteRepository.getByIdInspection(inspection.getInspectionId());

			// Si el jobcodeId no es nulo ni vacío, verifica si hay cambios y actualiza solo si es necesario
			if (quoteDto.getJobcodeId() != null && !quoteDto.getJobcodeId().isEmpty()) {
				// Obtener el modelo actual
				CatJobcodeModel jobcode = catJobcodeRepository.findById(quoteDto.getJobcodeId())
						.orElse(null);
				if (jobcode != null) {
					boolean actualizado = false;
					if (!quoteDto.getWorkCode().equals(jobcode.getJobcodeRepair())) {
						jobcode.setJobcodeRepair(quoteDto.getWorkCode());
						actualizado = true;
					}
					if (!quoteDto.getRepairDescription().equals(jobcode.getJobcodeDescription())) {
						jobcode.setJobcodeDescription(quoteDto.getRepairDescription());
						actualizado = true;
					}
					if (!quoteDto.getMaterial().equals(jobcode.getJobcodeMaterial())) {
						jobcode.setJobcodeMaterial(quoteDto.getMaterial());
						actualizado = true;
					}
					if (!quoteDto.getHours().equals(jobcode.getJobcodeHh())) {
						jobcode.setJobcodeHh(quoteDto.getHours());
						actualizado = true;
					}
					if (!quoteDto.getExchange().equals(jobcode.getJobcodeExchange())) {
						jobcode.setJobcodeExchange(quoteDto.getExchange());
						actualizado = true;
					}
					if (actualizado) {
						// Guardar el estado anterior en histórico antes de actualizar
						JobcodeHistoricModel historic = JobcodeHistoricModel.builder()
								.jobcodeId(jobcode.getJobcodeId())
								.jobcodeRepair(jobcode.getJobcodeRepair())
								.jobcodeDescription(jobcode.getJobcodeDescription())
								.jobcodeMaterial(jobcode.getJobcodeMaterial())
								.jobcodeHh(jobcode.getJobcodeHh())
								.jobcodeExchange(jobcode.getJobcodeExchange())
								.jobcodeShippingId(jobcode.getJobcodeShippingId())
								.build();
						jobcodeHistoricRepository.save(historic);

						catJobcodeRepository.save(jobcode);
					}
				}
			}



			if(exist == 0) {
				quoteDto.setQuoteId(UuidProvider.getUUID());
				quoteRepository.save(quoteConverter.convert(quoteDto));
			}else {

				QuoteModel quote = quoteRepository.getByInspectionId(inspection.getInspectionId());
				quote.setHours(quoteDto.getHours());
				quote.setLabor(quoteDto.getLabor());
				quote.setMaterial(quoteDto.getMaterial());
				quote.setTarifa(quoteDto.getTarifa());
				quote.setWorkCode(quoteDto.getWorkCode());
				quote.setRepairDescription(quoteDto.getRepairDescription());
				quote.setExchange(quoteDto.getExchange());

				quoteRepository.save(quote);
			}



			int cuenta = inspectionRepository.countInspectionsValidation(inspection.getContainerId(),2);
			response.setNum(cuenta);
			response.setSuccess(true);
		} catch (Exception e) {
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	@Override
	public QuoteDto getQuoteDetail(String inspectionId) throws ConverterException {
		QuoteModel quote = quoteRepository.getByInspectionId(inspectionId);

		QuoteDto entity = quoteConverter.convert(quote);

		return entity;

	}


}
