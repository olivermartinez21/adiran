package com.tmm.myre.containers.dto;


import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResumentInformationDto  extends AbstractManagement implements ITransferObject {

	private static final long serialVersionUID = 1L;
	private String  tipo;
	private String  nomenclatura;
	private Integer  disponibles;
	private Integer  dañados;
	private Integer  ppti;
	private Integer  total;
	private Integer  suma;
}
