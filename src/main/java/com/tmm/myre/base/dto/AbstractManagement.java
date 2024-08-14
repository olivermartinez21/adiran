/**
 * 
 */
package com.tmm.myre.base.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author joshm
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AbstractManagement {
	
	private Integer idUser;
	private String operation;
	private Date managmentDate;
	private String log;
	
}
