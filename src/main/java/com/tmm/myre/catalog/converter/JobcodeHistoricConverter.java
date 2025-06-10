package com.tmm.myre.catalog.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.dto.JobcodeHistoricDto;
import com.tmm.myre.catalog.model.JobcodeHistoricModel;

@Component("jobcodeHistoricConverter")
public class JobcodeHistoricConverter implements IConverter<JobcodeHistoricModel, JobcodeHistoricDto> {

    @Override
    public JobcodeHistoricModel convert(JobcodeHistoricDto to) throws ConverterException {
        JobcodeHistoricModel entity = JobcodeHistoricModel.builder()
                .jobcodeHistoryId(to.getJobcodeHistoryId())
                .jobcodeId(to.getJobcodeId())
                .jobcodeRepair(to.getJobcodeRepair())
                .jobcodeDescription(to.getJobcodeDescription())
                .jobcodeMaterial(to.getJobcodeMaterial())
                .jobcodeHh(to.getJobcodeHh())
                .jobcodeExchange(to.getJobcodeExchange())
                .jobcodeShippingId(to.getJobcodeShippingId())
                .build();
        return entity;
    }

    @Override
    public JobcodeHistoricDto convert(JobcodeHistoricModel entity) throws ConverterException {
        JobcodeHistoricDto to = JobcodeHistoricDto.builder()
                .jobcodeHistoryId(entity.getJobcodeHistoryId())
                .jobcodeId(entity.getJobcodeId())
                .jobcodeRepair(entity.getJobcodeRepair())
                .jobcodeDescription(entity.getJobcodeDescription())
                .jobcodeMaterial(entity.getJobcodeMaterial())
                .jobcodeHh(entity.getJobcodeHh())
                .jobcodeExchange(entity.getJobcodeExchange())
                .jobcodeShippingId(entity.getJobcodeShippingId())
                .build();
        return to;
    }
}
