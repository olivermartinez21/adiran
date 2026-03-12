package com.tmm.myre.containers.repository;

import com.tmm.myre.containers.model.ContainerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.containers.model.ContainerHistoricModel;

import java.time.LocalDateTime;
import java.util.List;


@Repository("containerHistoricRepository")
public interface IContainerHistoricRepository extends JpaRepository<ContainerHistoricModel, String>, JpaSpecificationExecutor<ContainerHistoricModel> {
	
	@Query(value = "SELECT * FROM MYRE_CONTAINERS_HISTORIC WHERE CONTAINER = :container AND REGISTER_DATE = (SELECT MAX(REGISTER_DATE) FROM MYRE_CONTAINERS_HISTORIC WHERE CONTAINER = :container)", nativeQuery = true)
	ContainerHistoricModel getlastRegister(String container);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS_HISTORIC c WHERE c.QUOTE IS NOT NULL", nativeQuery = true)
	List<ContainerHistoricModel> findWhereQuoteNotEmpty();

	ContainerHistoricModel findByContainerId(String containerId);

	@Query(value = "SELECT * FROM MYRE_CONTAINERS_HISTORIC WHERE CONTAINER_CONDITION_PREGATE = 'VACIO' AND EXPEDITION_DATE BETWEEN :start AND :end", nativeQuery = true)
	List<ContainerHistoricModel> findByExpeditionDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
