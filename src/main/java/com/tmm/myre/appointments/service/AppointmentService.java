package com.tmm.myre.appointments.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmm.myre.appointments.converter.AppointmentConverter;
import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.appointments.repository.IAppointmentRepository;
import com.tmm.myre.appointments.service.core.IAppointmentService;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.repository.IUserRoleRepository;
import com.tmm.myre.base.service.core.IAppLogService;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.containers.converter.ContainerConverter;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.userRegister.model.UserRegisterModel;
import com.tmm.myre.userRegister.repository.IUserRegisterRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("appointmentService")
public class AppointmentService implements IAppointmentService {
	
	@Autowired
	private IAppointmentRepository appointmentRepository;
	
	@Autowired
	private AppointmentConverter appointmentConverter;
	 
	@Autowired
	private ContainerConverter containerConverter;
	
	@Autowired
	private IContainerRepository containerRepository;

	@Autowired
	private IUserRoleRepository userRoleRepository;
	
	@Autowired
	private IUserRegisterRepository userRegisterRepository;
	
	@Autowired
	private IAppLogService appLogService;
	
	@Autowired
	private IPdfGenerationService pdfGenerationService;
	
	@Override
	public List<AppointmentDto> getDataTable(Integer userId) throws ConverterException {
		int role = userRoleRepository.getRole(userId);
		List<AppointmentDto> list = new ArrayList<AppointmentDto>();
		List<AppointmentModel> entities ;
		switch (role) {
		case 1://usuario "CUSTOMER" se mostraran las citas que a realizado y su estatus 
			entities = appointmentRepository.getUseAppointmentTable(userId);
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
			break;
		case 4://usuario "RECEPCION" se mostraran las citas realizadas de todos los clientes para ingresar al patio
			entities = appointmentRepository.findAllActiveAppointment();
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
			break;
		case 3://usuario "INSPECTOR" se mostraran las citas las cuales ingresaron y estan listas para la inspeccion de las unidades 
			entities = appointmentRepository.findAllInspectorAppointment();
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
			break;
		case 5://usuario "TALLER" se mostraran las citas en las cuales se confirmo el estimado resultado de la inspeccion
			entities = appointmentRepository.findAllRepairsAppointment();
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
			break;
		case 6://usuario "TECNICO" se mostraran las citas en las cuales se hara la prueba tecnica del GenSet solo para reffer
			entities = appointmentRepository.findAlltechnicalAppointment();
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
			break;
		default://usuario "ADMIN y MASTER" podran tener acceso a todas las funciones para fines de reportes o modificaciones y seguimiento 
			entities = appointmentRepository.findAll();
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
			break;
		}
		return list;
	}


