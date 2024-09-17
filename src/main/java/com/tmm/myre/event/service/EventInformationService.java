package com.tmm.myre.event.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.configuration.secondary.EventActivityModel;
import com.tmm.myre.base.configuration.secondary.EventActivityRepository;
import com.tmm.myre.base.configuration.secondary.IIntegrationRepository;
import com.tmm.myre.base.configuration.secondary.IntegrationConverter;
import com.tmm.myre.base.configuration.secondary.IntegrationModel;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.event.converter.EventInformationConverter;
import com.tmm.myre.event.dto.EventInformationDto;
import com.tmm.myre.event.model.EventInformationModel;
import com.tmm.myre.event.repository.IEventInformationRepository;
import com.tmm.myre.event.service.core.IEventInformationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("eventInformationService")
public class EventInformationService implements IEventInformationService {
	
	@Autowired
	private EventInformationConverter eventInformationConverter;
	
	@Autowired
	private IEventInformationRepository eventInformationRepository;
	
	@Autowired
	private IContainerRepository containerRepository;
	
	@Autowired
	private IntegrationConverter integrationConverter;
	
	@Autowired
	private IIntegrationRepository integrationRepository;
	
	@Autowired
	private EventActivityRepository eventActivityRepository;
	
	@Autowired
	private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;

	@Override
	public ResponseManagement saveEventInformation(EventInformationDto eventInformationDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			ContainerModel container= containerRepository.getById(eventInformationDto.getContainerId());
			container.setContaierSize(eventInformationDto.getModel());
			container.setClasification(eventInformationDto.getUnitQuality());
			
			eventInformationDto.setModelYear(container.getModelYear());
			
			EventInformationModel event = eventInformationConverter.convert(eventInformationDto);
			event.setEventId(UuidProvider.getUUID());
			if(Integer.parseInt(event.getTransmit())==2) {
				EventActivityModel activityEvent = EventActivityModel.builder()
						.customerIdentifier(eventInformationDto.getCustomerIdentifier())
						.build();
				eventActivityRepository.save(activityEvent);
				
				IntegrationModel integration = integrationConverter.convert(eventInformationDto);
				integration.setEventDate(event.getEventDate());
				integration.setEventType(Integer.parseInt(event.getEventType())== 1 ? "GATEIN" : "GATEOUT" );
				integration.setEstimateRequired(Integer.parseInt(event.getEventType())== 1 ? "Y" : "N" );
				integration.setInspected(container.getStatus() !=1 ? "Y" : "N");
				//integration.setTransportType();
				//integration.setUnitQuality();
				//integration.setType("");
				//integration.setUnitQuality("");
				//integration.setModel("");
				//integration.setLocation("VERACRUZ");
				//integration.setContainer(container.getContainer());
				//integration.setModelYear("");
				//integration.setGateEventIdentifier(eventActivityRepository.getLast());
				integrationRepository.save(integration);
			}
			
			containerRepository.save(container);
			eventInformationRepository.save(event);
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}

	@Override
	public List<EventInformationDto> getEvents(String containerId) throws ConverterException {
		List<EventInformationDto> list = new ArrayList<EventInformationDto>();
		List<EventInformationModel> events = eventInformationRepository.findEvents(containerId);
		for(EventInformationModel event : events ) {
			list.add(eventInformationConverter.convert(event));
		}
		
		return list;
	}

	@Override
	public ResponseManagement saveNewEvent(ContainerDto containerDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			log.info(containerDto.getQualityEvent()+"------------------");
			ContainerModel container= containerRepository.getById(containerDto.getContainerId());
			container.setContainerType(containerDto.getContainerType());
			container.setClasification(containerDto.getClasification());
			container.setCondition(containerDto.getCondition());
			container.setShippingCompany(containerDto.getShippingCompany());
			container.setNomenclatura(containerDto.getNomenclatura());
			container.setTypeServicePregate(containerDto.getTypeServicePregate());
			container.setStatus(4);
			
			if(container.getStatusQute()==null) {
				container.setStatusQute(1);
			}
			
			//transmit
			
			containerRepository.save(container);
			CatShippingCompanyModel customer = catShippingCompanyReposirtory.getById(containerDto.getShippingCompany());
			if(containerDto.getTransmit()==2 ) {
				EventActivityModel activityEvent = EventActivityModel.builder()
						.customerIdentifier(customer.getCode())
						.build();
				eventActivityRepository.save(activityEvent);
				
				IntegrationModel event = IntegrationModel.builder()
						.eventDate(DateManagement.todayDate())
						.eventType("GATEIN")
						.estimateRequired("Y")
						.inspected("Y")
						.inspectedBy(container.getOperatorName()) //PERSONA QUE INSPECCIONO OPERADOR
						.booking(container.getBokking())
						.fillState(container.getConditionPregate()== "VACIO" ? container.getConditionPregate()== "RESERVADO" ? "F"  : "P" : "E" )
						.alternateUnit(null)//nulo
						.associatedUnit(container.getAssociateUnit())
						.transportType("TRUCK")
						.sapSaleOrder(null)//nlo
						.unitQuality(containerDto.getQualityEvent().length()>=10 ? containerDto.getQualityEvent().substring(0,9) : containerDto.getQualityEvent())
						.sealNumber(null)//SELLO DE SEGURIDAD
						.customerIdentifier(customer.getCode())
						.type(container.getContainerType() == 1 ? "CH":container.getContainerType() == 2 ? "OP":container.getContainerType() == 3 ? "DC": container.getContainerType()== 4 ? "GS":
							container.getContainerType()== 5 ? "IS":container.getContainerType() == 6 ? "RF":container.getContainerType() == 7 ? "HC": " ") //TIPO DE UNIDAD
						.model(container.getNomenclatura())
						//DE DQUE LOCALIAD ES CONTENEDOR
						.location(container.getLocation()=="VERACRUZ" ? container.getLocation()=="AGUASCALIENTES" ? container.getLocation()=="ALTAMIRA" ? container.getLocation()=="ENSENADA" ?  container.getLocation()=="PANTACO" ?"ZLO" : "PTO": "ESE" : "ATM": "AGS" :"AGS" )
						.container(container.getContainer())
						.modelYear(container.getModelYear())
						.gateEventIdentifier(eventActivityRepository.getLast())
						.build();
				
				integrationRepository.save(event);
			}
			
			  
			response.setSuccess(true);
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		return response;
	}
	
}
