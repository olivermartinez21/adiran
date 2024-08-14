package com.tmm.myre.userRegister.dto;


import com.tmm.myre.base.dto.AbstractManagement;
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
public class UserRegisterDto extends AbstractManagement implements ITransferObject {

	public static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String agency;
	private String user;
	private String password;
	private Integer idUser;
	
}
