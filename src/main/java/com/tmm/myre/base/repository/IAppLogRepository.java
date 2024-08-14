/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.base.dto.AppLogDto;
import com.tmm.myre.base.dto.appLogDatosDto;
import com.tmm.myre.base.model.AppLog;
import com.tmm.myre.base.model.appLogDatosModel;



/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 date creation 3 sep. 2020
 */
@Repository("appLogRepository")
public interface IAppLogRepository extends JpaRepository<AppLog, String>{

	@Query(value = "SELECT MANAGEMENT_DATE, COUNT(*) as Y FROM APP_LOG GROUP BY MANAGEMENT_DATE", nativeQuery = true)
	List<String> cosulta();

}
