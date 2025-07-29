package com.tmm.myre.quote.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceListExcelDto {

    @ExcelCellName("ID") private String id;
    @ExcelCellName("CODIGO DE TRABAJO") private String jobCodeRepair;
    @ExcelCellName("DESCRIPCION DE TRABAJO") private String jobCodeDescription;
    @ExcelCellName("MATERIAL") private String jobCodeMaterial;
    @ExcelCellName("HH") private Double jobCodeHours;
    @ExcelCellName("MONEDA") private String jobCodeCurrency;
    @ExcelCellName("CLIENTE") private String jobCodeClient;

}
