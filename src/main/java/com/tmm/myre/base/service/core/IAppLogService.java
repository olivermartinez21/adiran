/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.service.core;

import java.util.List;

import com.tmm.myre.base.dto.AppLogDto;
import com.tmm.myre.base.exception.ConverterException;



/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */
public interface IAppLogService {

	/**
	 * @param idUser
	 * @param operation
	 * @param managmentDate
	 * @param log
	 */
	void registerLog(Integer idUser, String operation, String log);

	/**
	 * @return
	 */
	List<AppLogDto> findAll() throws ConverterException;

}
