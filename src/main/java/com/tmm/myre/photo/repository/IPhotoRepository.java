package com.tmm.myre.photo.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmm.myre.photo.model.PhotoModel;
@Repository("photoRepository")
public interface IPhotoRepository extends JpaRepository<PhotoModel, String>{

	@Query(value = "SELECT * FROM MYRE_PHOTOS where CONTAINER_ID=:containerId", nativeQuery = true)
	List<PhotoModel> getPhotos(String containerId);

	


}	

