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
@Table(name = "MYRE_CAT_COMPONENT", schema = "MYRE")
public class CatComponentModel implements IModel{

		
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "COMPONENT_ID")private String  componentId;
	@Column(name = "SECCTION")private String  secction;
	@Column(name = "COMPONENT")private String  component;
	@Column(name = "DC")private String  dc;
	@Column(name = "RF")private String  rf;
	@Column(name = "GD")private String  gd;
	@Column(name = "CH")private String  ch;
}
