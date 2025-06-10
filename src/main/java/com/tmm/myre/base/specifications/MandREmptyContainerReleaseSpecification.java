package com.tmm.myre.base.specifications;

import com.tmm.myre.base.configuration.secondary.MandREmptyContainerReleaseModel;
import com.tmm.myre.containers.dto.ReportFilterDto;
import com.tmm.myre.containers.model.ContainerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MandREmptyContainerReleaseSpecification {

    public static Specification<MandREmptyContainerReleaseModel> byFilter(ReportFilterDto reportFilterDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrado por rango de fechas para RegisterDate (Date)
            if (reportFilterDto.getDateInit() != null && reportFilterDto.getDateEnd() != null) {
                LocalDateTime startDateTime = setStartOfDay(convertToLocalDateTime(reportFilterDto.getDateInit()));
                LocalDateTime endDateTime = setEndOfDay(convertToLocalDateTime(reportFilterDto.getDateEnd()));

                log.info("startDateTime: " + startDateTime);
                log.info("endDateTime: " + endDateTime);

                predicates.add(cb.between(root.get("fechaEDI"), startDateTime, endDateTime));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Método para convertir Date a LocalDateTime
    private static LocalDateTime convertToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()) // Convierte Date a Instant
                .atZone(ZoneId.systemDefault())     // Usa la zona horaria del sistema
                .toLocalDateTime();                 // Convierte a LocalDateTime
    }

    // Método para establecer la hora en 00:00:00
    private static LocalDateTime setStartOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    // Método para establecer la hora en 23:59:59
    private static LocalDateTime setEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }
}
