/**
 * 
 */
package com.tmm.myre.base.service.core;

import java.util.List;

import com.tmm.myre.base.dto.AppUserDto;
import com.tmm.myre.base.exception.ConverterException;



/**
 * @author joshm
 *
 */
public interface IAppUserService {

	List<AppUserDto> findAll() throws ConverterException;
}
