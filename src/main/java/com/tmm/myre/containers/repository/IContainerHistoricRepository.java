package com.tmm.myre.containers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.containers.model.ContainerHistoricModel;


@Repository("containerHistoricRepository")
public interface IContainerHistoricRepository extends JpaRepository<ContainerHistoricModel, String>{
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS_HISTORIC WHERE CONTAINER = :container AND REGISTER_DATE = (SELECT MAX(REGISTER_DATE) FROM MYRE_CONTAINERS_HISTORIC WHERE CONTAINER = :container)", nativeQuery = true)
	ContainerHistoricModel getlastRegister(String container);
}
