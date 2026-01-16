package com.tmm.myre.base.repository;


import com.tmm.myre.base.model.CountersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("counterRepository")
public interface ICounterRepository extends JpaRepository<CountersModel, String> {

    // incrementa de forma atómica y guarda el nuevo valor en LAST_INSERT_ID()
    @Modifying
    @Transactional
    @Query(value =
            "UPDATE MYRE_COUNTERS " +
                    "SET lastValue = LAST_INSERT_ID(lastValue + 1) " +
                    "WHERE counterName = :name",
            nativeQuery = true)
    int increment(@Param("name") String counterName);

    // lee el LAST_INSERT_ID de esta conexión
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertId();
}
