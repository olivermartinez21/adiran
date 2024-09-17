package com.tmm.myre.quote.model;


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
@Table(name = "MYRE_QUOTES", schema = "MYRE")
public class QuoteModel implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "QUOTE_ID")private String quoteId;
	@Column(name = "WORK_CODE")private String workCode;
	@Column(name = "REPAIR_DESCRIPTION") private String repairDescription;
	@Column(name = "HOURS")private String hours;
	@Column(name = "LABOR")private String labor;
	@Column(name = "MATERIAL")private String material;
	@Column(name = "TARIFA")private String tarifa;
	@Column(name = "EXCHANGE")private String exchange;
	@Column(name = "INSPECTION_ID")private String inspectionId;
}
