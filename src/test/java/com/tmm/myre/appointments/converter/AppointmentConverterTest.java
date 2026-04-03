package com.tmm.myre.appointments.converter;

import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.base.exception.ConverterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para AppointmentConverter.
 * Si modificas campos en AppointmentDto / AppointmentModel estas pruebas fallarán,
 * alertándote que el converter quedó desactualizado.
 */
class AppointmentConverterTest {

    private AppointmentConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AppointmentConverter();
    }

    // ---------------------------------------------------------------
    // DTO → Entity
    // ---------------------------------------------------------------
    @Test
    @DisplayName("convert(dto) debe mapear todos los campos al modelo")
    void convertDtoToModel() throws ConverterException {
        Date today = new Date(System.currentTimeMillis());
        AppointmentDto dto = AppointmentDto.builder()
                .appointmentId("APT-001")
                .location("AGS")
                .folio("AGS-001")
                .date(today)
                .creationAppointmentDate(today)
                .telephone("4491234567")
                .agency("Agencia Test")
                .customer("Cliente Test")
                .customerType(1)
                .containers(5)
                .containersType(2)
                .shippingCompany("MAERSK")
                .invoincingType(1)
                .eventType(1)
                .paymentType(2)
                .companyName("TMM")
                .rfc("TMM010101AAA")
                .fiscalAdress("Calle 1")
                .user("admin")
                .buque("BUQUE-TEST")
                .origin("MEXICO")
                .status(1)
                .paymentCheck("checked")
                .build();

        AppointmentModel model = converter.convert(dto);

        assertNotNull(model, "El modelo no debe ser nulo");
        assertEquals("APT-001",     model.getAppointmentId());
        assertEquals("AGS",         model.getLocation());
        assertEquals("AGS-001",     model.getFolio());
        assertEquals(today,         model.getDate());
        assertEquals(today,         model.getCreationAppointmentDate());
        assertEquals("4491234567",  model.getTelephone());
        assertEquals("Agencia Test",model.getAgency());
        assertEquals("Cliente Test",model.getCustomer());
        assertEquals(1,             model.getCustomerType());
        assertEquals(5,             model.getContainers());
        assertEquals(2,             model.getContainersType());
        assertEquals("MAERSK",      model.getShippingCompany());
        assertEquals(1,             model.getInvoincingType());
        assertEquals(1,             model.getEventType());
        assertEquals(2,             model.getPaymentType());
        assertEquals("TMM",         model.getCompanyName());
        assertEquals("TMM010101AAA",model.getRfc());
        assertEquals("Calle 1",     model.getFiscalAdress());
        assertEquals("admin",       model.getUser());
        assertEquals("BUQUE-TEST",  model.getBuque());
        assertEquals("MEXICO",      model.getOrigin());
        assertEquals(1,             model.getStatus());
        assertEquals("checked",     model.getPaymentCheck());
    }

    // ---------------------------------------------------------------
    // Entity → DTO
    // ---------------------------------------------------------------
    @Test
    @DisplayName("convert(model) debe mapear todos los campos al dto")
    void convertModelToDto() throws ConverterException {
        Date today = new Date(System.currentTimeMillis());
        AppointmentModel model = AppointmentModel.builder()
                .appointmentId("APT-002")
                .location("GDL")
                .folio("GDL-001")
                .date(today)
                .creationAppointmentDate(today)
                .telephone("3331234567")
                .agency("Agencia GDL")
                .customer("Cliente GDL")
                .customerType(2)
                .containers(3)
                .containersType(1)
                .shippingCompany("MSC")
                .invoincingType(2)
                .eventType(2)
                .paymentType(1)
                .companyName("TMM GDL")
                .rfc("TMGG010101BBB")
                .fiscalAdress("Av. 2")
                .user("user1")
                .buque("BUQUE-GDL")
                .origin("VERACRUZ")
                .status(2)
                .paymentCheck("pending")
                .build();

        AppointmentDto dto = converter.convert(model);

        assertNotNull(dto, "El dto no debe ser nulo");
        assertEquals("APT-002",      dto.getAppointmentId());
        assertEquals("GDL",          dto.getLocation());
        assertEquals("GDL-001",      dto.getFolio());
        assertEquals(today,          dto.getDate());
        assertEquals(today,          dto.getCreationAppointmentDate());
        assertEquals("3331234567",   dto.getTelephone());
        assertEquals("Agencia GDL",  dto.getAgency());
        assertEquals("Cliente GDL",  dto.getCustomer());
        assertEquals(2,              dto.getCustomerType());
        assertEquals(3,              dto.getContainers());
        assertEquals(1,              dto.getContainersType());
        assertEquals("MSC",          dto.getShippingCompany());
        assertEquals(2,              dto.getInvoincingType());
        assertEquals(2,              dto.getEventType());
        assertEquals(1,              dto.getPaymentType());
        assertEquals("TMM GDL",      dto.getCompanyName());
        assertEquals("TMGG010101BBB",dto.getRfc());
        assertEquals("Av. 2",        dto.getFiscalAdress());
        assertEquals("user1",        dto.getUser());
        assertEquals("BUQUE-GDL",    dto.getBuque());
        assertEquals("VERACRUZ",     dto.getOrigin());
        assertEquals(2,              dto.getStatus());
        assertEquals("pending",      dto.getPaymentCheck());
    }

    @Test
    @DisplayName("convertir dto con campos nulos no debe lanzar excepción")
    void convertDtoConCamposNulos() {
        AppointmentDto dto = new AppointmentDto();
        assertDoesNotThrow(() -> converter.convert(dto));
    }
}

