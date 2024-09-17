package com.tmm.myre.inspections.dto;






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
public class InspectionDto implements ITransferObject{
	
	private static final long serialVersionUID = 1L;
	
	private String  inspectionId;
	private String  part;
	private String  component;
	private Integer  damage;
	private String  location;
	private String  repair;
	private String  damageGenSet;
	private Integer  damageCode;
	private String  reference;
	private Integer  customerType;
	private String  photo;
	private Integer  status;
	private String  containerId;
	
	private String  length;
	private String  width;
	private String  depth;
	private String  otherLength;
	
	private Integer extentLarge;
	private Integer extentHeigth;
	private Integer extentDepth;
	private Integer extentOtherLarge;
	
	private String  quantity;
	private Integer  repairInspection;
	
	private String labor;
	
	
	
}
