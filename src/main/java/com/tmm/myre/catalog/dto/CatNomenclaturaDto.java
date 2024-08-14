package com.tmm.myre.catalog.dto;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CatNomenclaturaDto extends AbstractManagement implements ITransferObject {
	
	private static final long serialVersionUID = 1L;
	private String  nomenclaturaId;
	private String  containerType;
	private String  size;
	private String  nomenclatura;
	private String  transman;
}
