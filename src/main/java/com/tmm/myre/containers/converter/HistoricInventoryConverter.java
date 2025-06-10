package com.tmm.myre.containers.converter;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.ContainerInventoryHistoric;
import com.tmm.myre.containers.dto.HistoricInventoryDto;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;

@Component("historicInventoryConverter")
public class HistoricInventoryConverter implements IConverter<ContainerInventoryHistoric, HistoricInventoryDto> {


    @Override
    public ContainerInventoryHistoric convert(HistoricInventoryDto to) throws ConverterException {
        ContainerInventoryHistoric entity = ContainerInventoryHistoric.builder()
                .inventoryHistoricId(to.getInventoryHistoricId())
                .shippingCompanyDescription(to.getShippingCompanyDescription())
                .container(to.getContainer())
                .condition(to.getCondition())
                .containerType(to.getContainerType())
                .nomenclatura(to.getNomenclatura())
                .conditionPregate(to.getConditionPregate())
                .location(to.getLocation())
                .dateInspection(to.getDateInspection())
                .clasification(to.getClasification())
                .daysOfStay(to.getDaysOfStay())
                .finalDate(to.getFinalDate())
                .statusQuote(to.getStatusQuote())
                .aptTo(to.getAptTo())
                .typeServicePregate(to.getTypeServicePregate())
                .registerDate(to.getRegisterDate())
                .comments(to.getComments())
                .uploadDate(to.getUploadDate())
                .build();
        return entity;
    }

    @Override
    public HistoricInventoryDto convert(ContainerInventoryHistoric entity) throws ConverterException {
        HistoricInventoryDto to = HistoricInventoryDto.builder()
                .inventoryHistoricId(entity.getInventoryHistoricId())
                .shippingCompanyDescription(entity.getShippingCompanyDescription())
                .container(entity.getContainer())
                .condition(entity.getCondition())
                .containerType(entity.getContainerType())
                .nomenclatura(entity.getNomenclatura())
                .conditionPregate(entity.getConditionPregate())
                .location(entity.getLocation())
                .dateInspection(entity.getDateInspection())
                .clasification(entity.getClasification())
                .daysOfStay(entity.getDaysOfStay())
                .finalDate(entity.getFinalDate())
                .statusQuote(entity.getStatusQuote())
                .aptTo(entity.getAptTo())
                .typeServicePregate(entity.getTypeServicePregate())
                .registerDate(entity.getRegisterDate())
                .comments(entity.getComments())
                .uploadDate(entity.getUploadDate())
                .build();
        return to;
    }
}
