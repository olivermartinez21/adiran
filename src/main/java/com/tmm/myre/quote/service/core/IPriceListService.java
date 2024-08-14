package com.tmm.myre.quote.service.core;

import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.quote.dto.QuoteDto;

public interface IPriceListService {

	ResponseManagement createLisprice(MultipartFile file,String type) throws ConverterException;


}
