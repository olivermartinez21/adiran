package com.tmm.myre.base.service.core;

import com.tmm.myre.containers.dto.ReportFilterDto;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public interface IExcelUtils {

    byte[] inventoryExcel(ReportFilterDto reportFilterDto);

    byte[] maneuverExcel(ReportFilterDto reportFilterDto);

    byte[] hapagAds(ReportFilterDto reportFilterDto);

    byte[] exitDateReport(ReportFilterDto reportFilterDto);

    byte[] estimadoExcel(ReportFilterDto reportFilterDto);
}
