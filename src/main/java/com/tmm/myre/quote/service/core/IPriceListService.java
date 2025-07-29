package com.tmm.myre.quote.service.core;

import com.tmm.myre.catalog.dto.CatShippingCompanyDto;
import org.springframework.web.multipart.MultipartFile;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.quote.dto.QuoteDto;

import java.util.Map;

public interface IPriceListService {

    ResponseManagement createLisprice(MultipartFile file) throws ConverterException;

    void updateJobcode(Map<String, Object> payload);

    //ResponseManagement updateLaborByShippingCompanyId(CatShipping CompanyDto catShippingCompanyDto) throws ConverterException;

    ResponseManagement updateLaborAndExchangeByShippingCompanyId(String shippingCompanyId, String maneuverCost, String jobcodeId, String laborRate) throws ConverterException;

    String getExchangeByShippingCompanyId(String shippingCompanyId);

}
