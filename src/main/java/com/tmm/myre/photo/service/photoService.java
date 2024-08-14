package com.tmm.myre.photo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.photo.converter.PhotosConverter;
import com.tmm.myre.photo.dto.PhotosDto;
import com.tmm.myre.photo.model.PhotoModel;
import com.tmm.myre.photo.repository.IPhotoRepository;
import com.tmm.myre.photo.service.core.IPhotorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("photoService")
public class photoService implements IPhotorService{
	
	@Autowired
	private IPhotoRepository photoRepository;
	
	@Autowired
	private PhotosConverter photosConverter;
	
	@Override
	public List<PhotosDto> getPhotos(String containerId) throws ConverterException {
		List<PhotoModel> photos = photoRepository.getPhotos(containerId);
		List<PhotosDto> list = new ArrayList<PhotosDto>();
			for(PhotoModel entity : photos) {
					list.add(photosConverter.convert(entity));
				
			}
			
		return list;
	}


	@Override
	public PhotosDto getOne(String photoId) throws ConverterException {
		PhotoModel iamgen = photoRepository.getById(photoId);
		return photosConverter.convert(iamgen);
	}


	@Override
	public ResponseManagement deleteImage(String photoId) throws ConverterException {
		ResponseManagement response = ResponseManagement.builder().operation(KeyConstants.UPDATE).build();
		if(photoRepository.existsById(photoId)) {
			photoRepository.deleteById(photoId);
		}
		
		response.setSuccess(true);
		response.setOperation(KeyConstants.UPDATE);
		return response;
	}
}
