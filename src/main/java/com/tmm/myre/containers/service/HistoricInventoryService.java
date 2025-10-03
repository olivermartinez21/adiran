package com.tmm.myre.containers.service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.ContainerInventoryHistoric;
import com.tmm.myre.base.repository.IContainerInventoryHistoricRepository;
import com.tmm.myre.containers.converter.HistoricInventoryConverter;
import com.tmm.myre.containers.dto.HistoricInventoryDto;
import com.tmm.myre.containers.service.core.IHistoricInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("historicInventoryService")
public class HistoricInventoryService implements IHistoricInventoryService {

    @Autowired
    private IContainerInventoryHistoricRepository containerInventoryHistoricRepository;

    @Autowired
    private HistoricInventoryConverter historicInventoryConverter;

    @Override
    public List<HistoricInventoryDto> getDataTable() throws ConverterException {
        return buildDtos(containerInventoryHistoricRepository.findAll());
    }

    @Override
    public List<HistoricInventoryDto> getDataTable(LocalDate startDate, LocalDate endDate) throws ConverterException {
        if (startDate == null && endDate == null) {
            return getDataTable();
        }
        if (startDate != null && endDate == null) {
            endDate = startDate; // mismo día
        } else if (endDate != null && startDate == null) {
            startDate = endDate; // mismo día
        }
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            log.warn("Rango de fechas inválido: endDate {} es anterior a startDate {}", endDate, startDate);
            return new ArrayList<>();
        }
        List<ContainerInventoryHistoric> entities;
        if (startDate != null && endDate != null && startDate.isEqual(endDate)) {
            entities = containerInventoryHistoricRepository.findAllByUploadDate(Date.valueOf(startDate));
        } else {
            entities = containerInventoryHistoricRepository.findAllByUploadDateBetween(Date.valueOf(startDate), Date.valueOf(endDate));
        }
        return buildDtos(entities);
    }

    private List<HistoricInventoryDto> buildDtos(List<ContainerInventoryHistoric> entities) throws ConverterException {
        List<HistoricInventoryDto> list = new ArrayList<>();
        for (ContainerInventoryHistoric entity : entities) {
            list.add(historicInventoryConverter.convert(entity));
        }
        return list;
    }
}
