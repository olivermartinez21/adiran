package com.tmm.myre.userRegister.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.userRegister.dto.UserRegisterDto;
import com.tmm.myre.userRegister.model.UserRegisterModel;
@Component("userRegisterConverter")
public class UserRegisterConverter  implements IConverter<UserRegisterModel, UserRegisterDto> {

	@Override
	public UserRegisterModel convert(UserRegisterDto to) throws ConverterException {
		UserRegisterModel entity = UserRegisterModel.builder()
				.id(to.getId())
				.name(to.getName())
				.agency(to.getAgency())
				.idUser(to.getIdUser())
				.build();
		return entity;
	}

	@Override
	public UserRegisterDto convert(UserRegisterModel entity) throws ConverterException {
		UserRegisterDto to = UserRegisterDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.agency(entity.getAgency())
				.idUser(entity.getIdUser())
				.build();
		return to;
	}

}
