package com.tmm.myre.inspections.model;



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
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MYRE_INSPECTIONS", schema = "MYRE")
public class InspectionModel implements IModel {
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "INSPECTION_ID")private String inspectionId;
	@Column(name = "PART")private String part;
	@Column(name = "DAMAGE")private Integer damage;
	@Column(name = "DAMAGE_CODE")private Integer damageCode;
	@Column(name = "DAMAGE_GENSET")private String damageGenSet;
	@Column(name = "REFERENCE")private String reference;
	@Column(name = "CUSTOMER_TYPE")private Integer customerType;
	@Column(name = "IMAGE")private String photo;
	@Column(name = "STATUS")private Integer status;
	@Column(name = "CONTAINER_ID")private String containerId;
	
	@Column(name = "COMPONET")private String  component;
	@Column(name = "LOCATION")private String  location;
	@Column(name = "REPAIR")private String  repair;
	
	@Column(name = "LENGTH")private String  length;
	@Column(name = "WIDTH")private String  width;
	@Column(name = "DEPTH")private String  depth;
	@Column(name = "OTHER_LENGTH")private String  otherLength;
	
	@Column(name = "EXTENT_LARGE") private Integer extentLarge;
	@Column(name = "EXTENT_HEIGHT")private Integer extentHeight;
	@Column(name = "EXTENT_DEPTH")private Integer extentDepth;
	@Column(name = "EXTENT_OTHER_LENGTH")private Integer extentOtherLarge;

	
	@Column(name = "QUANTITY")private String  quantity;

}
