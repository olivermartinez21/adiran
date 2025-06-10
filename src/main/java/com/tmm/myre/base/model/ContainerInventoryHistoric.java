package com.tmm.myre.base.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONTAINER_INVENTORY_HISTORIC", schema = "MYRE")
public class ContainerInventoryHistoric implements IModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVENTORY_HISTORIC_ID")
    private Integer inventoryHistoricId;

    @Column(name = "SHIPPING_COMPANY_DESCRIPTION")
    private String shippingCompanyDescription;

    @Column(name = "CONTAINER_NUMBER")
    private String container;

    @Column(name = "CONTAINER_CONDITION")
    private String condition;

    @Column(name = "CONTAINER_TYPE")
    private String containerType;

    @Column(name = "NOMENCLATURA")
    private String nomenclatura;

    @Column(name = "CONTAINER_CONDITION_PREGATE")
    private String conditionPregate;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "DATE_INSPECTION_CONTAINER")
    private Date dateInspection;

    @Column(name = "CLASIFICATION")
    private String clasification;

    @Column(name = "DAYS_OF_STAY")
    private Long daysOfStay;

    @Column(name = "FINAL_DATE")
    private String finalDate;

    @Column(name = "STATUS_QUOTE")
    private String statusQuote;

    @Column(name = "APT_TO")
    private String aptTo;

    @Column(name = "TYPE_SERVICE_PREGATE")
    private String typeServicePregate;

    @Column(name = "REGISTER_DATE")
    private LocalDateTime registerDate;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "UPLOAD_DATE")
    private Date uploadDate;
}
