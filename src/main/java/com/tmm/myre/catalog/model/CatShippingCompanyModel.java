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
@Table(name = "MYRE_CAT_SHIPPINGCOMPANY", schema = "MYRE")
public class CatShippingCompanyModel implements IModel{
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SHIPPINGCOMPANY_ID")private String  shippingCompanyId;
	@Column(name = "DESCRIPTION")private String  description;
	@Column(name = "CODE")private String  code;
	@Column(name = "LABOR")private String  labor;
}
