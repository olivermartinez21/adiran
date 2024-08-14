package com.tmm.myre.base.configuration.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository("eventActivityRepository")
public interface EventActivityRepository extends JpaRepository<EventActivityModel, Integer> {
	@Query(value = "SELECT TOP 1 * FROM GateActivityOutbound_EDI ORDER BY gateEventIdentifier DESC", nativeQuery = true)
	int getLast();

}
