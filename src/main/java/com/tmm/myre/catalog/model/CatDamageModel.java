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
@Table(name = "MYRE_CAT_DAMAGE", schema = "MYRE")
public class CatDamageModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "DAMAGE_ID")private String  damageId;
	@Column(name = "IICL_COD")private String  iiclCode;
	@Column(name = "DAMAGE_DESCRIPTION")private String  damageDescription;
	@Column(name = "DESCRIPTION")private String  description;
	@Column(name = "DC")private String  dc;
	@Column(name = "RF")private String  rf;
	@Column(name = "GD")private String  gd;
	@Column(name = "CH")private String  ch;
}
