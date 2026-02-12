package com.tmm.myre.containers.controller;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import com.tmm.myre.catalog.dto.CatTransportCompanyDto;
import com.tmm.myre.catalog.service.core.ICatShippingCompanyService;
import com.tmm.myre.catalog.service.core.ICatTransportCompanyService;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.service.core.IContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(ContainerSuppliesController.HOME)
public class ContainerSuppliesController extends AbstractMyreController {


    public static final String HOME = PREFIX_CONTAINER + "containerSupplies";

    @GetMapping(EMPTY)
    public String onLoadHome() {
        return HOME;
    }

    @Autowired
    private IContainerService containerService;

    @Autowired
    private ICatShippingCompanyService catShippingCompanyService;

    @Autowired
    private ICatTransportCompanyService catTransportCompanyService;

    @ModelAttribute("catShipping")
    List<CatShippingCompanyDto> catShipping() {
        try {
            return catShippingCompanyService.catShipping();
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
    public List<ContainerDto> getDataTable(@RequestParam(required = true) String appointmentId , Integer userId) {
        try {
            return containerService.getContainersFull(getWarehouse());
        } catch(Exception ex) {
            return null;
        }
    }
}
