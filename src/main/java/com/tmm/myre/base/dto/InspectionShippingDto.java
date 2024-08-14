package com.tmm.myre.base.dto;

import com.tmm.myre.catalog.dto.CatComponentDto;

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
public class InspectionShippingDto extends AbstractManagement implements ITransferObject{

	private static final long serialVersionUID = 1L;
	
	private String  inspectionId;
	private String  containerId;
	private String  shippingCompany;
	private String  labor;

}
