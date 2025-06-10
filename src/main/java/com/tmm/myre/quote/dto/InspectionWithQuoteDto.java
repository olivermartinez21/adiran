package com.tmm.myre.quote.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InspectionWithQuoteDto {
    private String inspectionId;
    private String part;
    private String component;
    private Integer damage;
    private String location;
    private String repair;
    private String damageGenSet;
    private Integer damageCode;
    private String reference;
    private Integer customerType;
    private String customerName;
    private String photo;
    private Integer status;
    private String containerId;
    private String length;
    private String width;
    private String depth;
    private String otherLength;
    private Integer extentLarge;
    private Integer extentHeigth;
    private Integer extentDepth;
    private Integer extentOtherLarge;
    private String quantity;

    // Campos de Quote
    private String quoteId;
    private String workCode;
    private String repairDescription;
    private String hours;
    private String labor;
    private String material;
    private String tarifa;
    private String exchange;
}

