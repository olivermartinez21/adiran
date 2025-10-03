package com.tmm.myre.base.repository;

import com.tmm.myre.base.model.ContainerInventoryHistoric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository("containerInventoryHistoricRepository")
public interface IContainerInventoryHistoricRepository extends JpaRepository<ContainerInventoryHistoric, Integer> {
    List<ContainerInventoryHistoric> findAllByUploadDateBetween(Date startDate, Date endDate);
    List<ContainerInventoryHistoric> findAllByUploadDate(Date date);
    // Nuevo método para eliminación de históricos anteriores a una fecha de corte
    @Transactional
    long deleteByUploadDateBefore(Date cutoffDate);
}
