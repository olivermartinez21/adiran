package com.tmm.myre.containers.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tmm.myre.base.model.IModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REPORTE_MANIOBRAS", schema = "MYRE")
public class ReporteManiobraModel implements IModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "CONTAINER_ID")
    private String containerId;

    @Column(name = "EIR")
    private String eir;

    @Column(name = "LOCALIDAD")
    private String localidad;

    @Column(name = "TIPO_ACTIVIDAD")
    private String tipoActividad;

    @Column(name = "TIPO_UNIDAD")
    private String tipoUnidad;

    @Column(name = "FECHA_EVENTO")
    private LocalDateTime fechaEvento;

    @Column(name = "UNIDAD")
    private String unidad;

    @Column(name = "GRADO_CALIDAD")
    private String gradoCalidad;

    @Column(name = "NUMERO_BOOKING")
    private String numeroBooking;

    @Column(name = "TIPO_UNIDAD_2")
    private String tipoUnidad2;

    @Column(name = "TAMANO")
    private String tamano;

    @Column(name = "NOMENCLATURA")
    private String nomenclatura;

    @Column(name = "AUTORIZACION_CLIENTE")
    private String autorizacionCliente;

    @Column(name = "FACTURA_SAP")
    private String facturaSap;

    @Column(name = "COSTO_MANIOBRA")
    private String costoManiobra;

    @Column(name = "MONEDA")
    private String moneda;

    @Column(name = "SELLO_CALIDAD")
    private String selloCalidad;

    @Column(name = "SELLO_SEGURIDAD")
    private String selloSeguridad;

    @Column(name = "TRANSMITIR_EDI")
    private Boolean transmitirEdi;

    @Column(name = "ESTATUS_EDI")
    private String estatusEdi;

    @Column(name = "REG_INSERTADO_TABLA_EDI")
    private Boolean regInsertadoTablaEdi;

    @Column(name = "REG_ENVIADO_EDI")
    private Boolean regEnviadoEdi;

    @Column(name = "ARCHIVO_EDI")
    private String archivoEdi;

    @Column(name = "PROPIETARIO")
    private String propietario;

    @Column(name = "COBRAR_A")
    private String cobrarA;

    @Column(name = "TIPO_SERVICIO")
    private String tipoServicio;

    @Column(name = "COMPANIA_TRANSPORTISTA")
    private String companiaTransportista;

    @Column(name = "NOMBRE_OPERADOR")
    private String nombreOperador;

    @Column(name = "PLACAS_TRANSPORTE")
    private String placasTransporte;

    @Column(name = "NUMERO_ECONOMICO")
    private String numeroEconomico;

    @Column(name = "ORIGEN")
    private String origen;

    @Column(name = "PLANTA_DESTINO")
    private String plantaDestino;

    @Column(name = "ANDEN")
    private String anden;

    @Column(name = "TIPO_ENTREGA")
    private String tipoEntrega;

    @Column(name = "shippingCompany")
    private String shippingCompany;
}
