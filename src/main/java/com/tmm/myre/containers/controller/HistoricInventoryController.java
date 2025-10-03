package com.tmm.myre.containers.controller;

import com.tmm.myre.base.controller.AbstractMyreController;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.HistoricInventoryDto;
import com.tmm.myre.containers.service.core.IHistoricInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
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
    public List<HistoricInventoryDto> getDataTable(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws ConverterException {
        try {
            if (startDate == null && endDate == null) {
                return historicInventoryService.getDataTable();
            }
            return historicInventoryService.getDataTable(startDate, endDate);
        } catch(Exception ex) {
            log.error("Error obteniendo inventario histórico", ex);
            return null;
        }
    }
}
