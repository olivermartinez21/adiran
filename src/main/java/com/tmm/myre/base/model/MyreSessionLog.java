package com.tmm.myre.base.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MYRE_SESSION_LOG", schema = "MYRE")
public class MyreSessionLog implements IModel {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SESSION_ID") private String sessionId;
	
	@Column(name = "USER_ID") private Integer userId; 
	@Column(name = "SESSION_DATETIME") private Timestamp sessionDatetime;
}
