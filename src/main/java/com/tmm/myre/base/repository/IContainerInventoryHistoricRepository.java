package com.tmm.myre.base.repository;

import com.tmm.myre.base.model.ContainerInventoryHistoric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("containerInventoryHistoricRepository")
public interface IContainerInventoryHistoricRepository extends JpaRepository<ContainerInventoryHistoric, Integer> {
}
