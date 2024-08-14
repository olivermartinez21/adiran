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
public class CatComponentDto extends AbstractManagement implements ITransferObject {
	
	private static final long serialVersionUID = 1L;
	private String  componentId;
	private String  secction;
	private String  component;
	private String  dc;
	private String  rf;
	private String  gd;
	private String  ch;

}
