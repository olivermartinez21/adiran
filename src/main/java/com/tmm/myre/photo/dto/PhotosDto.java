package com.tmm.myre.photo.dto;


import java.io.File;

import com.tmm.myre.base.dto.ITransferObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhotosDto implements ITransferObject{

	private static final long serialVersionUID = 1L;
	
	private String  photoId;
	private byte[]  photo;
	private String  containerId;
	private String photoPosition;
	private File photoFile;
}
