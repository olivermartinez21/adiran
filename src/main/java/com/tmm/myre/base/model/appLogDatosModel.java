package com.tmm.myre.base.model;

import java.sql.Date;

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
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "APP_LOG", schema = "MYRE")
public class appLogDatosModel implements IModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MANAGEMENT_DATE") private Date managementDate;
	@Column(name = "Y") private String operation;
}
