/**
 * 
 */
package com.tmm.myre.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.converter.AppUserConverter;
import com.tmm.myre.base.dto.AppUserDto;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.AppUser;
import com.tmm.myre.base.repository.IAppUserRepository;
import com.tmm.myre.base.service.core.IAppUserService;


/**
 * @author joshm
 *
 */
@Service("appUserService")
public class AppUserService implements IAppUserService{

	@Autowired
	private IAppUserRepository appUserRepository;
	
	@Autowired
	private AppUserConverter appUserConverter;
	
	@Override
	public List<AppUserDto> findAll() throws ConverterException {
		List<AppUserDto> list = new ArrayList<AppUserDto>();
		List<AppUser> entities = appUserRepository.findAll();
		for(AppUser entity : entities) {
			list.add(appUserConverter.convert(entity));
		}
		return list;
	}

}
