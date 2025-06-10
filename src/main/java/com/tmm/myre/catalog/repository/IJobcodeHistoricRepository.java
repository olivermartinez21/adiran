package com.tmm.myre.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tmm.myre.catalog.model.JobcodeHistoricModel;

@Repository("IJobcodeHistoricRepository")
public interface IJobcodeHistoricRepository extends JpaRepository<JobcodeHistoricModel, Integer> {

}