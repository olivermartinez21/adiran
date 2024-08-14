package com.tmm.myre.photo.service.core;

import java.util.List;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.photo.dto.PhotosDto;

public interface IPhotorService {

	List<PhotosDto> getPhotos(String containerId) throws ConverterException;

	PhotosDto getOne(String photoId)throws ConverterException;

	ResponseManagement deleteImage(String photoId) throws ConverterException;

}
