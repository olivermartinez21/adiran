package com.tmm.myre.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.quote.service.core.IPriceListService;

import lombok.extern.slf4j.Slf4j;
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

	@PostMapping("upload")
	@ResponseBody
	public ResponseManagement uploadFile(@RequestParam("file") MultipartFile file, String type) {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).build();	
		try {
			return priceListService.createLisprice(file,type);
	} catch(Exception ex) {
			log.error(ex.toString());
			response.setErrorCode(KeyConstants.CONTROLLER_ERROR_CODE);
			response.setMessage(KeyConstants.CONTROLLER_ERROR + ex.toString());
			response.setOperation(KeyConstants.INSERT);
		}
		
		return response;
	}
	

}
