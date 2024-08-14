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
@Table(name = "MYRE_CAT_NOMENCLATURAS", schema = "MYRE")
public class CatNomenclaturaModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "NOMENCLATURA_ID")private String  nomenclaturaId;
	@Column(name = "CONTAINER_TYPE")private String  containerType;
	@Column(name = "SIZE")private String  size;
	@Column(name = "NOMENCLATURA")private String  nomenclatura;
	@Column(name = "TRANSMAN")private String  transman;
}

