package com.tmm.myre.containers.service;

import java.io.File;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.appointments.repository.IAppointmentRepository;
import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.base.configuration.secondary.EventActivityModel;
import com.tmm.myre.base.configuration.secondary.EventActivityRepository;
import com.tmm.myre.base.configuration.secondary.IIntegrationRepository;
import com.tmm.myre.base.configuration.secondary.IntegrationModel;
import com.tmm.myre.base.dto.AppLogDto;
import com.tmm.myre.base.dto.InspectionShippingDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.dto.appLogDatosDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.appLogDatosModel;
import com.tmm.myre.base.repository.IAppLogRepository;
import com.tmm.myre.base.repository.IUserRoleRepository;
import com.tmm.myre.base.service.core.IAppLogService;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.catalog.converter.CatShippingCompanyConverter;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.containers.converter.ContainerConverter;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ResumentInformationDto;
import com.tmm.myre.containers.model.ContainerHistoricModel;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerHistoricRepository;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.inspections.converter.InspectionConverter;
import com.tmm.myre.inspections.converter.InspectionOutConverter;
import com.tmm.myre.inspections.dto.InspecctionOutDto;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.model.InspecctionOutModel;
import com.tmm.myre.inspections.model.InspectionModel;
import com.tmm.myre.inspections.repository.IInspectionOutRepository;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.photo.model.PhotoModel;
import com.tmm.myre.photo.repository.IPhotoRepository;
import com.tmm.myre.userRegister.model.UserRegisterModel;
import com.tmm.myre.userRegister.repository.IUserRegisterRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("containerService")
public class ContainerService implements IContainerService{
	
	@Autowired
	private IContainerRepository containerRepository;
	
	@Autowired
	private IContainerHistoricRepository containerHistoricRepository;
	
	@Autowired
	private ContainerConverter containerConverter;

	@Autowired
	private InspectionConverter inspectionConverter;
	
	@Autowired
	private IInspectionRepository inspectorRepository;
	
	@Autowired
	private IAppointmentRepository appointmentRepository;
	
	
	@Autowired
	private IIntegrationRepository integrationRepository;
	
	@Autowired
	private IAppLogService appLogService;
	
	@Autowired
	private IUserRegisterRepository userRegisterRepository;
	
	@Autowired
	private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;
	
	@Autowired
	private IPdfGenerationService pdfGenerationService;
	
	@Autowired
	private IAssignmentRepository assignmentRepository;
	
	@Autowired
	private InspectionOutConverter inspectionOutConverter;
	
	@Autowired
	private IInspectionOutRepository inspectionOutRepository;
	
	@Autowired
	private EventActivityRepository eventActivityRepository;
	
	@Autowired
	private IAppLogRepository appLogRepository;
	
	@Autowired
	private IPhotoRepository photoRepository;
	
