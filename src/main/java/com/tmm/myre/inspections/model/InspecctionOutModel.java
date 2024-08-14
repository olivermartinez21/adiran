package com.tmm.myre.inspections.model;

import java.sql.Date;

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
@Table(name = "MYRE_INSPECTIONS_OUT", schema = "MYRE")
public class InspecctionOutModel  implements IModel{
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "INSPECTIONS_OUT_ID")private String inspectionId;
	@Column(name = "IMAGE")private byte[] photo;
	@Column(name = "CONTAINER_ID")private String containerId;
	@Column(name = "INSPECTION_DATE_OUT")private Date inspectionDateOut;
}
