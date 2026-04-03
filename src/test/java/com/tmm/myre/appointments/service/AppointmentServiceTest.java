package com.tmm.myre.appointments.service;

import com.tmm.myre.appointments.converter.AppointmentConverter;
import com.tmm.myre.appointments.dto.AppointmentDto;
import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.appointments.repository.IAppointmentRepository;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.service.core.IAppLogService;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.containers.converter.ContainerConverter;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.userRegister.model.UserRegisterModel;
import com.tmm.myre.userRegister.repository.IUserRegisterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AppointmentService.
 * Los repositorios se simulan con Mockito para no requerir BD.
 */
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private IAppointmentRepository appointmentRepository;
    @Mock private AppointmentConverter    appointmentConverter;
    @Mock private ContainerConverter      containerConverter;
    @Mock private IContainerRepository    containerRepository;
    @Mock private IUserRegisterRepository userRegisterRepository;
    @Mock private IAppLogService          appLogService;
    @Mock private IPdfGenerationService   pdfGenerationService;

    @InjectMocks
    private AppointmentService service;

    // ---------------------------------------------------------------
    // appointmentValidation
    // ---------------------------------------------------------------
    @Test
    @DisplayName("appointmentValidation: status=4 debe cambiar a status=5")
    void appointmentValidation_deStatus4aStatus5() {
        AppointmentModel model = AppointmentModel.builder()
                .appointmentId("APT-001")
                .status(4)
                .build();
        when(appointmentRepository.getById("APT-001")).thenReturn(model);

        ResponseManagement response = service.appointmentValidation("APT-001");

        assertTrue(response.getSuccess());
        assertEquals(KeyConstants.UPDATE, response.getOperation());
        assertEquals(5, model.getStatus());
        verify(appointmentRepository).save(model);
    }

    @Test
    @DisplayName("appointmentValidation: status distinto de 4 debe cambiar a status=2")
    void appointmentValidation_statusDistintoDe4CambiaA2() {
        AppointmentModel model = AppointmentModel.builder()
                .appointmentId("APT-002")
                .status(1)
                .build();
        when(appointmentRepository.getById("APT-002")).thenReturn(model);

        ResponseManagement response = service.appointmentValidation("APT-002");

        assertTrue(response.getSuccess());
        assertEquals(2, model.getStatus());
        verify(appointmentRepository).save(model);
    }

    @Test
    @DisplayName("appointmentValidation: si getById() lanza excepción, ésta se propaga (getById está fuera del try-catch)")
    void appointmentValidation_getByIdFueraDelTryCatch_propagaExcepcion() {
        // El getById está FUERA del try-catch en AppointmentService,
        // por eso la excepción no se captura y se propaga.
        // Este test documenta ese comportamiento actual.
        when(appointmentRepository.getById(anyString())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> service.appointmentValidation("APT-ERR"),
                "Se esperaba que la excepción se propague porque getById está fuera del try-catch");
    }

    @Test
    @DisplayName("appointmentValidation: excepción en save() es capturada y devuelve failure")
    void appointmentValidation_excepcionEnSaveResultaEnFallure() {
        AppointmentModel model = AppointmentModel.builder()
                .appointmentId("APT-SAVE-ERR")
                .status(1)
                .build();
        when(appointmentRepository.getById("APT-SAVE-ERR")).thenReturn(model);
        doThrow(new RuntimeException("Save failed")).when(appointmentRepository).save(any());

        ResponseManagement response = service.appointmentValidation("APT-SAVE-ERR");

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
        assertNotNull(response.getMessage());
    }

    // ---------------------------------------------------------------
    // getOne
    // ---------------------------------------------------------------
    @Test
    @DisplayName("getOne: debe retornar el dto del appointment solicitado")
    void getOneRetornaDtoCorrectamente() throws Exception {
        AppointmentModel model = AppointmentModel.builder()
                .appointmentId("APT-003")
                .folio("AGS-003")
                .build();
        AppointmentDto expectedDto = AppointmentDto.builder()
                .appointmentId("APT-003")
                .folio("AGS-003")
                .build();
        when(appointmentRepository.getById("APT-003")).thenReturn(model);
        when(appointmentConverter.convert(model)).thenReturn(expectedDto);

        AppointmentDto result = service.getOne("APT-003");

        assertNotNull(result);
        assertEquals("APT-003", result.getAppointmentId());
        assertEquals("AGS-003", result.getFolio());
    }

    // ---------------------------------------------------------------
    // getSingleData – appointmentId vacío
    // ---------------------------------------------------------------
    @Test
    @DisplayName("getSingleData: con appointmentId vacío debe retornar dto con datos del usuario")
    void getSingleDataConIdVacioRetornaDatosDeUsuario() throws Exception {
        UserRegisterModel user = new UserRegisterModel();
        user.setAgency("Mi Agencia");
        when(userRegisterRepository.getcode(1)).thenReturn(user);

        AppointmentDto dtoConAgency = AppointmentDto.builder().agency("Mi Agencia").build();
        when(appointmentConverter.convert(any(AppointmentModel.class))).thenReturn(dtoConAgency);

        AppointmentDto result = service.getSingleData("", 1, "AGS");

        assertNotNull(result);
        assertEquals("Mi Agencia", result.getAgency());
    }

    // ---------------------------------------------------------------
    // getDataTableByDate
    // ---------------------------------------------------------------
    @Test
    @DisplayName("getDataTableByDate: debe retornar lista convertida de appointments")
    void getDataTableByDateRetornaListaConvertida() throws Exception {
        Date start = new Date(System.currentTimeMillis());
        Date end   = new Date(System.currentTimeMillis() + 86_400_000L);

        AppointmentModel m1 = AppointmentModel.builder().appointmentId("APT-A").build();
        AppointmentModel m2 = AppointmentModel.builder().appointmentId("APT-B").build();
        AppointmentDto   d1 = AppointmentDto.builder().appointmentId("APT-A").build();
        AppointmentDto   d2 = AppointmentDto.builder().appointmentId("APT-B").build();

        when(appointmentRepository.getAppointmentsByDate(start, end, "AGS"))
                .thenReturn(Arrays.asList(m1, m2));
        when(appointmentConverter.convert(m1)).thenReturn(d1);
        when(appointmentConverter.convert(m2)).thenReturn(d2);

        List<AppointmentDto> result = service.getDataTableByDate(start, end, "AGS");

        assertEquals(2, result.size());
        assertEquals("APT-A", result.get(0).getAppointmentId());
        assertEquals("APT-B", result.get(1).getAppointmentId());
    }
}
