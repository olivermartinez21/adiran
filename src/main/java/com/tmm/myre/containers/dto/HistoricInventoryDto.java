package com.tmm.myre.containers.dto;

import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;
import lombok.*;

import javax.persistence.Column;
import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HistoricInventoryDto extends AbstractManagement implements ITransferObject {

    private static final long serialVersionUID = 1L;

    private Integer inventoryHistoricId;
    private String shippingCompanyDescription;
    private String container;
    private String condition;
    private String containerType;
    private String nomenclatura;
    private String conditionPregate;
    private String location;
    private Date dateInspection;
    private String clasification;
    private Long daysOfStay;
    private String finalDate;
    private String statusQuote;
    private String aptTo;
    private String typeServicePregate;
    private LocalDateTime registerDate;
    private String comments;
    private Date uploadDate;
}
