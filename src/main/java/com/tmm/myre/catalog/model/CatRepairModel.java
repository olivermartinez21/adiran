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
@Table(name = "MYRE_CAT_REPAIR", schema = "MYRE")
public class CatRepairModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "REPAIR_ID")private String  repairId;
	@Column(name = "REPAIR_CODE")private String  repairCode;
	@Column(name = "REPAIR_DESCRIPTION")private String  repairDescription;
	@Column(name = "ENGLISH_DESCRIPTION")private String  englishDescription;
}
