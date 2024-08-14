package com.tmm.myre.photo.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import com.tmm.myre.base.model.IModel;

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
@Entity
@Table(name = "MYRE_PHOTOS", schema = "MYRE")
public class PhotoModel implements IModel {
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PHOTO_ID")private String photoId;
	@Column(name = "CONTAINER_ID")private String containerId;
	@Column(name = "PHOTO")private byte[] photo;
	@Column(name = "PHOTO_POSITION")private String photoPosition;
	

}