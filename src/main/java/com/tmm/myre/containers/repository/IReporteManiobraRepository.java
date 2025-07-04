package com.tmm.myre.containers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tmm.myre.containers.model.ReporteManiobraModel;

@Repository("reporteManiobraRepository")
public interface IReporteManiobraRepository extends JpaRepository<ReporteManiobraModel, Long>, JpaSpecificationExecutor<ReporteManiobraModel> {

    // Ejemplo de método de consulta personalizado:
    // List<ReporteManiobraModel> findByLocalidad(String localidad);

    // Ejemplo de query con @Query y parámetros:
    // @Query("SELECT r FROM ReporteManiobraModel r WHERE r.fechaEvento BETWEEN :inicio AND :fin")
    // List<ReporteManiobraModel> findByFechaEventoBetween(
    //         @Param("inicio") LocalDateTime inicio,
    //         @Param("fin") LocalDateTime fin);
}