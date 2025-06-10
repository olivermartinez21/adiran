package com.tmm.myre.containers.dto;

import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilterDto extends AbstractManagement implements ITransferObject {

    private static final long serialVersionUID = 1L;

    private String shippingCompany;
    private LocalDateTime registerDate;

    private Date dateInit;
    private Date dateEnd;
}
