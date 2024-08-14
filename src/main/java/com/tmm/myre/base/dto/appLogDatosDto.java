package com.tmm.myre.base.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class appLogDatosDto extends AbstractManagement implements ITransferObject{

	
private static final long serialVersionUID = 1L;
	
	private String  x;
	private String  y;
	
}
