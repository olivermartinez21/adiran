package com.tmm.myre.inspections.service.core;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;


public interface IInspectionsOutService {

	ResponseManagement saveInspectionOut(List<MultipartFile> file, String container) throws ConverterException;


	//ResponseManagement inspectionSave(String inspectionList)  throws ConverterException;

}
