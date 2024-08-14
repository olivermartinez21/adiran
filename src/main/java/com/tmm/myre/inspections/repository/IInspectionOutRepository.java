package com.tmm.myre.inspections.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmm.myre.inspections.model.InspecctionOutModel;
@Repository("inspectorOutRepository")
public interface IInspectionOutRepository extends JpaRepository<InspecctionOutModel, String>{

	


}
