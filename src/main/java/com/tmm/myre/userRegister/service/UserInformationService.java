package com.tmm.myre.userRegister.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.administration.dto.UserTableDto;
import com.tmm.myre.administration.service.core.IUserManagementService;
import com.tmm.myre.base.controller.MainController;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.repository.IAppUserRepository;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.base.utils.UuidProvider;
import com.tmm.myre.userRegister.converter.UserRegisterConverter;
import com.tmm.myre.userRegister.dto.UserRegisterDto;
import com.tmm.myre.userRegister.model.UserRegisterModel;
import com.tmm.myre.userRegister.repository.IUserRegisterRepository;
import com.tmm.myre.userRegister.service.core.IUserInformationService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("userInformationService")
public class UserInformationService implements IUserInformationService{
	
	@Autowired
	private IUserManagementService userManagementService;
	
	@Autowired
	private IAppUserRepository appUserRepository;
	
	@Autowired
	private UserRegisterConverter userRegisterConverter;
	
	@Autowired
	private IUserRegisterRepository userRegisterRepository;
	
	
	@Override
	public ResponseManagement saveUserInformation(UserRegisterDto userRegisterDto) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.INSERT).success(false).build();
		try {
			
			
			UserRegisterModel userRegisterModel= userRegisterConverter.convert(userRegisterDto);
			userRegisterModel.setIdUser(appUserRepository.getNext());
			userRegisterModel.setId(UuidProvider.getUUID());
			
		
				
			UserTableDto userTableDto = UserTableDto.builder()
					.userName(userRegisterDto.getUser())
					.roleList("1")
					.operation(KeyConstants.INSERT)
					.idUser(0)
					.password(userRegisterDto.getPassword())
					.build(); 
			
			response = userManagementService.saveNewUser(userTableDto);
			if(response.getSuccess()==true) {
				userRegisterRepository.save(userRegisterModel);
			}else {
				response.setSuccess(false);
			}
			
		} catch (Exception ex) {
			response.setSuccess(false);
			response.setErrorCode(KeyConstants.SERVICE_ERROR_CODE);
			response.setMessage(KeyConstants.SERVICE_ERROR + ex.toString());
		}
		
		return response;
	}

}
