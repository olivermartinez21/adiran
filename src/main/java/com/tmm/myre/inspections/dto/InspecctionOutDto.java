package com.tmm.myre.inspections.dto;

import java.sql.Date;

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
public class InspecctionOutDto implements ITransferObject{

	private static final long serialVersionUID = 1L;
	
	private String  inspectionId;
	private byte[]  photo;
	private String  containerId;
	private Date inspectionDateOut;
}
