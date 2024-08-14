/**
 * 
 */
package com.tmm.myre.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.converter.AppRoleConverter;
import com.tmm.myre.base.dto.AppRoleDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.AppRole;
import com.tmm.myre.base.repository.IAppRoleRepository;
import com.tmm.myre.base.service.core.IAppRoleService;



/**
 * @author joshm
 *
 */
@Service("appRoleService")
public class AppRoleService implements IAppRoleService {

	@Autowired
	private IAppRoleRepository appRoleRepository;
	
	@Autowired
	private AppRoleConverter appRoleConverter;
	
	@Override
	public List<AppRoleDto> findAll() throws ConverterException {
		List<AppRoleDto> list = new ArrayList<AppRoleDto>();
		List<AppRole> entities = appRoleRepository.findAll();
		for(AppRole entity : entities) {
			list.add(appRoleConverter.convert(entity));
		}
		return list;
	}


	

}
