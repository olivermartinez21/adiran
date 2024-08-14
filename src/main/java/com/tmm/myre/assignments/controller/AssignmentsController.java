package com.tmm.myre.assignments.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmm.myre.assignments.dto.AssignmentsDto;
import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.assignments.service.core.IAssignmentsService;
import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.catalog.dto.CatCustomerDto;
import com.tmm.myre.catalog.dto.CatFinalClientDto;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;
import com.tmm.myre.catalog.service.core.ICatCustomerService;
import com.tmm.myre.catalog.service.core.ICatFinalClientService;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
import com.tmm.myre.catalog.service.core.ICatTransportCompanyService;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.deliveryOrders.dto.DeliveryOrderDto;
import com.tmm.myre.deliveryOrders.service.core.IDeliveryOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(AssignmentsController.HOME)
public class AssignmentsController extends AbstractMyreController{

public static final String HOME = PREFIX_ASSIGNMENTS + "assignments";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IAssignmentsService assignmentsService;
	
	@Autowired
	private IDeliveryOrderService deliveryOrderService;
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private ICatCustomerService catCustomerService;

	@Autowired
	private ICatTransportCompanyService catTransportCompanyService;
	
	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;
	
	@Autowired
	private ICatFinalClientService catFinalClientService;
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<AssignmentsDto> getDataTable() {
		try {
			return assignmentsService.getDataTable(getWarehouse());
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getDataTableFull")
	@ResponseBody
	public List<AssignmentsFullDto> getDataTableFull() {
		try {
			return assignmentsService.getDataTableFull(getWarehouse());
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getDataTableOrders")
	@ResponseBody
	public List<DeliveryOrderDto> getDataTableOrders() {
		try {
			return deliveryOrderService.getDataTable(getWarehouse());
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getDataTableOrdersByAssignmentID")
	@ResponseBody
	public List<DeliveryOrderDto> getDataTableOrdersByAssignmentID(@RequestParam(required = true) String assigntmentId) {
		try {
			return deliveryOrderService.getDataTableByAssignmentID(assigntmentId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	
	

	@PostMapping("createBooking")
	@ResponseBody
	public ResponseManagement createBooking(@ModelAttribute("assignmentsDto") AssignmentsDto assignmentsDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
				return assignmentsService.createBooking(assignmentsDto);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	@PostMapping("createBookingFull")
	@ResponseBody
	public ResponseManagement createBookingFull(@ModelAttribute("assignmentsFullDto") AssignmentsFullDto assignmentsFullDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return assignmentsService.createBookingFull(assignmentsFullDto);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	
	@PostMapping("createDeliveryOrder")
	@ResponseBody
	public ResponseManagement createDeliveryOrder(@ModelAttribute("deliveryOrderDto") DeliveryOrderDto deliveryOrderDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return deliveryOrderService.createDeliveryOrder(deliveryOrderDto);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	
	@GetMapping("getInformationUnit")
	@ResponseBody
	public ContainerDto getInformationUnit(@RequestParam(required = true) String unit) {
		try {
			return containerService.getInformationUnit(unit);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@PostMapping("assignation")
	@ResponseBody
	public ResponseManagement assignation(@ModelAttribute("deliveryOrderDto") DeliveryOrderDto deliveryOrderDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			
			//return deliveryOrderService.createDeliveryOrder(deliveryOrderDto);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	@ModelAttribute("catClients")
	List<CatCustomerDto> catClients() {
		try {
			return catCustomerService.catClients();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	
	@ModelAttribute("catTransport")
	List<CatTransportCompanyDto> catTransport() {
		try {
			return catTransportCompanyService.catTransport();
		} catch(Exception ex) {
			log.error(ex.toString());
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
	
	@ModelAttribute("catFinalClient")
	List<CatFinalClientDto> catFinalClient() {
		try {
			return catFinalClientService.catFinalClient();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	
	
	
	
}
