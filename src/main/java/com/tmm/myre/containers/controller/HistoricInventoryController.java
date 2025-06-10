package com.tmm.myre.containers.controller;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerHistoricDto;
import com.tmm.myre.containers.dto.HistoricInventoryDto;
import com.tmm.myre.containers.service.HistoricInventoryService;
import com.tmm.myre.containers.service.core.IHistoricInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.tmm.myre.base.controller.AbstractMyreController.PREFIX_CONTAINER;

@Slf4j
@Controller
@RequestMapping(HistoricInventoryController.HOME)
public class HistoricInventoryController extends AbstractMyreController {

    @Autowired
    private IHistoricInventoryService historicInventoryService;

    public static final String HOME = PREFIX_CONTAINER + "historicInventory";

    @GetMapping(EMPTY)
    public String onLoadHome() {
        return HOME;
    }

    @GetMapping("/getDataTable")
    @ResponseBody
    public List<HistoricInventoryDto> getDataTable() throws ConverterException {
        try {
            return historicInventoryService.getDataTable();
        } catch(Exception ex) {
            return null;
        }

    }
}
