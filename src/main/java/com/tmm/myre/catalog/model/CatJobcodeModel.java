package com.tmm.myre.catalog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tmm.myre.base.model.IModel;

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
@Table(name = "MYRE_CAT_JOBCODES", schema = "MYRE")
public class CatJobcodeModel implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "JOBCODE_ID")private String  jobcodeId;
	@Column(name = "JOBCODE_REPAIR")private String  jobcodeRepair;
	@Column(name = "JOBCODE_DESCRIPTION")private String  jobcodeDescription;
	@Column(name = "JOBCODE_MATERIAL")private String  jobcodeMaterial;
	@Column(name = "JOBCODE_HH")private String  jobcodeHh;
	@Column(name = "JOBCODE_EXCHANGE")private String  jobcodeExchange;
	
}
