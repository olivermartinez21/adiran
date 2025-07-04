package com.tmm.myre.containers.converter;


import com.tmm.myre.base.converter.IConverter;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.containers.dto.ReporteManiobraDto;
import com.tmm.myre.containers.model.ReporteManiobraModel;
import org.springframework.stereotype.Component;


@Component("reporteManiobraConverter")
public class ReporteManiobraConverter implements IConverter<ReporteManiobraModel, ReporteManiobraDto> {

    @Override
    public ReporteManiobraModel convert(ReporteManiobraDto dto) throws ConverterException {
        try {
            return ReporteManiobraModel.builder()
                    .id(dto.getId())
                    .containerId(dto.getContainerId())
                    .eir(dto.getEir())
                    .localidad(dto.getLocalidad())
                    .tipoActividad(dto.getTipoActividad())
                    .tipoUnidad(dto.getTipoUnidad())
                    .fechaEvento(dto.getFechaEvento())
                    .unidad(dto.getUnidad())
                    .gradoCalidad(dto.getGradoCalidad())
                    .numeroBooking(dto.getNumeroBooking())
                    .tipoUnidad2(dto.getTipoUnidad2())
                    .tamano(dto.getTamano())
                    .nomenclatura(dto.getNomenclatura())
                    .autorizacionCliente(dto.getAutorizacionCliente())
                    .facturaSap(dto.getFacturaSap())
                    .costoManiobra(dto.getCostoManiobra())
                    .moneda(dto.getMoneda())
                    .selloCalidad(dto.getSelloCalidad())
                    .selloSeguridad(dto.getSelloSeguridad())
                    .transmitirEdi(dto.getTransmitirEdi())
                    .estatusEdi(dto.getEstatusEdi())
                    .regInsertadoTablaEdi(dto.getRegInsertadoTablaEdi())
                    .regEnviadoEdi(dto.getRegEnviadoEdi())
                    .archivoEdi(dto.getArchivoEdi())
                    .propietario(dto.getPropietario())
                    .cobrarA(dto.getCobrarA())
                    .tipoServicio(dto.getTipoServicio())
                    .companiaTransportista(dto.getCompaniaTransportista())
                    .nombreOperador(dto.getNombreOperador())
                    .placasTransporte(dto.getPlacasTransporte())
                    .numeroEconomico(dto.getNumeroEconomico())
                    .origen(dto.getOrigen())
                    .plantaDestino(dto.getPlantaDestino())
                    .anden(dto.getAnden())
                    .tipoEntrega(dto.getTipoEntrega())
                    .build();
        } catch (Exception e) {
            throw new ConverterException("Error converting DTO to Model", e);
        }
    }

    @Override
    public ReporteManiobraDto convert(ReporteManiobraModel entity) throws ConverterException {
        try {
            return ReporteManiobraDto.builder()
                    .id(entity.getId())
                    .containerId(entity.getContainerId())
                    .eir(entity.getEir())
                    .localidad(entity.getLocalidad())
                    .tipoActividad(entity.getTipoActividad())
                    .tipoUnidad(entity.getTipoUnidad())
                    .fechaEvento(entity.getFechaEvento())
                    .unidad(entity.getUnidad())
                    .gradoCalidad(entity.getGradoCalidad())
                    .numeroBooking(entity.getNumeroBooking())
                    .tipoUnidad2(entity.getTipoUnidad2())
                    .tamano(entity.getTamano())
                    .nomenclatura(entity.getNomenclatura())
                    .autorizacionCliente(entity.getAutorizacionCliente())
                    .facturaSap(entity.getFacturaSap())
                    .costoManiobra(entity.getCostoManiobra())
                    .moneda(entity.getMoneda())
                    .selloCalidad(entity.getSelloCalidad())
                    .selloSeguridad(entity.getSelloSeguridad())
                    .transmitirEdi(entity.getTransmitirEdi())
                    .estatusEdi(entity.getEstatusEdi())
                    .regInsertadoTablaEdi(entity.getRegInsertadoTablaEdi())
                    .regEnviadoEdi(entity.getRegEnviadoEdi())
                    .archivoEdi(entity.getArchivoEdi())
                    .propietario(entity.getPropietario())
                    .cobrarA(entity.getCobrarA())
                    .tipoServicio(entity.getTipoServicio())
                    .companiaTransportista(entity.getCompaniaTransportista())
                    .nombreOperador(entity.getNombreOperador())
                    .placasTransporte(entity.getPlacasTransporte())
                    .numeroEconomico(entity.getNumeroEconomico())
                    .origen(entity.getOrigen())
                    .plantaDestino(entity.getPlantaDestino())
                    .anden(entity.getAnden())
                    .tipoEntrega(entity.getTipoEntrega())
                    .build();
        } catch (Exception e) {
            throw new ConverterException("Error converting Model to DTO", e);
        }
    }
}

