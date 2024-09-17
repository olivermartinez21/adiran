package com.tmm.myre.quote.service;

import java.util.List;

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
	
	@Override
	public ResponseManagement saveInformationQuote(QuoteDto quoteDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		
		try {
			log.info(quoteDto.toString()+"********************************************");	
			InspectionModel inspection = inspectionRepository.getById(quoteDto.getInspectionId());
			inspection.setStatus(3);
			inspectionRepository.save(inspection);
			
			int exist = quoteRepository.getByIdInspection(inspection.getInspectionId());
			
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

}
