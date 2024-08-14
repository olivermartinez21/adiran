package com.tmm.myre.base.service.core;

import java.util.List;

import com.tmm.myre.base.dto.AppRoleDto;
import com.tmm.myre.base.exception.ConverterException;



public interface IAppRoleService {

	List<AppRoleDto> findAll() throws ConverterException;

	
}
