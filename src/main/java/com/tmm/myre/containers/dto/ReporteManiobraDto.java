package com.tmm.myre.containers.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.tmm.myre.base.dto.AbstractManagement;
import com.tmm.myre.base.dto.ITransferObject;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReporteManiobraDto extends AbstractManagement implements ITransferObject, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String containerId;
    private String eir;
    private String localidad;
    private String tipoActividad;
    private String tipoUnidad;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaEvento;

    private String unidad;
    private String gradoCalidad;
    private String numeroBooking;
    private String tipoUnidad2;
    private String tamano;
    private String nomenclatura;
    private String autorizacionCliente;
    private String facturaSap;
    private String costoManiobra;
    private String moneda;
    private String selloCalidad;
    private String selloSeguridad;
    private Boolean transmitirEdi;
    private String estatusEdi;
    private Boolean regInsertadoTablaEdi;
    private Boolean regEnviadoEdi;
    private String archivoEdi;
    private String propietario;
    private String cobrarA;
    private String tipoServicio;
    private String companiaTransportista;
    private String nombreOperador;
    private String placasTransporte;
    private String numeroEconomico;
    private String origen;
    private String plantaDestino;
    private String anden;
    private String tipoEntrega;
}
