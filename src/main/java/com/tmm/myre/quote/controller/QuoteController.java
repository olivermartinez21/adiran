package com.tmm.myre.quote.controller;

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

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.InspectionShippingDto;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.catalog.dto.CatComponentDto;
import com.tmm.myre.catalog.dto.CatDamageDto;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.catalog.dto.CatRepairDto;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.service.CatJobcodeService;
import com.tmm.myre.catalog.service.core.ICatComponentService;
import com.tmm.myre.catalog.service.core.ICatDamageService;
import com.tmm.myre.catalog.service.core.ICatJobcodeService;
import com.tmm.myre.catalog.service.core.ICatRepairService;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ContainerHistoricDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.containers.service.core.IContainerService;
import com.tmm.myre.inspections.dto.InspectionDto;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.inspections.service.core.IInspectionsService;
import com.tmm.myre.photo.dto.PhotosDto;
import com.tmm.myre.photo.service.core.IPhotorService;
import com.tmm.myre.quote.dto.QuoteDto;
import com.tmm.myre.quote.service.core.IQuoteService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(QuoteController.HOME)
public class QuoteController extends AbstractMyreController {

	public static final String HOME = PREFIX_QUOTES + "quote";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;
	
	@Autowired
	private IInspectionsService inspectionsService;
	
	@Autowired
	private ICatComponentService catComponentService;
	
	@Autowired
	private ICatDamageService catDamageService;
	
	@Autowired
	private ICatRepairService catRepairService;
	
	@Autowired
	private ICatJobcodeService catJobcodeService;
	
	@Autowired
	private IQuoteService quoteService;
	
	@Autowired
	private IInspectionRepository inspectionRepository;
	
	@Autowired
	private IPdfGenerationService pdfGenerationService;
	
	@Autowired
	private IPhotorService photorService;
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<ContainerDto> getDataTable() {
		try {
			return containerService.getContainersQuoteAll(getWarehouse());
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
	
	@GetMapping("dataTableInpection")
	@ResponseBody
	public List<InspectionDto> dataTableInpection(@RequestParam(required = true) String containerId) {
		try {
			return inspectionsService.getInspectionsByContainer(containerId);
		} catch(Exception ex) {
			return null;
		}
	}
	
	
	

	@ModelAttribute("catComponent")
	List<CatComponentDto> catComponent() {
		try {
			return catComponentService.findAll();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}

	@ModelAttribute("catDamage")
	List<CatDamageDto> catDamage() {
		try {
			return catDamageService.findAll();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;

		}
	}

	@ModelAttribute("catRepair")
	List<CatRepairDto> catRepair() {
		try {
			return catRepairService.getRepairInformation();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@ModelAttribute("catJobcode")
	List<CatJobcodeDto> catJobcode() {
		try {
			log.info("Entrando a controller Jobcodeeeeeeeeeeeeeeeeeeeeeeeee");
			return catJobcodeService.catJobcode();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;

		}
	}
	
	
	@PostMapping("saveInformationQuote")
	@ResponseBody
	public ResponseManagement saveInformationQuote(@ModelAttribute("quoteDto") QuoteDto quoteDto) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
				return quoteService.saveInformationQuote(quoteDto);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	
	@RequestMapping(value = "/PDF_QUOTE", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> certificateView(@RequestParam(required = true) String containerId) {
		try {
			ContainerDto pdf = containerService.getOne(containerId);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",  "inline; filename="+pdf.getQuoteName()+getFileExtension(pdf.getQuoteName()));
			ByteArrayInputStream body = new ByteArrayInputStream(pdf.getQuote());
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
	
	
	@GetMapping("validationInspection")
	@ResponseBody
	public int validationInspection(@RequestParam(required = true) String containerId) {
		try {
			return inspectionRepository.countInspectionsValidation(containerId,2);
		} catch(Exception ex) {
			return 10000;
		}
	}
	
	@PostMapping("changeStatus")
	@ResponseBody
	public ResponseManagement changeStatus(@RequestParam(required = true) String containerId) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			
				return containerService.changeStatus(containerId,getWarehouse());
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	@PostMapping("editStatus")
	@ResponseBody
	public ResponseManagement editStatus(@RequestParam(required = true) String containerId, Integer status) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			log.info(containerId+"-------------------"+status);
				return containerService.changeStatusApproved(containerId,"",status);
		} catch(Exception ex) {
				response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
				response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
				response.setOperation(KeyConstants.UPDATE);
			}
			return response;
		}
	
	
	

	@GetMapping("getPhotos")
	@ResponseBody
	public List<PhotosDto> getPhotos(@RequestParam("containerId") String containerId) {
		try {
			return photorService.getPhotos(containerId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	
	
	@RequestMapping(value = "/IMAGE", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<InputStreamResource> IMAGE(@RequestParam(required = true) String photoId) {
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
	
	@ModelAttribute("getRepairInformation")
	List<CatRepairDto> getRepairInformation() {
		try {
			return catRepairService.getRepairInformation();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getJobcodeDescription")
	@ResponseBody
	public CatJobcodeDto getJobcodeDescription(@RequestParam("jobcodeId") String jobcodeId) {
		try {
			return catJobcodeService.getJobcodeDescription(jobcodeId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}
	
	@GetMapping("validationStatus")
	@ResponseBody
	public int validationStatus(@RequestParam(required = true) String containerId) {
		try {
			return inspectionRepository.getCountInspectionsRepair(containerId);
		} catch(Exception ex) {
			return 10000;
		}
	}
	
	
	  @GetMapping("getPreLabor")
	  @ResponseBody 
	  public InspectionShippingDto getPreLabor(@RequestParam(required = true) String inspectionId) { 
		  try { 
			  	log.info(inspectionId+"----------------");
		  		return containerService.getPreLabor(inspectionId); 
		  	} catch(Exception ex) {
		  		return null; 
	  } }
	 
		
	
}
