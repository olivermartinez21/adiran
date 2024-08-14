package com.tmm.myre.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatRepairModel;
@Repository("ICatRepairRepository")
public interface ICatRepairRepository extends JpaRepository<CatRepairModel, String> {

}
