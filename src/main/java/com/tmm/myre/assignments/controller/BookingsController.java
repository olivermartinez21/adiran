package com.tmm.myre.assignments.controller;

import java.io.ByteArrayInputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.assignments.dto.AssignmentDto;
import com.tmm.myre.assignments.dto.AssignmentsFullDto;
import com.tmm.myre.assignments.dto.BookingDto;
import com.tmm.myre.assignments.service.core.IAssignmentService;
import com.tmm.myre.assignments.service.core.IAssignmentsService;
import com.tmm.myre.assignments.service.core.IBookingService;
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
import com.tmm.myre.deliveryOrders.model.DeliveryOrderModel;
import com.tmm.myre.deliveryOrders.repository.IDeliveryOrderRepository;
import com.tmm.myre.deliveryOrders.service.core.IDeliveryOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(BookingsController.HOME)
public class BookingsController extends AbstractMyreController{

public static final String HOME = PREFIX_ASSIGNMENTS + "bookings";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IBookingService bookingService;
	
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
	
	@Autowired
	private IAssignmentService assignmentService;
	
	@Autowired
	private IDeliveryOrderRepository deliveryOrderRepository;
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<BookingDto> getDataTable() {
		try {
			return bookingService.getDataTable(getWarehouse());
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
	
	@GetMapping("getDataTableOrdersByBookingId")
	@ResponseBody
	public List<DeliveryOrderDto> getDataTableOrdersByBookingId(@RequestParam(required = true) String bookingId) {
		try {
			return deliveryOrderService.getDataTableByAssignmentID(bookingId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	
	

	@PostMapping("createBooking")
	@ResponseBody
	public ResponseManagement createBooking(@ModelAttribute("bookingDto") BookingDto bookingDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
		return bookingService.createBooking(bookingDto);
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
	
	@PostMapping("updateDeliveryOrder")
	@ResponseBody
	public ResponseManagement updateDeliveryOrder(@ModelAttribute("deliveryOrderDto") DeliveryOrderDto deliveryOrderDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return deliveryOrderService.editDeliveryOrder(deliveryOrderDto);
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
	public ResponseManagement assignation(@ModelAttribute("assignmentDto") AssignmentDto assignmentDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			return assignmentService.assignation(assignmentDto);
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
	
	@GetMapping("assignmentInformation")
	@ResponseBody
	List<AssignmentDto> assignmentInformation(@RequestParam(required = true) String bookingId) {
		try {
			return assignmentService.assignmentInformation(bookingId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	
	@GetMapping("assignmentInformationEdit")
	@ResponseBody
	List<AssignmentDto> assignmentInformationEdit(@RequestParam(required = true) String deliveryOrderId ) {
		try {
			return assignmentService.assignmentInformationEdit(deliveryOrderId,getWarehouse());
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getInformationDelivery")
	@ResponseBody
	DeliveryOrderDto getInformationDelivery(@RequestParam(required = true) String deliveryOrderId) {
		try {
			return deliveryOrderService.getInformation(deliveryOrderId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	

	@GetMapping("getContainersStock")
	@ResponseBody
	List<ContainerDto> getContainersStock(@RequestParam(required = true) String location) {
		try {
			return containerService.getContainersStock(location);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	@PostMapping("deleteAssignemnt")
	@ResponseBody
	public ResponseManagement deleteAssignemnt(@ModelAttribute("assignmentId") String assignmentId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			return assignmentService.deleteAssignemnt(assignmentId);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	@PostMapping("deleteDeliveryOrder")
	@ResponseBody
	public ResponseManagement deleteDeliveryOrder(@ModelAttribute("deliveryOrderId") String deliveryOrderId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			
			return deliveryOrderService.deleteDeliveryOrder(deliveryOrderId);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	@PostMapping("printDeliveryOrder")
	@ResponseBody
	public ResponseManagement printDeliveryOrder(@ModelAttribute("deliveryOrderId") String deliveryOrderId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			
			return deliveryOrderService.printDeliveryOrder(deliveryOrderId);
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.INSERT);
			}
			return response;
		}
	
	@RequestMapping(value = "/PDF_order", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> certificateView(@RequestParam(required = true) String deliveryOrderId) {
		try {
			DeliveryOrderModel pdf = deliveryOrderRepository.getById(deliveryOrderId);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",  "inline; filename="+pdf.getFileName()+getFileExtension(pdf.getFileType()));
			ByteArrayInputStream body = new ByteArrayInputStream(pdf.getFileContent());
			return ResponseEntity
					.ok()
					.headers(headers)
					.contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(body));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return null;
		}
	}
	
	@GetMapping("getUnitInfo")
	@ResponseBody
	public ContainerDto getContainerInformation(@RequestParam("containerId") String containerId) {
		
		try {
			return containerService.getContainerInformation(containerId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getUnitsFilter")
	@ResponseBody
	public List<ContainerDto> getUnitsFilter(String type,String size, String clasification) {
		
		try {
			return containerService.getUnitsFilter(type,size,clasification,getWarehouse());
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	
}
