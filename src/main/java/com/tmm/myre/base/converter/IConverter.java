/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.converter;

import com.tmm.myre.base.dto.ITransferObject;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.model.IModel;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
public interface IConverter<M extends IModel, T extends ITransferObject>{

	M convert(T to) throws ConverterException;
	
	T convert(M entity) throws ConverterException;

}
