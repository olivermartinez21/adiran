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
public class CatDamageDto extends AbstractManagement implements ITransferObject {
	
	private static final long serialVersionUID = 1L;
	private String  damageId;
	private String  iiclCode;
	private String  damageDescription;
	private String  description;
	private String  dc;
	private String  rf;
	private String  gd;
	private String  ch;

}
