package com.tmm.myre.userRegister.service.core;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.userRegister.dto.UserRegisterDto;

public interface IUserInformationService {

	ResponseManagement saveUserInformation(UserRegisterDto userInformationDto)throws ConverterException;

}