	@Autowired
	private CatShippingCompanyConverter catShippingCompanyConverter;
	
	
	
	
	@Override
	public List<ContainerDto> getContainers(String warehouse) throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAllbyWarehouse(warehouse);
			for(ContainerModel entity : entities) {
				list.add(containerConverter.convert(entity));
			
		}
		return list;
	}
	
	@Override
	public List<ContainerDto> getContainersStock(String warehouse) throws ConverterException {
		log.info(warehouse);
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAllbyWarehouseStock(warehouse);
			for(ContainerModel entity : entities) {
				list.add(containerConverter.convert(entity));
		}
		return list;
	}

	@Override
	public ResponseManagement updateContainerInformationOut(ContainerDto containerDto)throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();	
		
		try {
			ContainerModel container = containerRepository.getById(containerDto.getContainerId());
			
			AssignmentModel asignation = assignmentRepository.getbycontainer(container.getContainer());
			
			if(container.getContainerType()==3) {
				
				response.setSuccess(true);
			}else if(container.getContainerType()==6) {
				
				container.setVentilation(containerDto.getVentilation());
				container.setTemperature(containerDto.getTemperature());
				container.setHumiity(containerDto.getHumiity());
				container.setCo2(containerDto.getCo2());
				container.setO2(containerDto.getO2());
				container.setNi(containerDto.getNi());
				container.setAssociateUnit(containerDto.getAssociateUnit());
				container.setQualityStamp(containerDto.getQualityStamp());
				container.setSecurityStamp(containerDto.getSecurityStamp());
				
				if(asignation.getVentilation().equals(container.getVentilation()) && asignation.getTemperatuere().equals(container.getTemperature()) && asignation.getHumity().equals(container.getHumiity()) &&
						asignation.getCo2().equals(container.getCo2()) && asignation.getO2().equals(container.getO2()) && asignation.getNitrogen().equals(container.getNi())) {
					response.setSuccess(true);
				}else  {
					response.setMessage(" los valores requeridos no son correctos por favor revisarlos ");
				}
			}else if(container.getContainerType()==4) {
				container.setDiesel(containerDto.getDiesel());
				container.setHorometro(containerDto.getHorometro());
				container.setAssociateUnit(containerDto.getAssociateUnit());
				container.setQualityStamp(containerDto.getQualityStamp());
				container.setSecurityStamp(containerDto.getSecurityStamp());
				response.setSuccess(true);
			}
			if(response.getSuccess()==true) {
				if(containerDto.getNum()==containerDto.getFilas()-1) {
					int valor = containerRepository.getCountEirOut();
					valor=valor +1;
					container.setEirOutName("EIR-OUT-"+container.getLocation()+"-0"+valor);
					container.setEirOut(pdfGenerationService.pdfEirOut(containerDto.getContainerId(),containerDto.getDataUrl()));
				}
				
				response.setPdf(container.getContainerId());
				response.setNum(containerDto.getNum());
				
				InspecctionOutModel inspection = new InspecctionOutModel();
				inspection.setContainerId(container.getContainerId());
				inspection.setInspectionId(UuidProvider.getUUID());
				//inspection.setPhoto(containerDto.getImageList());
				inspectionOutRepository.save(inspection);
				
				container.setStatus(6);
			}
			
			
			containerRepository.save(container);
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	
	@Override
	public ResponseManagement saveUpdateContainer(ContainerDto containerDto) {
		
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();	
		try {
			log.info(containerDto+"--------------------------------------");
			ObjectMapper inspectionMapper = new ObjectMapper();
			List<InspectionDto> inspections = inspectionMapper.readValue(containerDto.getInspectionList(), new TypeReference<List<InspectionDto>>() { });
			List<InspectionModel> entities = new ArrayList<InspectionModel>();
			
			
			for(InspectionDto inspection : inspections ){
				log.info(inspection.getInspectionId()+"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				/*if(inspection.getCustomerType()==1) {
					containeredit.setStatusQute(1);
				}
				if(!inspectorRepository.existsById(inspection.getInspectionId())) {
					inspection.setInspectionId(UuidProvider.getUUID());
					inspection.setContainerId(containeredit.getContainerId());
					inspection.setStatus(2);
					entities.add(inspectionConverter.convert(inspection));
				}*/
				
			}
			
			response.setSuccess(true);
			
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
		
	}

	@Override
	public List<ContainerDto> findAll() throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAll();
		for(ContainerModel entity : entities) {
			list.add(containerConverter.convert(entity));
		}
		return list;
	}

	@Override
	public ResponseManagement containerValidation(String containerId,String condition,String clasification) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();	
		try {
			ContainerModel containerValidation= containerRepository.getById(containerId);
			containerValidation.setStatus(containerValidation.getStatus());
			containerValidation.setCondition(condition);
			containerValidation.setClasification(clasification);
			containerRepository.save(containerValidation);
			
			int container = containerRepository.countContainersByVistoBueno(containerId);
			
			if(container==0&&containerValidation.getAppointmentId()!=null) {
				AppointmentModel appointment = appointmentRepository.getById(containerValidation.getAppointmentId());
				if(appointment.getEventType()==1) { //1 = a ENTRADA/GATEIN 2 = salida/GATEOUT
					appointment.setStatus(7);// se libera con el VISTO BUENO 
				}else {
					appointment.setStatus(9);// TERMINA LA OPERACION 
				}
				appointmentRepository.save(appointment);
			}
			
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement saveEvent(String containerId, Integer userId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();	
		try {
			ContainerModel container =  containerRepository.getById(containerId);
			IntegrationModel integration = new IntegrationModel();
			
			integration.setEventDate(container.getDateInspection());
			integration.setEventType(container.getEventType() == 1 ? "ENTRADA" : "SALIDA" );
			integration.setEstimateRequired("");
			integration.setInspected("");
			integration.setInspectedBy(userId.toString());
			integration.setBooking(container.getBokking());
			integration.setFillState("");
			integration.setAlternateUnit("");
			integration.setAssociatedUnit("");
			integration.setTransportType("");
			integration.setSapSaleOrder("");
			integration.setSealNumber("");
			integration.setCustomerIdentifier("");
			integration.setType("");
			integration.setUnitQuality("");
			integration.setModel("");
			integration.setLocation("VERACRUZ");
			integration.setContainer(container.getContainer());
			integration.setModelYear("");
			//integration.setGateEventIdentifier(1);
			integrationRepository.save(integration);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	@Override
	public ResponseManagement saveContainer(ContainerDto containerDto , String warehouse) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			containerDto.setContainerId(UuidProvider.getUUID());
			containerDto.setStatus(1);
			containerDto.setLocation(warehouse);
			ContainerModel newContainer = containerConverter.convert(containerDto);
			
			if(containerRepository.serarchBd(containerDto.getContainer())==0) {
				log.info("Tambien esta entrando aquiiiiiiiiiiiiiiiiiiiiiiiii");
				containerRepository.save(newContainer);
			//} else if() preuntar si hay estatus 7 traer un get darle un nuevo uuid*

			}
			
			else if(containerRepository.findBycontainerStatus(containerDto.getContainer()) == 7) {
				log.info("Entrando aqui merooooooooooooooooooooo");
				 ContainerModel existe = containerRepository.findBycontainerReturn(containerDto.getContainer());
				  
				  existe.setStatus(1);
				  existe.setContainerType(containerDto.getContainerType());
				  existe.setContaierSize(containerDto.getContainerSize());
				  existe.setShippingCompany(containerDto.getShippingCompany());
				  existe.setLocation(warehouse);
				  
				  if(response.getMessage()==""||response.getMessage()==null||response.getMessage().isEmpty()) {
						containerDto.setLog("CONTAINER(" + containerDto.getIdUser() + ", " + containerDto.getContainerId() + "), " + "REGISTRO DE CONTENEDOR");
						appLogService.registerLog(containerDto.getIdUser(), KeyConstants.INSERT, containerDto.getLog());
						response.setSuccess(true);	
					}
				
			}
			
			else if(containerRepository.findBycontainerStatus(containerDto.getContainer()) >= 1 && containerRepository.findBycontainerStatus(containerDto.getContainer()) <= 6) {
				response.setMessage("La unidad "+ containerDto.getContainer() +" ya cuenta con un proceso de cita o ya esta en nuestro almacen verfificar el numero");
			
				if(response.getMessage()==""||response.getMessage()==null||response.getMessage().isEmpty()) {
					containerDto.setLog("CONTAINER(" + containerDto.getIdUser() + ", " + containerDto.getContainerId() + "), " + "REGISTRO DE CONTENEDOR");
					appLogService.registerLog(containerDto.getIdUser(), KeyConstants.INSERT, containerDto.getLog());
					response.setSuccess(true);	
				}
			}
			
			
			/*
			 * else {
			 * if(containerRepository.serarchBdByStatus(containerDto.getContainer())!=0) {
			 * ContainerModel existe =
			 * containerRepository.findBycontainer(containerDto.getContainer());
			 * existe.setStatus(1);
			 * existe.setContainerType(containerDto.getContainerType());
			 * existe.setContaierSize(containerDto.getContainerSize());
			 * existe.setShippingCompany(containerDto.getShippingCompany());
			 * existe.setLocation(warehouse); }else {
			 * response.setMessage("La unidad "+containerDto.getContainer()
			 * +" ya cuenta con un proceso de cita o ya esta en nuestro almacen verfificar el numero"
			 * ); }
			 * 
			 * }
			 */
			 
			if(response.getMessage()==""||response.getMessage()==null||response.getMessage().isEmpty()) {
				containerDto.setLog("CONTAINER(" + containerDto.getIdUser() + ", " + containerDto.getContainerId() + "), " + "REGISTRO DE CONTENEDOR");
				appLogService.registerLog(containerDto.getIdUser(), KeyConstants.INSERT, containerDto.getLog());
				response.setSuccess(true);	
			}
			
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		
		return response;
	}

	@Override
	public ResponseManagement savePregate(ContainerDto containerDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
			try {
				ContainerModel containeredit =  containerRepository.getById(containerDto.getContainerId());
				
				containeredit.setConditionPregate(containerDto.getConditionPregate());
				containeredit.setTypeServicePregate(containerDto.getTypeServicePregate());
				containeredit.setBillTo(containerDto.getBillTo());
				containeredit.setTransportId(containerDto.getTransportId());
				containeredit.setOriginPregate(containerDto.getOriginPregate());
				containeredit.setBuque(containerDto.getBuque());
				containeredit.setBl(containerDto.getBl());
				containeredit.setOperatorName(containerDto.getOperatorName());
				containeredit.setPlate(containerDto.getPlate());
				containeredit.setEconomicNumber(containerDto.getEconomicNumber());
				if(containerDto.getNum()!=1) {
					containeredit.setStatus(2);
				}
				
				
				containerRepository.save(containeredit);
				response.setSuccess(true);
			} catch (Exception ex) {
				response.setSuccess(false);
				response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
				response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
			}
		
		return response;
	}

	@Override
	public List<ContainerDto> getIn(String appointmentId, Integer userId,String warehouse) throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities =  containerRepository.findAllGateIn(warehouse ); 
		for(ContainerModel entity : entities) {
			list.add(containerConverter.convert(entity));
		}
		return list;
	}

	@Override
	public List<ContainerDto> getOut(String appointmentId, Integer userId,String warehouse) throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities =  containerRepository.findAllGateOut(warehouse); 
		for(ContainerModel entity : entities) {
			list.add(containerConverter.convert(entity));
		}
		return list;
	}

	@Override
	public List<ContainerModel> preGate(String appointmentId, Integer userId,String warehouse) throws ConverterException {

		List<ContainerModel> list = new ArrayList<ContainerModel>();
		List<ContainerModel> entities;  
		if(!appointmentId.isEmpty()) {
			entities = containerRepository.findAllPreGate(appointmentId,warehouse); 
			for(ContainerModel entity : entities) {
				list.add(entity);
			}
		}else {
			entities = containerRepository.findAllbyWarehousePregate(warehouse);
			for(ContainerModel entity : entities) {
				log.info(entity.getContainer().toString()+"-------");
				log.info(entity.getContaierSize().toString()+"-------");
				list.add(entity);
				
			}
			
		}
		
		
		
		return list;
	}

	@Override
	public ContainerDto getSingleData(String containerId) throws ConverterException {
		ContainerModel container = containerRepository.getById(containerId);
		return containerConverter.convert(container);
	}

	@Override
	public List<ResumentInformationDto> getDataResumen(Integer userId) throws ConverterException {
		UserRegisterModel information = userRegisterRepository.getcode(userId);
		CatShippingCompanyModel client = catShippingCompanyReposirtory.getidclient(information.getCode());
		List<ResumentInformationDto> list = new ArrayList<ResumentInformationDto>();
		
		return list;
	}

	@Override
	public List<ContainerDto> getContainersbyShippingCompany(Integer userId) throws ConverterException {
		UserRegisterModel information = userRegisterRepository.getcode(userId);
		CatShippingCompanyModel client = catShippingCompanyReposirtory.getidclient(information.getCode());
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAllByClient(client.getShippingCompanyId());
		for(ContainerModel entity : entities) {
				list.add(containerConverter.convert(entity));
			
		}
		return list;
	}

	@Override
	public List<ResumentInformationDto> getDataResumenUsers(Integer userId, String location) {
		List<ResumentInformationDto> list = new ArrayList<ResumentInformationDto>();
		List<?> distinct = containerRepository.getContainerTypes(location);
		
		for (int i = 0; i < distinct.size(); i++) {
			List<?> nomenclaturas = containerRepository.getNomenclaturas(location,distinct.get(i).toString());
			
			for (int j = 0; j < nomenclaturas.size(); j++) {
				
				list.add(ResumentInformationDto.builder()
						.tipo(distinct.get(i).toString())
						.nomenclatura(nomenclaturas.get(j).toString())
						.disponibles(containerRepository.getCount(location, distinct.get(i).toString(), nomenclaturas.get(j).toString(), 1))
						.dañados(containerRepository.getCount(location, distinct.get(i).toString(), nomenclaturas.get(j).toString(), 2))
						.ppti(containerRepository.getCount(location, distinct.get(i).toString(), nomenclaturas.get(j).toString(), 5))
						.total(containerRepository.getCount(location, distinct.get(i).toString(), nomenclaturas.get(j).toString(), 7))
						.suma(0)
						.build());
			}
			
		}
		return list;
	}

	@Override
	public ContainerDto getInformationUnit(String unit) throws ConverterException {
		ContainerDto unitInformation = containerConverter.convert(containerRepository.getByUnit(unit));
		return unitInformation;
	}

	@Override
	public ContainerDto getOne(String containerId) throws ConverterException {
		ContainerModel container = containerRepository.getById(containerId);
		return containerConverter.convert(container);
	}

	@Override
	public List<ContainerDto> getContainersAssignments() throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<AssignmentModel> assignments = assignmentRepository.getAssignments();
		
		for(AssignmentModel assigment : assignments) {
			log.info("INIT GET");			
			if(!assigment.getUnitNumber().isEmpty()) {
				log.info("UNIT NUMBER NOT EMPTY" + assigment.getUnitNumber().toString());			
				ContainerModel container = containerRepository.getByUnitAssigment(assigment.getUnitNumber());
				log.info("Aqui paso--------------------------" + container.toString());
					log.info(container.getContainer() +  "    "+container.getStatus().toString());		
				if(container.getStatus()==5) {
					log.info("Entro IF y e igualo estatus");
					log.info("----------------"+container.toString());
					list.add(containerConverter.convert(container));
				}
				
			}
		}
		log.info("ok");
		return list;
	}

	@Override
	public ResponseManagement gateOutEvent(ContainerDto containerDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();
		try {
			ContainerModel container= containerRepository.getById(containerDto.getContainerId());
			container.setStatus(7);
			
			ContainerHistoricModel containerHistoric = ContainerHistoricModel.builder()
					.containerId(container.getContainerId())
					.registerDate(container.getRegisterDate())
					.location(container.getLocation())
					.container(container.getContainer())
					.containerType(container.getContainerType())
					.contaierSize(container.getContaierSize())
					.shippingCompany(container.getShippingCompany())
					.appointmentId(container.getAppointmentId())
					.status(container.getStatus())
					.bokking(container.getBokking())
					.bookingQuantity(container.getBookingQuantity())
					.eventType(container.getEventType())
					.dateInspection(container.getDateInspection())
					.status(container.getStatus())
					.condition(container.getCondition())
					.clasification(container.getClasification())
					.coments(container.getComents())
					.definition(container.getDefinition())
					.destination(container.getDestination())
					.chassis(container.getChassis())
					.associateUnit(container.getAssociateUnit())
					.mark(container.getMark())
					.horometro(container.getHorometro())
					.technology(container.getTechnology())
					.generatorType(container.getGeneratorType())
					.setPoint(container.getSetPoint())
					.ventilation(container.getVentilation())
					.modelYear(container.getModelYear())
					.nomenclatura(container.getNomenclatura())
					.conditionPregate(container.getConditionPregate())
					.typeServicePregate(container.getTypeServicePregate())
					.billTo(container.getBillTo())
					.transportId(container.getTransportId())
					.buque(container.getBuque())
					.bl(container.getBl())
					.operatorName(container.getOperatorName())
					.plate(container.getPlate())
					.economicNumber(container.getEconomicNumber())
					.aptTo(container.getAptTo())
					.eir(container.getEir())
					.eirName(container.getEirName())
					.eirOut(container.getEirOut())
					.eirOutName(container.getEirOutName())
					.statusQute(container.getStatusQute())
					.quote(container.getQuote())
					.quoteName(container.getQuoteName())
					.daysStay(container.getDaysStay())
					.startDate(container.getStartDate())
					.finalDate(container.getFinalDate())
					.assignedTo(container.getAssignedTo())
					.comnetsQuote(container.getComnetsQuote())
					.build();
			
			containerHistoricRepository.save(containerHistoric);
			
			if(containerDto.getTransmit()==2 ) { 
				log.info(container.getClasification().toString()+"--------Clasifiacion "+containerDto.getQualityEvent()+"--------CalidadEvento ");
				CatShippingCompanyModel customer = catShippingCompanyReposirtory.getById(container.getShippingCompany());
				EventActivityModel activityEvent = EventActivityModel.builder()
						.customerIdentifier(customer.getCode())
						.build();
				eventActivityRepository.save(activityEvent);
				
				String condicion = null;
				switch (container.getClasification()) {
				case "1":
					condicion = "DISPONIBLE";
					break;
				case "2":
					condicion = "DAÑADO";
			        break;
			    case "3":
			    	condicion = "DAÑADO/PTI";
			        break;
			    case "4":
			    	condicion = "EVACUACION";
			        break;
			    case "5":
			    	condicion = "PTI";
			        break;
			    case "6":
			    	condicion = "BLOQUEADO/GX";
			        break;
			    case "7":
			    	condicion = "TOTAL LOOS";
			        break;
			    case "8":
			    	condicion = "VENTA";
			        break;
			    case "9":
			    	condicion = "ACCIDENTADO";
			        break;
				default:
					break;
				}
				
				log.info(condicion+"soy la condicion---------------");
				IntegrationModel event = IntegrationModel.builder()
						.eventDate(DateManagement.todayDate())
						.eventType("GATEOUT")
						.estimateRequired("Y")
						.inspected("Y")
						.inspectedBy(container.getOperatorName())
						.booking(container.getBokking())
						.fillState(container.getConditionPregate()== "VACIO" ? container.getConditionPregate()== "RESERVADO" ? "F"  : "P" : "E" )
						.alternateUnit(null)//No hay dato
						.associatedUnit(container.getAssociateUnit())
						.transportType("TRUCK")
						.sapSaleOrder(null)//No hay dato
						//NO muestra la calidad del contenedor
						.unitQuality(condicion.toString())
						.sealNumber(container.getSecurityStamp())
						.customerIdentifier(customer.getCode())
						.type(container.getContainerType() == 1 ? "CH":container.getContainerType() == 2 ? "OP":container.getContainerType() == 3 ? "DC": container.getContainerType()== 4 ? "GS":
							container.getContainerType()== 5 ? "IS":container.getContainerType() == 6 ? "RF":container.getContainerType() == 7 ? "HC": " ")
						.model(container.getNomenclatura())
						.location(container.getLocation()=="VERACRUZ" ? container.getLocation()=="AGUASCALIENTES" ? container.getLocation()=="ALTAMIRA" ? container.getLocation()=="ENSENADA" ?  container.getLocation()=="PANTACO" ?"ZLO" : "PTO": "ESE" : "ATM": "AGS" :"AGS" )
						.container(container.getContainer())
						.modelYear(container.getModelYear())
						.gateEventIdentifier(eventActivityRepository.getLast())
						.build();
				integrationRepository.save(event);
			}
			containerRepository.save(container);
			
			
			assignmentRepository.daleteUnit(container.getContainer());
			log.info("Si paso");
			containerRepository.deleteById(containerDto.getContainerId());
			
			
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}

	@Override
	public List<ContainerDto> getContainersQuote(String warehouse,int status) throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAllbyWarehouseStatus(warehouse,status);
			for(ContainerModel entity : entities) {
				
				long diff = DateManagement.todayDate().getTime() - entity.getDateInspection().getTime() ;
				TimeUnit time = TimeUnit.DAYS; 
			    long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
			    entity.setDaysStay(" "+diffrence);
			    
				list.add(containerConverter.convert(entity));
			
		}
		return list;
	}

	@Override
	public List<ContainerDto> getContainersQuoteAll(String warehouse) throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAllbyWarehouseStatus2(warehouse);
			for(ContainerModel entity : entities) {
				list.add(containerConverter.convert(entity));
			
		}
		return list;
	}

	@Override
	public ResponseManagement  printQuote(String containerId, String warehouse) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		
		List<InspectionModel> inspections =  inspectorRepository.getAllInspectionsByContainerIdStatus(containerId);
		ContainerModel container = containerRepository.getById(containerId);
		container.setStatusQute(2);
		container.setQuote(pdfGenerationService.pdfQuote(containerId,inspections));
		int numero = containerRepository.getCountQuote(containerId)+1;
		container.setQuoteName("ESTIMADO "+ warehouse +" "+ numero);
		containerRepository.save(container);
		
		response.setSuccess(true);
		
		return response;
	}


	@Override
	public ResponseManagement changeStatus(String containerId,String warehouse)  throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
			try {;
				List<InspectionModel> inspections =  inspectorRepository.getAllInspectionsByContainerIdStatus(containerId);
				ContainerModel containeredit =  containerRepository.getById(containerId);;
				containeredit.setStatusQute(2);
				int numero = containerRepository.getCountQuote(containerId)+1;
				containeredit.setQuoteName("ESTIMADO "+ warehouse +" "+ numero);
				containeredit.setQuote(pdfGenerationService.pdfQuote(containerId, inspections));
				log.info("Si paso de aqui");
				
				
				containerRepository.save(containeredit);
				
				response.setNum(2);
				response.setSuccess(true);
			} catch (Exception ex) {
				response.setSuccess(false);
				response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
				response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
			}
		
		return response;
	}

	@Override
	public ResponseManagement saveInformationRepair(ContainerDto containerDto) throws ConverterException {
		
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		
		try {
			ContainerModel container = containerRepository.getById(containerDto.getContainerId());
			container.setAssignedTo(containerDto.getAssignedTo());
			container.setStartDate(containerDto.getStartDate());
			container.setFinalDate(containerDto.getFinalDate());
			
			
			containerRepository.save(container);
			
			response.setSuccess(true);
			
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + e.toString());
		}
		
		return response;
	}

	public ResponseManagement saveInspectionContainer(List<MultipartFile> file, ContainerDto containerDto, List<String> index)throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();	
		
		
		
		
		try {
			
			ObjectMapper inspectionMapper = new ObjectMapper();
			List<InspectionDto> inspections = inspectionMapper.readValue(containerDto.getInspectionList(), new TypeReference<List<InspectionDto>>() { });
			List<InspectionModel> entities = new ArrayList<InspectionModel>();
			List<PhotoModel> photo = new ArrayList<PhotoModel>();
			ContainerModel containeredit = containerRepository.getById(containerDto.getContainerId());
			
			containeredit.setDateInspection(containerDto.getDateInspection());
			containeredit.setClasification(containerDto.getClasification());
			containeredit.setCondition(containerDto.getCondition());
			containeredit.setModelYear(containerDto.getModelYear());
			containeredit.setAptTo(containerDto.getAptTo());
			containeredit.setNomenclatura(containerDto.getNomenclatura());
			containeredit.setComents(containerDto.getComents());
			containeredit.setAssociateUnit(containeredit.getContainerType()==4 ? containerDto.getAssociateUnitGenset() :containerDto.getAssociateUnit() );
			containeredit.setTechnology(containeredit.getContainerType()==4 ? "" : containerDto.getTechnology() );
			containeredit.setMark(containeredit.getContainerType()==4 ? "" :containerDto.getMark());
			containeredit.setChassis(containerDto.getChassis());
			containeredit.setGeneratorType(containeredit.getContainerType()==4 ? containerDto.getGeneratorType() : "" );
			containeredit.setHorometro(containeredit.getContainerType()==4 ? containerDto.getHorometro() : "");
			containeredit.setDiesel(containeredit.getContainerType()==4 ? containerDto.getDiesel() : "" );
			containeredit.setStatus(3); 
			containerDto.setStatusQute(containeredit.getStatusQute());
			
			
			if(containeredit.getTypeServicePregate().equals("MERCHANT")) {
				//containeredit.setStatus(50); 
				containeredit.setStatusQute(1);
			}
			
			
			
			for(InspectionDto inspection : inspections ){
					
					if(inspection.getCustomerType()==1) {
						containeredit.setStatusQute(1);
					}
					if(!inspectorRepository.existsById(inspection.getInspectionId())) {
						inspection.setPhoto(inspection.getInspectionId());
						inspection.setInspectionId(UuidProvider.getUUID());
						inspection.setContainerId(containeredit.getContainerId());
						inspection.setStatus(2);
						entities.add(inspectionConverter.convert(inspection));
					}
					
				}
			
			inspectorRepository.saveAll(entities);
			/*log.info(index+"----------------------");
			for(InspectionDto inspection : inspections ){
				
				if(inspection.getCustomerType()==1) {
					containeredit.setStatusQute(1);
				}
				if(!inspectorRepository.existsById(inspection.getInspectionId())) {
					inspection.setPhoto(inspection.getInspectionId());
					inspection.setInspectionId(UuidProvider.getUUID());
					inspection.setContainerId(containeredit.getContainerId());
					inspection.setStatus(2);
					entities.add(inspectionConverter.convert(inspection));
				}
				Set<String> miConjunto = new HashSet<>(index);
				for(String num2 : miConjunto) {
					int contador = 0;
					for(String num : index) {
						if(num2.equals(num)) {
							contador++;
						}
					}
					log.info(num2+" "+contador);
					for (int i = 1; i <= contador; i++) {
					}
				}
			}*/
			
		/*	Set<String> miConjunto = new HashSet<>(index);
			for(String num2 : miConjunto) {
				int contador = 0;
				for(String num : index) {
					
					if(num2.equals(num)) {
						contador++;
					}
					
				}
				
				for (int i = 0; i < contador; i++) {
					
					photo.add(PhotoModel.builder()
							.photoId(UuidProvider.getUUID())
							.containerId(inspections.get(i).getInspectionId())
							.photo(file.get(i).getBytes())
							.build());
				}
				
			
				
				
				
			}
			
			photoRepository.saveAll(photo);*/
							
			
			
			if(containeredit.getAppointmentId()!=null) {
				AppointmentModel appointmentStatus=appointmentRepository.getById(containeredit.getAppointmentId());
				if(containerRepository.countContainers(containeredit.getAppointmentId())==0) {
					appointmentStatus.setStatus(4);//Status EN PROCESO
				}else {
					appointmentStatus.setStatus(3);//Status EN INSPECCION
				}
				appointmentRepository.save(appointmentStatus);
			}
			
			
			int valor = containerRepository.getCountEir();
			valor=valor +1;
			containeredit.setEirName("EIR-"+containeredit.getLocation()+"-0"+valor);
			containeredit.setEir(pdfGenerationService.pdfEir(containerDto.getContainerId(),containerDto.getDataUrl()));
			
			
			
			containerRepository.save(containeredit);
			
			response.setPdf(containeredit.getContainerId());
			response.setNum(containerDto.getNum());
			response.setSuccess(false);
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}

	@Override
	public ResponseManagement newInspectionContainer(ContainerDto containerDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		
		try {
			
			ContainerModel containeredit = containerRepository.getById(containerDto.getContainerId());
			
			containeredit.setDateInspection(containerDto.getDateInspection());
			containeredit.setClasification(containerDto.getClasification());
			containeredit.setCondition(containerDto.getCondition());
			containeredit.setModelYear(containerDto.getModelYear());
			containeredit.setAptTo(containerDto.getAptTo());
			containeredit.setNomenclatura(containerDto.getNomenclatura());
			containeredit.setComents(containerDto.getComents());
			containeredit.setAssociateUnit(containeredit.getContainerType()==4 ? containerDto.getAssociateUnitGenset() :containerDto.getAssociateUnit() );
			containeredit.setTechnology(containeredit.getContainerType()==4 ? "" : containerDto.getTechnology() );
			containeredit.setMark(containeredit.getContainerType()==4 ? "" :containerDto.getMark());
			containeredit.setChassis(containerDto.getChassis());
			containeredit.setGeneratorType(containeredit.getContainerType()==4 ? containerDto.getGeneratorType() : "" );
			containeredit.setHorometro(containeredit.getContainerType()==4 ? containerDto.getHorometro() : "");
			containeredit.setDiesel(containeredit.getContainerType()==4 ? containerDto.getDiesel() : "" );
			containeredit.setStatus(3); 
			containerDto.setStatusQute(containeredit.getStatusQute());
			
			
			if(containeredit.getTypeServicePregate().equals("MERCHANT")) {
				//containeredit.setStatus(50); 
				containeredit.setStatusQute(1);
			}else {
				int cantidad=	containerRepository.getInspectionsMerchant(containeredit.getContainerId());
				
				if(cantidad!=0) {
					containeredit.setStatusQute(1);
				}
				
			}
			
			
			if(containeredit.getAppointmentId()!=null) {
				AppointmentModel appointmentStatus=appointmentRepository.getById(containeredit.getAppointmentId());
				if(containerRepository.countContainers(containeredit.getAppointmentId())==0) {
					appointmentStatus.setStatus(4);//Status EN PROCESO
				}else {
					appointmentStatus.setStatus(3);//Status EN INSPECCION
				}
				appointmentRepository.save(appointmentStatus);
			}
			
			
			int valor = containerRepository.getCountEir();
			valor=valor +1;
			containeredit.setEirName("EIR-"+containeredit.getLocation()+"-0"+valor);
			containeredit.setEir(pdfGenerationService.pdfEir(containerDto.getContainerId(),containerDto.getDataUrl()));
			
			
			
			containerRepository.save(containeredit);
			
			response.setPdf(containeredit.getContainerId());
			response.setNum(containerDto.getNum());
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}


	@Override
	public ContainerDto getContainerInformation(String containerId) throws ConverterException {
		ContainerDto container = new ContainerDto();
		ContainerModel entities = containerRepository.getContainerInformation(containerId);
		container=containerConverter.convert(entities);
		return container;
	}

	@Override
	public List<ContainerDto> getUnitsFilter(String type, String size, String clasification, String warehous) throws ConverterException {
		List<ContainerDto> containers = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.getUnitsFilter(type,size,clasification,warehous);
		
		for(ContainerModel container : entities) {
			containers.add(containerConverter.convert(container));
		}
		return containers;
	}

	@Override
	public List<ContainerDto> getUnitsValidationAppointment(String warehouse, Integer userId) throws ConverterException {
		List<AppointmentModel> appointments = appointmentRepository.getUseAppointmentTable(userId);
		List<ContainerDto> containers = new ArrayList<ContainerDto>();
		for(AppointmentModel app : appointments) {
			List<ContainerModel> entities = containerRepository.getUnitsValidationAppointment(app.getAppointmentId());
			for(ContainerModel ent : entities) {
				
				containers.add(containerConverter.convert(ent));
			}
		}
		
		
		return containers;
	}

	@Override
	public ResponseManagement changeStatusApproved(String containerId,String coments,Integer status) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		ContainerModel container = containerRepository.getById(containerId);
		if(status == 3) {
			container.setAprovedQuote(DateManagement.todayDate());
			List<InspectionModel> inspections =  inspectorRepository.getAllInspectionsByContainerIdStatus(containerId);
			ContainerModel containeredit =  containerRepository.getById(containerId);;
			containeredit.setQuoteName(containeredit.getQuoteName());
			containeredit.setQuote(pdfGenerationService.pdfQuote(containerId, inspections));
		}
		container.setStatusQute(status);
		container.setComents(coments);
		containerRepository.save(container);
		response.setSuccess(true);
		return response;
	}

	@Override
	public List<ContainerDto> getAllContainersQuote(String warehouse) throws ConverterException {
		List<ContainerDto> list = new ArrayList<ContainerDto>();
		List<ContainerModel> entities = containerRepository.findAllcontainersQuote(warehouse);
			for(ContainerModel entity : entities) {
				
				long diff = DateManagement.todayDate().getTime() - entity.getDateInspection().getTime() ;
				TimeUnit time = TimeUnit.DAYS; 
			    long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
			    entity.setDaysStay(" "+diffrence);
			    
				list.add(containerConverter.convert(entity));
			
		}
		return list;
	}

	
		  @Override 
		  public InspectionShippingDto getPreLabor(String inspectionId) {
			  	
			  		CatShippingCompanyModel infoPreLabor = catShippingCompanyReposirtory.getPreLaborFinalFinal(inspectionId);
			
			  		log.info(infoPreLabor.getLabor()+"--------4-----------");
			  		InspectionShippingDto entity = InspectionShippingDto.builder()
			  				.labor(infoPreLabor.getLabor())
			  				.build();
			 
					/*
					 * List<InspectionShippingDto> list = new ArrayList<InspectionShippingDto>();
					 * List<InspectionModel> inspectionInfo =
					 * inspectorRepository.getInspectionForLabor(inspectionId);
					 * 
					 * for(InspectionModel entity : inspectionInfo) {
					 * 
					 * List<ContainerModel> infoPreContainer =
					 * containerRepository.getPreLabor(entity.getContainerId());
					 * 
					 * for(ContainerModel listEntity : infoPreContainer) {
					 * 
					 * CatShippingCompanyModel infoPreLabor =
					 * catShippingCompanyReposirtory.getPreLaborFinal(listEntity.getShippingCompany(
					 * ));
					 * 
					 * list.add(infoPreLabor.getLabor().toArray()); }
					 * 
					 * 
					 * }
					 */
			  		log.info(entity.getLabor()+"-----------5--------");
		  
		  return entity; 
		  }
	 

}
