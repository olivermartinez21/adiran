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
@Table(name = "MYRE_CAT_TRANSPORTCOMPANY", schema = "MYRE")
public class CatTransportCompanyModel implements IModel{

private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TRANSPORT_ID")private String  transportId;
	@Column(name = "TRANSPORT_CODE")private String  transportCode;
	@Column(name = "TRANSPORT_NAME")private String  transportName;
}
