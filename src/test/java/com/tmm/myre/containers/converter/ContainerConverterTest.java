package com.tmm.myre.containers.converter;

import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.assignments.repository.IBookingRepository;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.base.exception.ConverterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ContainerConverter.
 * Si añades/quitas campos en ContainerDto o ContainerModel,
 * estas pruebas deben actualizarse, detectando el cambio de inmediato.
 */
@ExtendWith(MockitoExtension.class)
class ContainerConverterTest {

    @Mock
    private IAssignmentRepository assignmentRepository;

    @Mock
    private IBookingRepository bookingRepository;

    @InjectMocks
    private ContainerConverter converter;

    private static final LocalDateTime NOW = LocalDateTime.now();

    // ---------------------------------------------------------------
    // DTO → Entity
    // ---------------------------------------------------------------
    @Test
    @DisplayName("convert(dto) debe mapear campos principales al modelo")
    void convertDtoToModel() throws ConverterException {
        ContainerDto dto = ContainerDto.builder()
                .containerId("CNT-001")
                .container("ABCD1234567")
                .containerType(1)
                .containerSize("40HC")
                .shippingCompany("MAERSK")
                .appointmentId("APT-001")
                .status(1)
                .location("AGS")
                .eventType(1)
                .condition("DAÑADO")
                .clasification("IMPORTACION")
                .definition("DEF-1")
                .destination("MONTERREY")
                .chassis("CH-001")
                .buque("BUQUE-1")
                .bl("BL-001")
                .operatorName("Juan")
                .plate("ABC-1234")
                .economicNumber("ECO-001")
                .conditionPregate("BUENO")
                .noInvoice("INV-001")
                .registerDate(NOW)
                .build();

        ContainerModel model = converter.convert(dto);

        assertNotNull(model);
        assertEquals("CNT-001",        model.getContainerId());
        assertEquals("ABCD1234567",    model.getContainer());
        assertEquals(1,                model.getContainerType());
        assertEquals("40HC",           model.getContaierSize());
        assertEquals("MAERSK",         model.getShippingCompany());
        assertEquals("APT-001",        model.getAppointmentId());
        assertEquals(1,                model.getStatus());
        assertEquals("AGS",            model.getLocation());
        assertEquals(1,                model.getEventType());
        assertEquals("DAÑADO",         model.getCondition());
        assertEquals("IMPORTACION",    model.getClasification());
        assertEquals("DEF-1",          model.getDefinition());
        assertEquals("MONTERREY",      model.getDestination());
        assertEquals("CH-001",         model.getChassis());
        assertEquals("BUQUE-1",        model.getBuque());
        assertEquals("BL-001",         model.getBl());
        assertEquals("Juan",           model.getOperatorName());
        assertEquals("ABC-1234",       model.getPlate());
        assertEquals("ECO-001",        model.getEconomicNumber());
        assertEquals("BUENO",          model.getConditionPregate());
        assertEquals("INV-001",        model.getNoInvoice());
        assertEquals(NOW,              model.getRegisterDate());
    }

    // ---------------------------------------------------------------
    // Entity → DTO
    // ---------------------------------------------------------------
    @Test
    @DisplayName("convert(model) debe mapear campos principales al dto")
    void convertModelToDto() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("CNT-002")
                .container("EFGH9876543")
                .containerType(2)
                .contaierSize("20ST")
                .shippingCompany("MSC")
                .appointmentId("APT-002")
                .status(3)
                .location("GDL")
                .eventType(2)
                .condition("BUENO")
                .clasification("EXPORTACION")
                .definition("DEF-2")
                .destination("VERACRUZ")
                .chassis("CH-002")
                .buque("BUQUE-2")
                .bl("BL-002")
                .operatorName("Pedro")
                .plate("XYZ-5678")
                .economicNumber("ECO-002")
                .conditionPregate("REGULAR")
                .noInvoice("INV-002")
                .registerDate(NOW)
                .build();

        ContainerDto dto = converter.convert(model);

        assertNotNull(dto);
        assertEquals("CNT-002",        dto.getContainerId());
        assertEquals("EFGH9876543",    dto.getContainer());
        assertEquals(2,                dto.getContainerType());
        assertEquals("20ST",           dto.getContainerSize());
        assertEquals("MSC",            dto.getShippingCompany());
        assertEquals("APT-002",        dto.getAppointmentId());
        assertEquals(3,                dto.getStatus());
        assertEquals("GDL",            dto.getLocation());
        assertEquals(2,                dto.getEventType());
        assertEquals("BUENO",          dto.getCondition());
        assertEquals("EXPORTACION",    dto.getClasification());
        assertEquals("DEF-2",          dto.getDefinition());
        assertEquals("VERACRUZ",       dto.getDestination());
        assertEquals("CH-002",         dto.getChassis());
        assertEquals("BUQUE-2",        dto.getBuque());
        assertEquals("BL-002",         dto.getBl());
        assertEquals("Pedro",          dto.getOperatorName());
        assertEquals("XYZ-5678",       dto.getPlate());
        assertEquals("ECO-002",        dto.getEconomicNumber());
        assertEquals("REGULAR",        dto.getConditionPregate());
        assertEquals("INV-002",        dto.getNoInvoice());
        assertEquals(NOW,              dto.getRegisterDate());
    }

    @Test
    @DisplayName("convertir dto vacío no debe lanzar excepción")
    void convertDtoVacioNoLanzaExcepcion() {
        ContainerDto dto = new ContainerDto();
        assertDoesNotThrow(() -> converter.convert(dto));
    }

    @Test
    @DisplayName("convertir model vacío no debe lanzar excepción")
    void convertModelVacioNoLanzaExcepcion() {
        ContainerModel model = new ContainerModel();
        assertDoesNotThrow(() -> converter.convert(model));
    }
}


