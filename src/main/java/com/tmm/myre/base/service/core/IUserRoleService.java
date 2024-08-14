/**
 * 
 */
package com.tmm.myre.base.service.core;

import java.util.List;

import com.tmm.myre.base.dto.ComboBox;



/**
 * @author joshm
 *
 */
public interface IUserRoleService {

	/**
	 * @param userId
	 * @return
	 */
	List<ComboBox> getComboByUser(String userId);


}
