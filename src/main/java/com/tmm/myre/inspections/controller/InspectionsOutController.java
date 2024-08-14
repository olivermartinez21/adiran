package com.tmm.myre.inspections.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.appointments.dto.AppointmentDto;
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
import com.tmm.myre.catalog.service.core.ICatTransportCompanyService;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.inspections.dto.InspecctionOutDto;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.service.core.IInspectionsOutService;
import com.tmm.myre.inspections.service.core.IInspectionsService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(InspectionsOutController.HOME)
public class InspectionsOutController extends AbstractMyreController {

	public static final String HOME = PREFIX_INSPECTION + "inspectionOut";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;

	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private IInspectionsOutService inspectionsOutService;
	
	@GetMapping("getContainersAssignments")
	@ResponseBody
	public List<ContainerDto> getContainersAssignments() {
		try {
			log.info("controler entraceeeeeeeeeeeeeeeeeeeeee");
			return containerService.getContainersAssignments();
		} catch(Exception ex) {
			log.info(ex.toString());
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
	
	@PostMapping("updateContainerInformationOut")
	@ResponseBody
	public ResponseManagement updateContainerInformationOut(@ModelAttribute("containerDto") ContainerDto containerDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
				return containerService.updateContainerInformationOut(containerDto);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	@RequestMapping(value = "/PDF_EIR_OUT", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> certificateView(@RequestParam(required = true) String containerId) {
		try {
			ContainerDto pdf = containerService.getOne(containerId);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",  "inline; filename="+pdf.getEirOutName()+getFileExtension(pdf.getEirOutName()));
			ByteArrayInputStream body = new ByteArrayInputStream(pdf.getEirOut());
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
	
	
	@PostMapping("saveInspectionOut")
	@ResponseBody
	public ResponseManagement saveInspectionOut(@ModelAttribute("file") List<MultipartFile> file, @ModelAttribute("container") String container) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			log.info(container+"----------");
			return inspectionsOutService.saveInspectionOut(file,container);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE); 
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	
	
}
