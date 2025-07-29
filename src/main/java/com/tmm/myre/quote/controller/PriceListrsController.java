package com.tmm.myre.quote.controller;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.service.core.ICatJobcodeService;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.quote.service.core.IPriceListService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PriceListrsController.HOME)
public class PriceListrsController extends AbstractMyreController {

	public static final String HOME = PREFIX_QUOTES + "priceLists";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
	
	@Autowired
	private IPriceListService priceListService;

	@Autowired
	private ICatJobcodeService catJobcodeService;

	@Autowired
	private ICatShippingCompanyService catShippingCompanyService;


	@ModelAttribute("catShipping")
	List<CatShippingCompanyDto> catShipping() {
		try {
			return catShippingCompanyService.catShipping();
		} catch(Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}

	@GetMapping("getDataTable")
	@ResponseBody
	List<CatJobcodeDto> catJobcode(@RequestParam(required = false) String shippingCompanyId) {
		try {
			if (shippingCompanyId != null && !shippingCompanyId.equals("0")) {
				return catJobcodeService.catJobcode(shippingCompanyId);
			} else{
				return catJobcodeService.findAll();
			}

		} catch(Exception ex) {
			log.error(ex.toString());
			return null;

		}
	}

	@GetMapping("getLaborByShippingCompany")
	@ResponseBody
	public CatShippingCompanyModel getLaborByShippingCompany(@RequestParam Integer shippingCompanyId) {

		return  catShippingCompanyService.getLaborByShippingCompany(shippingCompanyId);
	}

	@GetMapping("getExchangeByShippingCompany")
	@ResponseBody
	public String getExchangeByShippingCompany(@RequestParam String shippingCompanyId) {
		try {
			return priceListService.getExchangeByShippingCompanyId(shippingCompanyId);
		} catch(Exception ex) {
			log.error(ex.toString());
			return "";
		}
	}

	@PostMapping("updateJobcode")
	@ResponseBody
	public ResponseManagement updateJobcode(@RequestBody Map<String, Object> payload) {
		ResponseManagement response = new ResponseManagement();
		try {
			priceListService.updateJobcode(payload);
			response.setOperation(KeyConstants.UPDATE);
			response.setMessage("Actualización exitosa");
		} catch (Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.UPDATE);
		}
		return response;
	}

//	@PostMapping("updateLaborByShippingCompany")
//	@ResponseBody
//	public ResponseManagement updateLaborByShippingCompany(CatShippingCompanyDto catShippingCompanyDto) throws ConverterException {
//		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
//	    // Lógica para actualizar el valor de labor en la base de datos
//	    priceListService.updateLaborByShippingCompanyId(catShippingCompanyDto);
//	    return response;
//	}

	@PostMapping("updateLaborAndExchangeByShippingCompany")
	@ResponseBody
	public ResponseManagement updateLaborAndExchangeByShippingCompany(@RequestParam String shippingCompanyId, @RequestParam String maneuverCost, @RequestParam String labor, @RequestParam String exchange) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).success(false).build();
		try {
			priceListService.updateLaborAndExchangeByShippingCompanyId(shippingCompanyId, maneuverCost, labor, exchange);
			response.setSuccess(true);
			response.setMessage("Actualización exitosa");
		} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
		}
		return response;
	}

	@PostMapping("upload")
	@ResponseBody
	public ResponseManagement uploadFile(@RequestParam("file") MultipartFile file) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();	
		try {
			return priceListService.createLisprice(file);
	} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.INSERT);
		}
		
		return response;
	}
	

}
