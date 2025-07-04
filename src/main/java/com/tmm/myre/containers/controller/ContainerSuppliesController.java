package com.tmm.myre.containers.controller;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.service.core.IContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
