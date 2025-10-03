package com.tmm.myre.containers.service.core;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.HistoricInventoryDto;

import java.time.LocalDate;
import java.util.List;

public interface IHistoricInventoryService {

    List<HistoricInventoryDto> getDataTable() throws ConverterException;

    List<HistoricInventoryDto> getDataTable(LocalDate startDate, LocalDate endDate) throws ConverterException;
}
