package com.tmm.myre.catalog.dto;

import com.tmm.myre.base.dto.AbstractManagement;
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
public class JobcodeHistoricDto extends AbstractManagement implements ITransferObject {

    private static final long serialVersionUID = 1L;

    private int jobcodeHistoryId;
    private String jobcodeId;
    private String jobcodeRepair;
    private String jobcodeDescription;
    private String jobcodeMaterial;
    private String jobcodeHh;
    private String jobcodeExchange;
    private String jobcodeShippingId;
}
