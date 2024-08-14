package com.tmm.myre.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmm.myre.catalog.model.CatTransportCompanyModel;
@Repository("ICatTransportCompanyRepository")
public interface ICatTransportCompanyRepository extends JpaRepository<CatTransportCompanyModel, String>{

}
