package com.tmm.myre.quote.service.core;

import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.quote.dto.QuoteDto;

public interface IQuoteService {

	ResponseManagement saveInformationQuote(QuoteDto quoteDto) throws ConverterException;

}
