package com.tmm.myre.assignments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.assignments.model.AssignmentsFullModel;
import com.tmm.myre.assignments.model.AssignmentsModel;
@Repository("assignmentsFullRepository")
public interface IAssignmentsFullRepository  extends JpaRepository<AssignmentsFullModel, String>{

	
	
	@Query(value = "SELECT * FROM MYRE_ASSIGNMENTS_FULL", nativeQuery = true)
	List<AssignmentsFullModel> getByLocation();

}