	@Override
	public ResponseManagement saveAppointment(AppointmentDto appointmentDto) {
		ResponseManagement response = ResponseManagement.builder().operation(appointmentDto.getOperation()).build();
		try {
			
			
			if(!appointmentDto.getAppointmentId().isEmpty()) {
				AppointmentModel AppointmentOut = appointmentRepository.getById(appointmentDto.getAppointmentId());
				
				if(AppointmentOut.getStatus()==7 && !appointmentDto.getShippingCompany().isEmpty()) {
					AppointmentOut.setStatus(9);
					appointmentRepository.save(AppointmentOut);
				}
			}
			
			
			ObjectMapper containersMapper = new ObjectMapper();
			List<ContainerDto> containers = containersMapper.readValue(appointmentDto.getContainersList(), new TypeReference<List<ContainerDto>>() { });
			//<ContainerModel> entities = new ArrayList<ContainerModel>();

			int Folio = appointmentRepository.countWarehouse(appointmentDto.getWarehouse()) + 1;
			appointmentDto.setAppointmentId(UuidProvider.getUUID());
			appointmentDto.setFolio(String.valueOf("AGS-0"+Folio));
			appointmentDto.setCreationAppointmentDate(DateManagement.todayDate());
			appointmentDto.setContainers(containers.size());
			appointmentDto.setStatus(1);
			//appointmentDto.setFileContent(out.toString());
			appointmentDto.setFileContent(pdfGenerationService.pdfCita(appointmentDto,containers));
			appointmentDto.setFileName(appointmentDto.getFolio());
			appointmentDto.setFileType(appointmentDto.getFolio());
			AppointmentModel newAppointment = appointmentConverter.convert(appointmentDto);
			for(ContainerDto container : containers) {
				if(containerRepository.serarchBd(container.getContainer())==0) {
					container.setContainerId(UuidProvider.getUUID());
					container.setRegisterDate(DateManagement.todayDate());
					container.setAppointmentId(newAppointment.getAppointmentId());
					container.setEventType(newAppointment.getEventType());
					container.setLocation(appointmentDto.getWarehouse());
					container.setStatus(1);
					containerRepository.save(containerConverter.convert(container));
				}else {
					if(containerRepository.serarchBdByStatus(container.getContainer())!=0) {
						ContainerModel existe = containerRepository.findBycontainer(container.getContainer());
						existe.setAppointmentId(newAppointment.getAppointmentId());
						existe.setStatus(1);
						existe.setContainerType(container.getContainerType());
						existe.setContaierSize(container.getContainerSize());
						existe.setShippingCompany(container.getShippingCompany());
						container.setLocation(appointmentDto.getWarehouse());
						containerRepository.save(existe);
					}else {
						response.setMessage("La unidad "+container.getContainer()+" ya cuenta con un proceso de cita o ya esta en nuestro almacen verfificar el numero");
					}
					
				}
			}
			log.info(response.getMessage()+"---");
			if(response.getMessage()==""||response.getMessage()==null||response.getMessage().isEmpty()) {
				appointmentDto.setLog("Appointment(" + appointmentDto.getIdUser() + ", " + appointmentDto.getAppointmentId() + "), " + "REGISTRO DE CITA");
				appLogService.registerLog(appointmentDto.getIdUser(), appointmentDto.getOperation(), appointmentDto.getLog());
				appointmentRepository.save(newAppointment);
				
				response.setPdf(newAppointment.getAppointmentId());
				response.setFolio(newAppointment.getFolio());
				response.setSuccess(true);
				response.setOperation(appointmentDto.getOperation());
			}
				
			
		} catch(Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	@Override
	public AppointmentDto getSingleData(String appointmentId,Integer userId,String warehouse) throws ConverterException {
		if(appointmentId.isEmpty()) {
			UserRegisterModel informationUser = userRegisterRepository.getcode(userId);
			AppointmentModel appointment = AppointmentModel.builder()
					.agency(informationUser.getAgency())
					.build();
			//appointment.setAgency(informationUser.getAgency());
			return appointmentConverter.convert(appointment);
		}else {
			return appointmentConverter.convert(appointmentRepository.getById(appointmentId));
		}
		
	}

	@Override
	public ResponseManagement updateAppointment(AppointmentDto appointmentDto) {
		ResponseManagement response = ResponseManagement.builder().operation(appointmentDto.getOperation()).success(false).build();
		try {
			ObjectMapper containersMapper = new ObjectMapper();
			List<ContainerDto> containers = containersMapper.readValue(appointmentDto.getContainersList(), new TypeReference<List<ContainerDto>>() { });
			List<ContainerModel> entities = new ArrayList<ContainerModel>();
			
			AppointmentModel updateAppointment = appointmentRepository.getById(appointmentDto.getAppointmentId());
			AppointmentModel detailsAppointmentUpdate = appointmentConverter.convert(appointmentDto);
			
			detailsAppointmentUpdate.setFolio(updateAppointment.getFolio());
			detailsAppointmentUpdate.setCreationAppointmentDate(updateAppointment.getCreationAppointmentDate());
			detailsAppointmentUpdate.setContainers(containers.size());
			detailsAppointmentUpdate.setStatus(updateAppointment.getStatus());
			
			updateAppointment = detailsAppointmentUpdate;
			
			for(ContainerDto container : containers) {
				 
				if(containerRepository.existsById(container.getContainerId())==true) {
					
					ContainerModel updateContainer = containerRepository.getById(container.getContainerId());
					ContainerModel detailsContainer = containerConverter.convert(container);
					detailsContainer.setAppointmentId(updateAppointment.getAppointmentId());
					detailsContainer.setStatus(updateContainer.getStatus());
					detailsContainer.setEventType(updateAppointment.getEventType());
					
					updateContainer = detailsContainer;
					containerRepository.save(updateContainer);
				}else {
					container.setContainerId(UuidProvider.getUUID());
					container.setAppointmentId(updateAppointment.getAppointmentId());
					container.setEventType(updateAppointment.getEventType());
					container.setStatus(1);
					container.setClasification("DAÑADO");
					entities.add(containerConverter.convert(container));
				}
			}
			appLogService.registerLog(appointmentDto.getIdUser(), appointmentDto.getOperation(), appointmentDto.getLog());
			appointmentRepository.save(updateAppointment);
			containerRepository.saveAll(entities);
			appointmentDto.setLog("Appointment(" + appointmentDto.getIdUser() + ", " + appointmentDto.getAppointmentId() + "), " + "EDICION DE CITA");
			appLogService.registerLog(appointmentDto.getIdUser(), appointmentDto.getOperation(), appointmentDto.getLog());
			response.setSuccess(true);
			response.setOperation(appointmentDto.getOperation());
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
		
	}

	@Override
	public ResponseManagement appointmentValidation(String appointmentId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		AppointmentModel updateAppointment = appointmentRepository.getById(appointmentId);
		try {
			if(updateAppointment.getStatus()==4) { //estatus "EN PROCESO" (aceptacion por parte del usuario del estimado para ingresar a taller) 
				updateAppointment.setStatus(5); //estatus "CONFIRMADO" para ingresar al taller  
			}else {
				updateAppointment.setStatus(2);
			}
			appointmentRepository.save(updateAppointment);
			
			response.setSuccess(true);
			response.setOperation(KeyConstants.UPDATE);
		}catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	@Override
	public List<AppointmentDto> getDataTableByDate(Date startDate, Date lastDate,String warehouse) throws ConverterException {
		List<AppointmentDto> list = new ArrayList<AppointmentDto>();
		List<AppointmentModel> entities = appointmentRepository.getAppointmentsByDate(startDate,lastDate,warehouse);;
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
		return list;
	}


	@Override
	public List<AppointmentDto> getDataTable(Integer userId, String warehouse,String role) throws ConverterException {
		//int role = userRoleRepository.getRole(userId);
		log.info(userId +" -- "+warehouse+" -- "+role +" -- " );
		List<AppointmentDto> list = new ArrayList<AppointmentDto>();
		List<AppointmentModel> entities ;
		if(role=="CLIENTE NAVIERA"|| role=="CLIENTE AA") { //role de cliente
			entities = appointmentRepository.getUseAppointmentTable(userId);
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
		}else {
			entities = appointmentRepository.findLocation(warehouse);
			for(AppointmentModel entity : entities) {
				list.add(appointmentConverter.convert(entity));
			}
		}
		
		return list;
	}


	@Override
	public AppointmentDto getOne(String appointmentId) throws ConverterException {
		AppointmentModel appointment = appointmentRepository.getById(appointmentId);
		return appointmentConverter.convert(appointment);
	}


	@Override
	public AppointmentDto getAppointmentData(String containerId) throws ConverterException {
		ContainerModel container = containerRepository.getById(containerId);
		if(!container.getAppointmentId().isEmpty()) {
			AppointmentModel appointment = appointmentRepository.getById(container.getAppointmentId());
			return appointmentConverter.convert(appointment);
		}
		return null;
		
	}


	
	
	


}
