package com.tmm.myre.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatFinalClientModel;
import com.tmm.myre.catalog.model.CatRepairModel;
@Repository("ICatFinalClientRepository")
public interface ICatFinalClientRepository extends JpaRepository<CatFinalClientModel, String> {

}
