package com.tmm.myre.containers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.catalog.dto.CatComponentDto;
import com.tmm.myre.catalog.dto.CatDamageDto;
import com.tmm.myre.catalog.dto.CatRepairDto;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.service.core.ICatComponentService;
import com.tmm.myre.catalog.service.core.ICatDamageService;
import com.tmm.myre.catalog.service.core.ICatRepairService;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ResumentInformationDto;
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.event.dto.EventInformationDto;
import com.tmm.myre.event.service.core.IEventInformationService;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.service.core.IInspectionsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(ContainerClientController.HOME)
public class ContainerClientController extends AbstractMyreController{
	

	public static final String HOME = PREFIX_CONTAINER + "containerClient";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private ICatDamageService catDamageService;
	
	@Autowired
	private IEventInformationService eventInformationService;
	
	@Autowired
	private IInspectionsService inspectionsService;

	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;
	
	@Autowired
	private ICatRepairService catRepairService;
	
	@Autowired
	private ICatComponentService catComponentService;
	
	/*@ModelAttribute("catShipping")
	List<CatCustomerDto> catShipping() {
		try {
			return catCustomerService.catShipping();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}*/
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<ContainerDto> getDataTable(@RequestParam(required = true) String appointmentId ,Integer userId) {
		try {
			return containerService.getContainersbyShippingCompany(userId);
		} catch(Exception ex) {
			return null;
		}
	}
	
	@GetMapping("getDataResumen")
	@ResponseBody
	public List<ResumentInformationDto> getDataResumen(@RequestParam(required = true) Integer userId) {
		try {
			return containerService.getDataResumen(userId);
		} catch(Exception ex) {
			return null;
		}
	}

	@ModelAttribute("catShipping")
	List<CatShippingCompanyDto> catShipping() {
		try {
			return catShippingCompanyService.catShipping();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@PostMapping("saveUpdateContainer")
	@ResponseBody
	public ResponseManagement saveUpdateContainer(@ModelAttribute("containerDto") ContainerDto containerDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			//containerDto.setModelYear("");
			return containerService.saveUpdateContainer(containerDto);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	@PostMapping("containerValidation")
	@ResponseBody
	public ResponseManagement containerValidation(@RequestParam(required = true)String containerId,String condition , String clasification) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
				return containerService.containerValidation(containerId,condition,clasification);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}

	
	@PostMapping("saveEvent")
	@ResponseBody
	public ResponseManagement saveContainerEdit(@RequestParam(required = true) String containerId ,Integer userId ) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			return containerService.saveEvent(containerId,userId);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	 
	@PostMapping("saveContainer")
	@ResponseBody
	public ResponseManagement saveContainer(@ModelAttribute("containerDto") ContainerDto containerDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return containerService.saveContainer(containerDto,getWarehouse());
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	
	@PostMapping("saveEventInformation")
	@ResponseBody
	public ResponseManagement saveEventInformation(@ModelAttribute("eventInformationDto") EventInformationDto eventInformationDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return eventInformationService.saveEventInformation(eventInformationDto);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	

	@GetMapping("getInspections")
	@ResponseBody
	public List<InspectionDto> getInspections(@RequestParam(required = true) String containerId) {
		try {
			return inspectionsService.getInspectionsByContainer(containerId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getDamageInformation")
	@ResponseBody
	public List<CatDamageDto> getDamageInformation(@RequestParam(required = true) Integer containerType) {
		try {
			return catDamageService.getDamageInformationByContainerType(containerType);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@ModelAttribute("getRepairInformation")
	List<CatRepairDto> getRepairInformation() {
		try {
			return catRepairService.getRepairInformation();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getComponentIformation")
	@ResponseBody
	public List<CatComponentDto> getComponentIformation(@RequestParam(required = true) String containerType , String Section) {
		try {
			return catComponentService.getComponentIformationByContainerTypeAndSection(containerType,Section);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getSectionInformation")
	@ResponseBody
	public List<?> getSectionInformation(@RequestParam(required = true) String containerType) {
		try {
			return catComponentService.getSectionByContainerType(containerType);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getEvents")
	@ResponseBody
	public List<EventInformationDto> getEvents(@RequestParam(required = true) String containerId) {
		try {
			return eventInformationService.getEvents(containerId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	/*@PostMapping("inspectionSave")
	@ResponseBody
	public ResponseManagement inspectionSave(@RequestParam(required = true)String inspectionList) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		
		try {
			return inspectionsService.inspectionSave(inspectionList);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}*/

}
