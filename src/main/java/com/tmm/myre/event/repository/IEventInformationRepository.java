package com.tmm.myre.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tmm.myre.event.model.EventInformationModel;
import com.tmm.myre.userRegister.model.UserRegisterModel;
@Repository("eventInformationRepository")
public interface IEventInformationRepository extends JpaRepository<EventInformationModel, String> {
	
	@Query(value = "SELECT * FROM MYRE_EVENT WHERE CONTAINER_ID = :containerId", nativeQuery = true)
	List<EventInformationModel> findEvents(@Param("containerId") String containerId);
	

}
