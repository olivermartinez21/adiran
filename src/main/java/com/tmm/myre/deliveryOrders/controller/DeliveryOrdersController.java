package com.tmm.myre.deliveryOrders.controller;

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
import com.tmm.myre.catalog.dto.CatCustomerDto;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;
import com.tmm.myre.catalog.service.core.ICatCustomerService;
import com.tmm.myre.catalog.service.core.ICatTransportCompanyService;
import com.tmm.myre.deliveryOrders.dto.DeliveryOrderDto;
import com.tmm.myre.deliveryOrders.service.core.IDeliveryOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(DeliveryOrdersController.HOME)
public class DeliveryOrdersController extends AbstractMyreController{

public static final String HOME = PREFIX_DELIVERY_ORDERS + "deliveryOrders";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}

	@Autowired
	private IDeliveryOrderService deliveryOrderService;
	
	@Autowired
	private ICatCustomerService catCustomerService;

	@Autowired
	private ICatTransportCompanyService catTransportCompanyService;
	
	@GetMapping("getDataTable")
	@ResponseBody
	public List<DeliveryOrderDto> getDataTable() {
		try {
			return deliveryOrderService.getDataTable(getWarehouse());
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	@GetMapping("getInformation")
	@ResponseBody
	public DeliveryOrderDto getInformation(@RequestParam(required = true) String deliveryOrderId ) {
		try {
			return deliveryOrderService.getInformation(deliveryOrderId);
		} catch(Exception ex) {
			log.info(ex.toString());
			return null;
		}
	}
	
	
	@PostMapping("editDeliveryOrder")
	@ResponseBody
	public ResponseManagement editDeliveryOrder(@ModelAttribute("deliveryOrderDto") DeliveryOrderDto deliveryOrderDto) {
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
	
	
	
	
}
