package com.tmm.myre.inspections.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.inspections.model.InspecctionOutModel;
import com.tmm.myre.inspections.repository.IInspectionOutRepository;
import com.tmm.myre.inspections.service.core.IInspectionsOutService;
import com.tmm.myre.photo.repository.IPhotoRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service("inspectionOutService")
public class InspectionOutService implements IInspectionsOutService {
	
	@Autowired
	private IInspectionOutRepository inspectionOutRepository;

	@Autowired
	private IContainerRepository containerRepository;

	@Autowired
	private IPdfGenerationService pdfGenerationService;
	
	@Override
	public ResponseManagement saveInspectionOut(List<MultipartFile> file, String container) throws ConverterException {
	ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
	
	try {
		ObjectMapper containerMpper = new ObjectMapper();
		ContainerDto containerDto = containerMpper.readValue(container, new TypeReference<ContainerDto>() { });
		List<InspecctionOutModel> inspection = new ArrayList<InspecctionOutModel>();
		
		if(file!=null) {
			for (int i = 0; i < file.size(); i++) {
				
				inspection.add(InspecctionOutModel.builder()
						.inspectionId(UuidProvider.getUUID())
						.photo(file.get(i).getBytes())
						.containerId(containerDto.getContainerId())
						.inspectionDateOut(containerDto.getDateInspection())
						.build());
			}
			inspectionOutRepository.saveAll(inspection);
		}
		
		ContainerModel containeEdit = containerRepository.getById(containerDto.getContainerId());
		
		containeEdit.setQualityStamp(containerDto.getQualityStamp());
		containeEdit.setSecurityStamp(containerDto.getSecurityStamp());
		int valor = containerRepository.getCountEirOut();
		containeEdit.setEirOutName("EIR-OUT-"+containeEdit.getLocation()+"-0"+valor);
		containeEdit.setEirOut(pdfGenerationService.pdfEirOut(containerDto.getContainerId(),containerDto.getDataUrl()));
		containeEdit.setStatus(6);
		
		containerRepository.save(containeEdit);
		
		response.setPdf(containeEdit.getContainerId());
		response.setSuccess(true);
	} catch (Exception e) {
		response.setSuccess(false);
		response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
		response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
	}
		return response;
	}
	

}
