package com.tmm.myre.appointments.controller;

import java.io.ByteArrayInputStream;
import java.sql.Date;
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
import com.tmm.myre.appointments.service.core.IAppointmentService;
import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.AppUserDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.service.core.IAppUserService;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.catalog.dto.CatCustomerDto;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;
import com.tmm.myre.catalog.service.core.ICatCustomerService;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
import com.tmm.myre.catalog.service.core.ICatTransportCompanyService;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.service.core.IInspectionsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(AppointmentController.HOME)
public class AppointmentController extends AbstractMyreController {

	public static final String HOME = PREFIX_APPOINTMENT + "appointment";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IAppUserService appUserService;
	
	@Autowired
	private IAppointmentService appointmentService;
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private IInspectionsService inspectionsService;
	
	@Autowired
	private ICatCustomerService catCustomerService;
	
	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;
	
	@Autowired
	private ICatTransportCompanyService catTransportCompanyService;
	
	
	
	@ModelAttribute("usersDescription")
	List<AppUserDto> getMaterials() {
		try {
			return appUserService.findAll();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@ModelAttribute("containers")
	List<ContainerDto> containers() {
		try {
			return containerService.findAll();
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
	
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<AppointmentDto> getDataTable(@RequestParam(required = true) Integer userId) {
		try {
			return appointmentService.getDataTable(userId,getWarehouse(),getLoggedUser().getUserRole());
			//return appointmentService.getDataTable(userId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getFilterDate")
	@ResponseBody
	public List<AppointmentDto> getFilterDate(@RequestParam(required = true) Date startDate, Date lastDate ) {
		try {
			//return appointmentService.getDataTable(userId,getWarehouse());
			return appointmentService.getDataTableByDate(startDate,lastDate,getWarehouse());
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getSingleData")
	@ResponseBody
	public AppointmentDto getSingleData(@RequestParam("appointmentId") String appointmentId , Integer idUser) {
		try {
			return appointmentService.getSingleData(appointmentId,idUser,getWarehouse());
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@PostMapping("saveUpdateAppointment")
	@ResponseBody
	public ResponseManagement saveAppointment(@ModelAttribute("appointmentDto") AppointmentDto appointmentDto) {
		ResponseManagement response = ResponseManagement.builder().operation(appointmentDto.getOperation()).success(false).build();
		try {
			
			if(appointmentDto.getOperation().equals(KeyConstants.INSERT)) {
				return appointmentService.saveAppointment(appointmentDto);
			}else if(appointmentDto.getOperation().equals(KeyConstants.UPDATE)) {
				return appointmentService.updateAppointment(appointmentDto);
			}
		} catch(Exception ex) {
				log.error(ex.toString());
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(appointmentDto.getOperation());
			}
			return response;
		}
	
	
	@GetMapping("getContainers")
	@ResponseBody
	public List<ContainerDto> getContainers(@RequestParam(required = true) String appointmentId , Integer userId) {
		try {
			return null;
			//return containerService.getContainers(appointmentId, userId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@PostMapping("appointmentValidation")
	@ResponseBody
	public ResponseManagement appointmentValidation(@RequestParam("appointmentId") String appointmentId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			return appointmentService.appointmentValidation(appointmentId);
		} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.UPDATE);
		}
		return response;
	}
	
	
	@GetMapping("getInspections")
	@ResponseBody
	public List<InspectionDto> getInspections(@RequestParam(required = true) String appointmentId) {
		try {
			log.info("LLEGO AL CONTROLADOR");
			return inspectionsService.getInspections(appointmentId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	
	@RequestMapping(value = "/PDF_folio", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> certificateView(@RequestParam(required = true) String appointmentId) {
		try {
			AppointmentDto pdf = appointmentService.getOne(appointmentId);
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
	
	@GetMapping("getAddressClient")
	@ResponseBody
	public CatCustomerDto getAddressClient(@RequestParam("customerId") String customerId) {
		try {
			return catCustomerService.getAddressClient(customerId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
}
