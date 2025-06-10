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
        List<HistoricInventoryDto> list = new ArrayList<HistoricInventoryDto>();
        List<ContainerInventoryHistoric> entities = containerInventoryHistoricRepository.findAll();
        for (ContainerInventoryHistoric entity : entities) {
            list.add(historicInventoryConverter.convert(entity));
        }
        return list;
    }

}
