package com.tmm.myre.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmm.myre.base.model.MyreSessionLog;

@Repository("sessionLogRepository")
public interface ISessionLogRepository extends JpaRepository<MyreSessionLog, String>{

	
}
