package com.tmm.myre.assignments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.assignments.model.AssignmentsModel;

@Repository("assignmentsRepository")
public interface IAssignmentsRepository extends JpaRepository<AssignmentsModel, String> {

	
	@Query(value = "SELECT * FROM MYRE_ASSIGNMENTS WHERE LOCATION=:warehouse", nativeQuery = true)
	List<AssignmentsModel> getByLocation(String warehouse);

}
