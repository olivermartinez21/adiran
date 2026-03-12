package com.tmm.myre.inspections.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tmm.myre.quote.dto.InspectionWithQuoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.appointments.repository.IAppointmentRepository;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.inspections.converter.InspectionConverter;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.model.InspectionModel;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.inspections.service.core.IInspectionsService;
import com.tmm.myre.photo.model.PhotoModel;
import com.tmm.myre.photo.repository.IPhotoRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service("inspectionService")
public class InspectionService implements IInspectionsService {
	
	@Autowired
	private IContainerRepository containeRrepository;
	
	@Autowired
	private IInspectionRepository inspectoRepository;
	
	@Autowired
	private InspectionConverter inspectionConverter;
	
	@Autowired
	private IAppointmentRepository appointmentRepository;
	
	@Autowired
	private IPhotoRepository photoRepository;


	

	@Override
	public List<InspectionDto> getInspections(String appointmentId) throws ConverterException {
		List<ContainerModel> containers = containeRrepository.findContainersInspections(appointmentId);
		List<InspectionModel> inspections; 
		List<InspectionDto> list = new ArrayList<InspectionDto>();
		for(ContainerModel container : containers ) {
			inspections = inspectoRepository.findAllByContainer(container.getContainerId());
			for(InspectionModel inspection: inspections ) {
				list.add(inspectionConverter.convert(inspection));
			}
			
			
		}
		
		return list;
	}
	
	@Override
	public List<InspectionDto> getInspectionsByContainer(String containerId) throws ConverterException {
		log.info("Entrado a getInspectionsByContainer " + containerId);
		List<InspectionModel> inspections = inspectoRepository.findAllByContainer(containerId);
		List<InspectionDto> list = new ArrayList<InspectionDto>();
		for(InspectionModel inspection: inspections ) {
			list.add(inspectionConverter.convert(inspection));
		}
		return list;
	}


	@Override
	public ResponseManagement inspectionValidation(String inspectionId) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		try {
			InspectionModel inspectionValidation = inspectoRepository.getById(inspectionId);
			
			if(inspectionValidation.getStatus()==1) {
				inspectionValidation.setStatus(2);//estatus de que se esta trabajando en el contenedor
				
			}else if(inspectionValidation.getStatus()==2) {
				inspectionValidation.setStatus(3);//estatus de que el contenedor se le aplicaron los procedimientos pertinentes en el taller
			}
			inspectoRepository.save(inspectionValidation); 
			
			int inspections = inspectoRepository.countInspections(inspectionValidation.getContainerId());
			
			ContainerModel container= containeRrepository.getById(inspectionValidation.getContainerId());
			
			if(inspections==0) {
				container.setStatus(3);//estatus de que entro al taller
				inspections = inspectoRepository.countInspectionsValidation(inspectionValidation.getContainerId(),2);
			}
			if(inspections==0) {
				container.setStatus(4);//estatus de que esta listo 
			}
			
			if(container.getAppointmentId()!=null) {
				AppointmentModel appointment = appointmentRepository.getById(container.getAppointmentId());
				appointment.setStatus(6);//indica a el cliente que sus contenedores o alguno de ellos se encuentra en el taller 
				appointmentRepository.save(appointment);	
			}
			
			containeRrepository.save(container);
			response.setSuccess(true);
			response.setOperation(KeyConstants.UPDATE);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement deleteInspecction(String inspecctionId) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		if(inspectoRepository.existsById(inspecctionId)) {
			inspectoRepository.deleteById(inspecctionId);
		}
		
		response.setSuccess(true);
		response.setOperation(KeyConstants.UPDATE);
		return response;
	}

