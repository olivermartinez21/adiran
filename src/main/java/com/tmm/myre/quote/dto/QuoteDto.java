package com.tmm.myre.quote.dto;

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
public class QuoteDto extends AbstractManagement implements ITransferObject{

public static final long serialVersionUID = 1L;
	
	private String quoteId;
	private String workCode;
	private String repairDescription;
	private String hours;
	private String labor;
	private String material;
	private String tarifa;
	private String exchange;
	private String inspectionId;
}
