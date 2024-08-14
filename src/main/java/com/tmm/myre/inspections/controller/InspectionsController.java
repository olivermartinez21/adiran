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
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.inspections.service.core.IInspectionsService;
import com.tmm.myre.photo.dto.PhotosDto;
import com.tmm.myre.photo.service.core.IPhotorService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(InspectionsController.HOME)
public class InspectionsController extends AbstractMyreController {

	public static final String HOME = PREFIX_INSPECTION + "inspection";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private IInspectionsService inspectionsService;
	
	@Autowired
	private ICatComponentService catComponentService;
	
	@Autowired
	private ICatDamageService catDamageService;
	
	@Autowired
	private ICatRepairService catRepairService;
	
	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;
	
	@Autowired
	private IPhotorService photorService;
	
	@Autowired
	private IInspectionRepository inspectionRepository;
	
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<ContainerDto> getDataTable() {
		try {
			return containerService.getAllContainersQuote(getWarehouse());
			//return containerService.getContainersQuote(getWarehouse(),2);
		} catch(Exception ex) {
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
	@ModelAttribute("getComponets")
	List<CatComponentDto> getComponets() {
		try {
			return catComponentService.findAll();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	@ModelAttribute("getDamage")
	List<CatDamageDto> getDamage() {
		try {
			return catDamageService.findAll();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	@ModelAttribute("getRepair")
	List<CatRepairDto> getRepair() {
		try {
			return catRepairService.getRepairInformation();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@PostMapping("inspectionValidation")
	@ResponseBody
	public ResponseManagement inspectionValidation(@RequestParam(required = true) String inspectionId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		
		try {
			return inspectionsService.inspectionValidation(inspectionId);
		} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.UPDATE);
		}
		return response;
	
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
	
	
	@GetMapping("dataTableInpection")
	@ResponseBody
	public List<InspectionDto> dataTableInpection(@RequestParam(required = true) String containerId) {
		try {
			return inspectionsService.getInspectionsByContainer(containerId);
		} catch(Exception ex) {
			return null;
		}
	}
	
	@PostMapping("saveInformationRepair")
	@ResponseBody
	public ResponseManagement saveInformationRepair(@ModelAttribute("containerDto") ContainerDto containerDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		
		log.info(containerDto.getAssignedTo()+"--"+containerDto.getStartDate()+"--"+containerDto.getContainerId());
		try {
			return containerService.saveInformationRepair(containerDto);
		} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.UPDATE);
		}
		return response;
	
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

	@PostMapping("saveNewInspection")
	@ResponseBody
	public ResponseManagement saveNewInspection(@ModelAttribute("inspectionDto") InspectionDto inspectionDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		
		try {
			return inspectionsService.saveNewInspection(inspectionDto);
		} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.UPDATE);
		}
		return response;
	
}
	
	@GetMapping("getPhotos")
	@ResponseBody
	public List<PhotosDto> getPhotos(@RequestParam("containerId") String containerId) {
		log.info(containerId+"--------------");
		try {
			return photorService.getPhotos(containerId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	

	@RequestMapping(value = "/IMAGE", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<InputStreamResource> certificateView(@RequestParam(required = true) String photoId) {
		try {
			PhotosDto imagen = photorService.getOne(photoId);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",  "inline; filename="+imagen.getPhotoId()+".JPEG");
			ByteArrayInputStream body = new ByteArrayInputStream(imagen.getPhoto());
			return ResponseEntity
					.ok()
					.headers(headers)
					.contentType(MediaType.IMAGE_JPEG)
					.body(new InputStreamResource(body));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			return null;
		}
	}
	@PostMapping("addDamage")
	@ResponseBody
	public ResponseManagement addDamage(@ModelAttribute("file") List<MultipartFile> file, @ModelAttribute("inspection") String inspection) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			return inspectionsService.addDamage(file,inspection);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	@PostMapping("repairDamage")
	@ResponseBody
	public ResponseManagement repairDamage(@RequestParam(required = true)String inspectionId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			return inspectionsService.repairDamage(inspectionId);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	@GetMapping("validationInspection")
	@ResponseBody
	public int validationInspection(@RequestParam(required = true) String containerId) {
		try {
			return inspectionsService.validationInspection(containerId,4);
		} catch(Exception ex) {
			return 10000;
		}
	}
	
	@PostMapping("changeStatus")
	@ResponseBody
	public ResponseManagement changeStatus(@ModelAttribute("contianerDto") ContainerDto contianerDto ) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			return inspectionsService.changeStatus(contianerDto);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
}
