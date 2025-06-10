package com.tmm.myre.base.configuration.secondary;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MandRSolicitudSalida", schema = "Integracion")
public class MandREmptyContainerReleaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "IDSalida")
    private Integer iDSalida;

    @Column(name = "Contenedor")
    private String contenedor;

    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Model")
    private String model;

    @Column(name = "Booking")
    private String booking;

    @Column(name = "FechaEDI")
    private LocalDateTime fechaEDI;

    @Column(name = "CantidadUnidades")
    private Integer cantidadUnidades;

    @Column(name = "EDIFilename")
    private String ediFilename;

    @Column(name = "ProcessTimeStamp")
    private LocalDateTime processTimeStamp;

    @Column(name = "CustomerIdentifier")
    private String customerIdentifier;

    @Column(name = "CarrierCustomer")
    private String carrierCustomer;

    @Column(name = "CarrierShipper")
    private String carrierShipper;

    @Column(name = "Status")
    private String status;

    @Column(name = "Shipment")
    private String shipment;

    @Column(name = "AdviceNum")
    private String adviceNum;

    @Column(name = "ReleaseDate")
    private LocalDateTime releaseDate;

    @Column(name = "ExpirationDate")
    private LocalDateTime expirationDate;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "FreeTxT")
    private String freeTxt;

    @Column(name = "Assigned")
    private Boolean assigned;

    @Column(name = "Sequence")
    private String sequence;

    @Column(name = "Haulage")
    private String haulage;

    @Column(name = "TareWeight")
    private String tareWeight;

    @Column(name = "TareWeightUnit")
    private String tareWeightUnit;

    @Column(name = "Temperature")
    private String temperature;

    @Column(name = "TemperatureUnit")
    private String temperatureUnit;

    @Column(name = "VentilationOption")
    private String ventilationOption;

    @Column(name = "VentilationOptionUnit")
    private String ventilationOptionUnit;

    @Column(name = "FreshAir")
    private String freshAir;

    @Column(name = "FreshAirUnit")
    private String freshAirUnit;

    @Column(name = "Humidity")
    private String humidity;

    @Column(name = "HumidityUnit")
    private String humidityUnit;

    @Column(name = "CO2")
    private String co2;

    @Column(name = "CO2Unit")
    private String co2Unit;

    @Column(name = "O2")
    private String o2;

    @Column(name = "O2Unit")
    private String o2Unit;

    @Column(name = "N")
    private String n;

    @Column(name = "NUnit")
    private String nUnit;

    @Column(name = "Localidad")
    private String localidad;
}
