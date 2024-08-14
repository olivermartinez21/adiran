package com.tmm.myre.catalog.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatJobcodeConverter;
import com.tmm.myre.catalog.dto.CatJobcodeDto;
import com.tmm.myre.catalog.model.CatJobcodeModel;
import com.tmm.myre.catalog.repository.ICatJobcodeRepository;
import com.tmm.myre.catalog.service.core.ICatJobcodeService;
import com.tmm.myre.containers.dto.ContainerHistoricDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("catJobcodeService")
public class CatJobcodeService implements  ICatJobcodeService{

	@Autowired
	private ICatJobcodeRepository catJobcodeRepository;
	
	@Autowired
	private CatJobcodeConverter catJobcodeConverter;
	
	
	@Override
	public List<CatJobcodeDto> catJobcode() throws ConverterException {
		List<CatJobcodeDto> list = new ArrayList<CatJobcodeDto>();
		List<CatJobcodeModel> entities = catJobcodeRepository.findAll();
		for(CatJobcodeModel entity : entities) {
			list.add(catJobcodeConverter.convert(entity));
		}
		return list;
}


	@Override
	public CatJobcodeDto getJobcodeDescription(String jobcodeId) throws ConverterException {
		CatJobcodeModel info = catJobcodeRepository.jobcodeDescription(jobcodeId);
		return catJobcodeConverter.convert(info);
		
	}
}
