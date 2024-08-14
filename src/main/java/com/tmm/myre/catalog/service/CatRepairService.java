package com.tmm.myre.catalog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.catalog.converter.CatRepairConverter;
import com.tmm.myre.catalog.dto.CatRepairDto;
import com.tmm.myre.catalog.model.CatRepairModel;
import com.tmm.myre.catalog.repository.ICatRepairRepository;
import com.tmm.myre.catalog.service.core.ICatRepairService;
@Service("catRepairService")
public class CatRepairService implements ICatRepairService{

	
	@Autowired
	private ICatRepairRepository catRepairRepository;
	
	@Autowired
	private CatRepairConverter catRepairConverter;
	
	@Override
	public List<CatRepairDto> getRepairInformation() throws ConverterException {
		List<CatRepairDto> list = new ArrayList<CatRepairDto>();
		List<CatRepairModel> entities = catRepairRepository.findAll();
		for(CatRepairModel entity : entities) {
			list.add(catRepairConverter.convert(entity));
		}
		return list;
	}


}
