package com.tmm.myre.userRegister.model;

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
@Table(name = "MYRE_USER_INFORMATION", schema = "MYRE")
public class UserRegisterModel  implements IModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_USER_INFORMATION")private String id;
	@Column(name = "NAME")private String name;
	@Column(name = "AGENCY")private String agency;
	@Column(name = "CODE")private String code;
	@Column(name = "ID_USER")private Integer idUser;
	
}
