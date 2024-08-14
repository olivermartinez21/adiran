package com.tmm.myre.photo.converter;

import org.springframework.stereotype.Component;

import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.photo.dto.PhotosDto;
import com.tmm.myre.photo.model.PhotoModel;
@Component("photosConverter")
public class PhotosConverter  implements IConverter<PhotoModel, PhotosDto>{

	@Override
	public PhotoModel convert(PhotosDto to) throws ConverterException {
		PhotoModel entity = PhotoModel.builder()
				.photoId(to.getPhotoId())
				.containerId(to.getContainerId())
				.photo(to.getPhoto())
				.photoPosition(to.getPhotoPosition())
				.build();
		return entity;
	}

	@Override
	public PhotosDto convert(PhotoModel entity) throws ConverterException {
		PhotosDto to = PhotosDto.builder()
				.photoId(entity.getPhotoId())
				.containerId(entity.getContainerId())
				.photo(entity.getPhoto())
				.photoPosition(entity.getPhotoPosition())
				.build();
		return to;
	}

}