	@Override
	public ResponseManagement saveNewInspection(InspectionDto inspectionDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		
		try {
		inspectionDto.setInspectionId(UuidProvider.getUUID());
		
		inspectoRepository.save(inspectionConverter.convert(inspectionDto));
		response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement addImages(List<MultipartFile> file, String inspectionUpdate)throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			ObjectMapper inspectionMapper = new ObjectMapper();
			InspectionDto inspectionDto = inspectionMapper.readValue(inspectionUpdate, new TypeReference<InspectionDto>() { });
			InspectionModel inspection = inspectoRepository.getById(inspectionDto.getInspectionId());
			log.info("llego servicio  --------------------------"+ inspection);
			inspectionDto.setStatus(2);
			if(inspectionDto.getCustomerType()==2){
				inspectionDto.setCustomerName("CARRIER");
			}
			inspectoRepository.save(inspectionConverter.convert(inspectionDto));
			
			int Ins = inspectoRepository.getInspectionMerchant(inspection.getContainerId());
			if(Ins==0) {
				ContainerModel container = containeRrepository.getById(inspection.getContainerId());
				container.setStatusQute(1);
				containeRrepository.save(container);
			}
			
			List<PhotoModel> photo = new ArrayList<PhotoModel>();
			if(file!=null) {
				for (int i = 0; i < file.size(); i++) {
					
					photo.add(PhotoModel.builder()
							.photoId(UuidProvider.getUUID())
							.containerId(inspection.getInspectionId())
							.photo(file.get(i).getBytes())
							.build());
				}
				
				photoRepository.saveAll(photo);
			}
			
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	
	@Override
	public ResponseManagement addDamage(List<MultipartFile> file, String inspection)throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			ObjectMapper inspectionMapper = new ObjectMapper();
			InspectionDto inspectionDto = inspectionMapper.readValue(inspection, new TypeReference<InspectionDto>() { });
			List<PhotoModel> photo = new ArrayList<PhotoModel>();
			//*************************** [PROCESO DE INSERCCION DE DAÑO NUEVO] ***************************
			if (inspectionDto.getNewDamageTypeOperation().equals("INSERT")){

				ContainerModel containerEdit = containeRrepository.getById(inspectionDto.getContainerId());
				log.info("+++++++++++++ " + containerEdit.toString());
				if(inspectionDto.getCustomerType()==2){
					inspectionDto.setCustomerName("CARRIER");
				}
				inspectionDto.setInspectionId(UuidProvider.getUUID());
				inspectionDto.setStatus(2);

				int cantidad =	containeRrepository.getInspectionsMerchant(containerEdit.getContainerId());


				if(inspectionDto.getCustomerType()==1 || inspectionDto.getCustomerType()==2) {
					containerEdit.setStatusQute(1);
				}

				if(inspectionDto.getRepairInspection()!=null) {
					if(inspectionDto.getRepairInspection()==1) {
						containerEdit.setStatusQute(5);
					}
				}


				containeRrepository.save(containerEdit);

				inspectoRepository.save(inspectionConverter.convert(inspectionDto));

				if(file != null && !file.isEmpty()) {
					for (int i = 0; i < file.size(); i++) {
						photo.add(PhotoModel.builder()
								.photoId(UuidProvider.getUUID())
								.containerId(inspectionDto.getInspectionId())
								.photo(file.get(i).getBytes())
								.build());
					}
					photoRepository.saveAll(photo);
				}

				response.setSuccess(true);
			}
			//*************************** [PROCESO DE ACTUALIZATION] ***************************
			else if (inspectionDto.getNewDamageTypeOperation().equals("UPDATE")) {
				InspectionModel inspectionUpdate = inspectoRepository.getInspectionForLabor(inspectionDto.getInspectionId());

				inspectionUpdate.setPart(inspectionDto.getPart());
				inspectionUpdate.setComponent(inspectionDto.getComponent());
				inspectionUpdate.setDamage(inspectionDto.getDamage());
				inspectionUpdate.setLocation(inspectionDto.getLocation());
				inspectionUpdate.setRepair(inspectionDto.getRepair());
				inspectionUpdate.setReference(inspectionDto.getReference());
				inspectionUpdate.setCustomerType(inspectionDto.getCustomerType());
				inspectionUpdate.setCustomerName(inspectionDto.getCustomerName());
				inspectionUpdate.setLength(inspectionDto.getLength());
				inspectionUpdate.setWidth(inspectionDto.getWidth());
				inspectionUpdate.setDepth(inspectionDto.getDepth());
				inspectionUpdate.setOtherLength(inspectionDto.getOtherLength());
				inspectionUpdate.setQuantity(inspectionDto.getQuantity());
				inspectionUpdate.setExtentOtherLarge(inspectionDto.getExtentOtherLarge());

				inspectionUpdate.setStatus(2);
				inspectionUpdate.setExtentLarge(null);

				if(inspectionDto.getCustomerType()==1 || inspectionDto.getCustomerType()==2) {
					ContainerModel containerEdit = containeRrepository.getById(inspectionUpdate.getContainerId());
					containerEdit.setStatusQute(1);
					containeRrepository.save(containerEdit);
				}

				if(file != null && !file.isEmpty()) {
					for (int i = 0; i < file.size(); i++) {
						photo.add(PhotoModel.builder()
								.photoId(UuidProvider.getUUID())
								.containerId(inspectionDto.getInspectionId())
								.photo(file.get(i).getBytes())
								.build());
					}
					photoRepository.saveAll(photo);
				}

				inspectoRepository.save(inspectionUpdate);
				response.setSuccess(true);

			}

		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement repairDamage(String inspectionId) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			InspectionModel inspection = inspectoRepository.getById(inspectionId);
			//ContainerModel container = containeRrepository.getById(inspection.getContainerId());
			
			inspection.setStatus(4);
			inspectoRepository.save(inspection);
			int inspections = inspectoRepository.getCountInspectionsRepair(inspection.getContainerId());
			/*if(inspections==0) {
				container.setStatusQute(4);
			}*/
			//containeRrepository.save(container);
			response.setNum(inspections);
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	@Override
	public int validationInspection(String containerId, int status) throws ConverterException {
		
		int  validation = inspectoRepository.countInspectionsValidation(containerId,3);
		/*if(validation==0) {
			ContainerModel container = containeRrepository.getById(containerId);
			container.setStatusQute(4);
			containeRrepository.save(container);
		}*/
		return validation;
	}

	@Override
	public ResponseManagement changeStatus(ContainerDto contianerDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			
			int  validation = inspectoRepository.countInspectionsValidation(contianerDto.getContainerId(),3);
			if(validation==0) {
				ContainerModel container = containeRrepository.getById(contianerDto.getContainerId());
				container.setCondition(contianerDto.getCondition());
				container.setClasification(contianerDto.getClasification());
				container.setStatusQute(4);//reparacion confirmadas
				containeRrepository.save(container);
				response.setSuccess(true);
			}else {
				response.setMessage("Revisa todos los daños antes de validar el contenedor");
			}
			
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement deleteInspection(String inspectionId) throws ConverterException{
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		if(inspectoRepository.existsById(inspectionId)) {
			inspectoRepository.deleteById(inspectionId);
		}
		
		response.setSuccess(true);
		response.setOperation(KeyConstants.UPDATE);
		return response;
	}

	@Override
	public ResponseManagement requestInspection(String containerId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			int validationInspection = inspectoRepository.countInspectionsRequest(containerId);
				if(validationInspection == 0) {
					response.setSuccess(false);
				}else {
					response.setSuccess(true);
				}
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}

	public List<InspectionWithQuoteDto> getInspectionsWithQuotes(String containerId) {
		List<Object[]> rows = inspectoRepository.findInspectionsWithQuotes(containerId);
		List<InspectionWithQuoteDto> result = new ArrayList<>();

		for (Object[] row : rows) {
			result.add(InspectionWithQuoteDto.builder()
					.inspectionId((String) row[0])
					.part((String) row[1])
					.component((String) row[2])
					.damage((Integer) row[3])
					.location((String) row[4])
					.repair((String) row[5])
					.damageGenSet((String) row[6])
					.damageCode((Integer) row[7])
					.reference((String) row[8])
					.customerType((Integer) row[9])
					.customerName((String) row[10])
					.photo((String) row[11])
					.status((Integer) row[12])
					.containerId((String) row[13])
					.length((String) row[14])
					.width((String) row[15])
					.depth((String) row[16])
					.otherLength((String) row[17])
					.extentLarge((Integer) row[18])
					.extentHeigth((Integer) row[19])
					.extentDepth((Integer) row[20])
					.extentOtherLarge((Integer) row[21])
					.quantity((String) row[22])
					.quoteId((String) row[23])
					.workCode((String) row[24])
					.repairDescription((String) row[25])
					.hours((String) row[26])
					.labor((String) row[27])
					.material((String) row[28])
					.tarifa((String) row[29])
					.exchange((String) row[30])
					.build()
			);
		}

		return result;
	}

	@Override
	public ResponseManagement editBillToName(String inspectionId, String newBillTo) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try{
			InspectionModel inspection = inspectoRepository.getById(inspectionId);
			inspection.setCustomerName(newBillTo);
			inspectoRepository.save(inspection);
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		return response;
	}


}
