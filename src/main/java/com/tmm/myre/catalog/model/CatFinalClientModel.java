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
@Table(name = "MYRE_CAT_FINAL_CLIENT", schema = "MYRE")
public class CatFinalClientModel implements IModel{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "FINAL_CLIENT_ID")private String  finalClientId;
	@Column(name = "CLIENT_NAME")private String  clientName;
}
