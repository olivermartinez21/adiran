package com.tmm.myre.containers.service;

import com.tmm.myre.appointments.model.AppointmentModel;
import com.tmm.myre.appointments.repository.IAppointmentRepository;
import com.tmm.myre.assignments.model.AssignmentModel;
import com.tmm.myre.assignments.model.BookingModel;
import com.tmm.myre.assignments.repository.IAssignmentRepository;
import com.tmm.myre.assignments.repository.IBookingRepository;
import com.tmm.myre.base.configuration.secondary.EventActivityRepository;
import com.tmm.myre.base.configuration.secondary.IIntegrationRepository;
import com.tmm.myre.base.dto.ResponseManagement;
import com.tmm.myre.base.exception.ConverterException;
import com.tmm.myre.base.repository.IAppLogRepository;
import com.tmm.myre.base.repository.IUserRoleRepository;
import com.tmm.myre.base.service.core.IAppLogService;
import com.tmm.myre.base.service.core.IFolioService;
import com.tmm.myre.base.service.core.IPdfGenerationService;
import com.tmm.myre.base.utils.KeyConstants;
import com.tmm.myre.catalog.converter.CatShippingCompanyConverter;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.containers.converter.ContainerConverter;
import com.tmm.myre.containers.dto.ContainerDto;
import com.tmm.myre.containers.dto.ResumentInformationDto;
import com.tmm.myre.containers.model.ContainerHistoricModel;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerHistoricRepository;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.inspections.converter.InspectionConverter;
import com.tmm.myre.inspections.converter.InspectionOutConverter;
import com.tmm.myre.inspections.model.InspectionModel;
import com.tmm.myre.inspections.repository.IInspectionOutRepository;
import com.tmm.myre.inspections.repository.IInspectionRepository;
import com.tmm.myre.photo.repository.IPhotoRepository;
import com.tmm.myre.userRegister.model.UserRegisterModel;
import com.tmm.myre.userRegister.repository.IUserRegisterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link ContainerService}.
 *
 * <p>Cada método de prueba verifica de forma aislada un comportamiento específico
 * del servicio, utilizando Mockito para sustituir todas las dependencias externas
 * (repositorios, convertidores, servicios auxiliares, etc.) y así garantizar que
 * la lógica de negocio dentro de {@code ContainerService} funciona correctamente
 * sin necesidad de una base de datos real.</p>
 *
 * <p>Convenciones de nomenclatura:
 * <ul>
 *   <li>{@code <metodo>_<escenario>_<resultadoEsperado>}</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class ContainerServiceTest {

    // ──────────────────────────────────────────────────────────────────────────
    // Mocks de todas las dependencias inyectadas en ContainerService
    // ──────────────────────────────────────────────────────────────────────────

    @Mock private IContainerRepository           containerRepository;
    @Mock private IContainerHistoricRepository   containerHistoricRepository;
    @Mock private ContainerConverter             containerConverter;
    @Mock private InspectionConverter            inspectionConverter;
    @Mock private IInspectionRepository          inspectorRepository;
    @Mock private IAppointmentRepository         appointmentRepository;
    @Mock private IIntegrationRepository         integrationRepository;
    @Mock private IAppLogService                 appLogService;
    @Mock private IUserRegisterRepository        userRegisterRepository;
    @Mock private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;
    @Mock private IPdfGenerationService          pdfGenerationService;
    @Mock private IAssignmentRepository          assignmentRepository;
    @Mock private InspectionOutConverter         inspectionOutConverter;
    @Mock private IInspectionOutRepository       inspectionOutRepository;
    @Mock private EventActivityRepository        eventActivityRepository;
    @Mock private IAppLogRepository              appLogRepository;
    @Mock private IPhotoRepository               photoRepository;
    @Mock private CatShippingCompanyConverter    catShippingCompanyConverter;
    @Mock private IBookingRepository             bookingRepository;
    @Mock private IFolioService                  folioService;

    /** Instancia real del servicio con las dependencias inyectadas por Mockito. */
    @InjectMocks
    private ContainerService containerService;

    // ══════════════════════════════════════════════════════════════════════════
    // getContainers
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>getContainers – lista vacía</b>
     * <p>Verifica que cuando el repositorio no devuelve contenedores para el
     * almacén indicado, el servicio retorna una lista vacía sin lanzar ninguna
     * excepción.</p>
     */
    @Test
    @DisplayName("getContainers: repositorio vacío → lista vacía")
    void getContainers_repositorioVacio_retornaListaVacia() throws ConverterException {
        when(containerRepository.findAllbyWarehouse("AGUASCALIENTES")).thenReturn(Collections.emptyList());

        List<ContainerDto> result = containerService.getContainers("AGUASCALIENTES");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * <b>getContainers – contenedor con fechaGateIn nula</b>
     * <p>Cuando un {@link ContainerModel} tiene {@code dateGateIn} en {@code null},
     * el servicio no debe intentar calcular días de estancia y debe continuar
     * convirtiendo el modelo a DTO correctamente.</p>
     */
    @Test
    @DisplayName("getContainers: contenedor con dateGateIn null no lanza excepción")
    void getContainers_dateGateInNula_noCalculaDias() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-001")
                .container("ABCD1234567")
                .dateGateIn(null)
                .build();
        ContainerDto dto = ContainerDto.builder().containerId("C-001").build();

        when(containerRepository.findAllbyWarehouse("AGS")).thenReturn(List.of(model));
        when(containerConverter.convert(model)).thenReturn(dto);

        List<ContainerDto> result = containerService.getContainers("AGS");

        assertEquals(1, result.size());
        assertEquals("C-001", result.get(0).getContainerId());
    }

    /**
     * <b>getContainers – contenedor con fechaGateIn válida</b>
     * <p>Cuando un contenedor tiene una fecha de entrada definida, el servicio
     * calcula los días de estancia (≥ 0) y lo asigna al modelo antes de
     * convertirlo. Este test comprueba que el cálculo no produce valores
     * negativos.</p>
     */
    @Test
    @DisplayName("getContainers: contenedor con dateGateIn válida → daysStay ≥ 0")
    void getContainers_dateGateInValida_daysStayNoNegativo() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-002")
                .container("EFGH9876543")
                .dateGateIn(LocalDateTime.now().minusDays(5))
                .build();
        ContainerDto dto = ContainerDto.builder().containerId("C-002").build();

        when(containerRepository.findAllbyWarehouse("AGUASCALIENTES")).thenReturn(List.of(model));
        when(containerConverter.convert(model)).thenReturn(dto);

        List<ContainerDto> result = containerService.getContainers("AGUASCALIENTES");

        assertEquals(1, result.size());
        // Verificamos que se haya calculado y asignado daysStay (debe ser "5" o "4"
        // dependiendo de milisegundos exactos, pero nunca un número negativo)
        assertNotNull(model.getDaysStay());
        assertFalse(model.getDaysStay().startsWith("-"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // findAll
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>findAll – conversión de todos los contenedores</b>
     * <p>Verifica que el servicio delega en el repositorio para obtener todos
     * los contenedores y los convierte al DTO correspondiente, devolviendo
     * la misma cantidad de elementos.</p>
     */
    @Test
    @DisplayName("findAll: convierte todos los contenedores del repositorio")
    void findAll_repositorioConDatos_retornaListaConvertida() throws ConverterException {
        ContainerModel m1 = ContainerModel.builder().containerId("C-001").build();
        ContainerModel m2 = ContainerModel.builder().containerId("C-002").build();
        ContainerDto   d1 = ContainerDto.builder().containerId("C-001").build();
        ContainerDto   d2 = ContainerDto.builder().containerId("C-002").build();

        when(containerRepository.findAll()).thenReturn(Arrays.asList(m1, m2));
        when(containerConverter.convert(m1)).thenReturn(d1);
        when(containerConverter.convert(m2)).thenReturn(d2);

        List<ContainerDto> result = containerService.findAll();

        assertEquals(2, result.size());
        verify(containerConverter, times(2)).convert(any(ContainerModel.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // getSingleData / getOne
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>getSingleData – recupera y convierte un contenedor por ID</b>
     * <p>Comprueba que el servicio busca el contenedor por su identificador
     * y lo convierte a {@link ContainerDto}.</p>
     */
    @Test
    @DisplayName("getSingleData: recupera y convierte el contenedor indicado")
    void getSingleData_idExistente_retornaDto() throws ConverterException {
        ContainerModel model = ContainerModel.builder().containerId("C-001").build();
        ContainerDto   dto   = ContainerDto.builder().containerId("C-001").build();

        when(containerRepository.getById("C-001")).thenReturn(model);
        when(containerConverter.convert(model)).thenReturn(dto);

        ContainerDto result = containerService.getSingleData("C-001");

        assertNotNull(result);
        assertEquals("C-001", result.getContainerId());
    }

    /**
     * <b>getOne – equivalente a getSingleData</b>
     * <p>Verifica que {@code getOne} también obtiene y convierte el contenedor
     * por su ID, comportándose de forma idéntica a {@code getSingleData}.</p>
     */
    @Test
    @DisplayName("getOne: recupera y convierte el contenedor indicado")
    void getOne_idExistente_retornaDto() throws ConverterException {
        ContainerModel model = ContainerModel.builder().containerId("C-003").build();
        ContainerDto   dto   = ContainerDto.builder().containerId("C-003").build();

        when(containerRepository.getById("C-003")).thenReturn(model);
        when(containerConverter.convert(model)).thenReturn(dto);

        ContainerDto result = containerService.getOne("C-003");

        assertNotNull(result);
        assertEquals("C-003", result.getContainerId());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // containerValidation
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>containerValidation – sin contenedores pendientes de visto bueno y
     * con cita de tipo ENTRADA</b>
     * <p>Cuando todos los contenedores ya tienen visto bueno ({@code count == 0})
     * y la cita es de tipo ENTRADA (eventType == 1), el estatus de la cita debe
     * cambiar a 7 (LIBERADO).</p>
     */
    @Test
    @DisplayName("containerValidation: visto bueno completo en cita ENTRADA → status cita = 7")
    void containerValidation_vistoBuenoCompletoEntrada_actualizaStatusCitaA7() throws ConverterException {
        ContainerModel containerModel = ContainerModel.builder()
                .containerId("C-001")
                .appointmentId("APT-001")
                .status(3)
                .build();
        AppointmentModel appointment = new AppointmentModel();
        appointment.setEventType(1);  // 1 = ENTRADA

        when(containerRepository.getById("C-001")).thenReturn(containerModel);
        when(containerRepository.countContainersByVistoBueno("C-001")).thenReturn(0);
        when(appointmentRepository.getById("APT-001")).thenReturn(appointment);

        ResponseManagement response = containerService.containerValidation("C-001", "DISPONIBLE", "1");

        assertTrue(response.getSuccess());
        assertEquals(7, appointment.getStatus());
        verify(containerRepository).save(containerModel);
        verify(appointmentRepository).save(appointment);
    }

    /**
     * <b>containerValidation – sin contenedores pendientes de visto bueno y
     * con cita de tipo SALIDA</b>
     * <p>Si la cita es de tipo SALIDA (eventType != 1), el estatus de la cita
     * debe cambiar a 9 (OPERACIÓN TERMINADA).</p>
     */
    @Test
    @DisplayName("containerValidation: visto bueno completo en cita SALIDA → status cita = 9")
    void containerValidation_vistoBuenoCompletoSalida_actualizaStatusCitaA9() throws ConverterException {
        ContainerModel containerModel = ContainerModel.builder()
                .containerId("C-002")
                .appointmentId("APT-002")
                .status(3)
                .build();
        AppointmentModel appointment = new AppointmentModel();
        appointment.setEventType(2);  // 2 = SALIDA

        when(containerRepository.getById("C-002")).thenReturn(containerModel);
        when(containerRepository.countContainersByVistoBueno("C-002")).thenReturn(0);
        when(appointmentRepository.getById("APT-002")).thenReturn(appointment);

        ResponseManagement response = containerService.containerValidation("C-002", "DAÑADO", "2");

        assertTrue(response.getSuccess());
        assertEquals(9, appointment.getStatus());
    }

    /**
     * <b>containerValidation – contenedores pendientes de visto bueno</b>
     * <p>Si aún quedan otros contenedores sin visto bueno ({@code count > 0}),
     * la cita NO debe actualizarse. El servicio sigue siendo exitoso pero no
     * toca la cita.</p>
     */
    @Test
    @DisplayName("containerValidation: quedan contenedores pendientes → no modifica la cita")
    void containerValidation_pendientesMasContenedores_noActualizaCita() throws ConverterException {
        ContainerModel containerModel = ContainerModel.builder()
                .containerId("C-003")
                .appointmentId("APT-003")
                .status(3)
                .build();

        when(containerRepository.getById("C-003")).thenReturn(containerModel);
        when(containerRepository.countContainersByVistoBueno("C-003")).thenReturn(2);

        ResponseManagement response = containerService.containerValidation("C-003", "1", "1");

        assertTrue(response.getSuccess());
        verify(appointmentRepository, never()).getById(anyString());
        verify(appointmentRepository, never()).save(any());
    }

    /**
     * <b>containerValidation – excepción en repositorio</b>
     * <p>Cuando el repositorio lanza una excepción inesperada, el servicio
     * captura el error y devuelve {@code success = false} junto al código de
     * error de servicio, sin propagar la excepción al llamador.</p>
     */
    @Test
    @DisplayName("containerValidation: excepción en repositorio → success=false con código error")
    void containerValidation_excepcionRepositorio_retornaErrorControlado() throws ConverterException {
        when(containerRepository.getById("C-ERR")).thenThrow(new RuntimeException("DB error"));

        ResponseManagement response = containerService.containerValidation("C-ERR", "1", "1");

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
        assertTrue(response.getMessage().contains(KeyConstants.SERVICE_ERROR));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // saveContainer
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>saveContainer – contenedor nuevo (no existe en BD)</b>
     * <p>Cuando el número de contenedor no existe en base de datos, el servicio
     * debe guardarlo y retornar {@code success = true}.</p>
     */
    @Test
    @DisplayName("saveContainer: contenedor nuevo → guarda y retorna success=true")
    void saveContainer_contenedorNuevo_guardaYRetornaExito() throws Exception {
        ContainerDto dto = ContainerDto.builder()
                .container("ABCD1234567")
                .containerType(1)
                .containerSize("40HC")
                .shippingCompany("MAERSK")
                //.idUser(1)
                .build();

        when(containerRepository.serarchBd("ABCD1234567")).thenReturn(0);
        doAnswer(inv -> new ContainerModel())
                .when(containerConverter).convert(any(ContainerDto.class));

        ResponseManagement response = containerService.saveContainer(dto, "AGS");

        assertTrue(response.getSuccess());
        verify(containerRepository).save(any(ContainerModel.class));
    }

    /**
     * <b>saveContainer – contenedor ya en inventario activo (status 1–6)</b>
     * <p>Si el contenedor ya tiene un proceso activo (estatus entre 1 y 6), el
     * servicio no debe guardarlo de nuevo y debe incluir un mensaje de
     * advertencia indicando que la unidad ya está en el inventario.</p>
     */
    @Test
    @DisplayName("saveContainer: contenedor activo (status 1-6) → mensaje de unidad duplicada")
    void saveContainer_contenedorEnInventarioActivo_retornaMensajeAdvertencia() {
        ContainerDto dto = ContainerDto.builder()
                .container("AAAA1111111")
                .containerType(1)
                .containerSize("20ST")
                .shippingCompany("MSC")
                //.idUser(1)
                .build();

        when(containerRepository.serarchBd("AAAA1111111")).thenReturn(1);
        when(containerRepository.findBycontainerStatus("AAAA1111111")).thenReturn(3);

        ResponseManagement response = containerService.saveContainer(dto, "AGS");

        assertNotNull(response.getMessage());
        assertTrue(response.getMessage().contains("AAAA1111111"));
    }

    /**
     * <b>saveContainer – excepción en el repositorio de búsqueda</b>
     * <p>Si el repositorio lanza una excepción al verificar la existencia del
     * contenedor, el servicio la captura y devuelve {@code success = false}
     * con el código de error de servicio.</p>
     */
    @Test
    @DisplayName("saveContainer: excepción en repositorio → success=false con código error")
    void saveContainer_excepcionConverter_retornaErrorControlado() {
        ContainerDto dto = ContainerDto.builder()
                .container("ERRR0000000")
                .containerType(1)
                .build();

        when(containerRepository.serarchBd("ERRR0000000"))
                .thenThrow(new RuntimeException("DB error"));

        ResponseManagement response = containerService.saveContainer(dto, "AGS");

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // savePregate
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>savePregate – contenedor VACÍO sin restricciones</b>
     * <p>Un contenedor con condición VACÍO puede ser pré-registrado sin
     * necesidad de nomenclatura. El servicio debe actualizar el estado a 2 y
     * retornar {@code success = true}.</p>
     */
    @Test
    @DisplayName("savePregate: condición VACIO → guarda sin requerir nomenclatura")
    void savePregate_condicionVacio_guardaCorrectamente() throws ConverterException {
        ContainerDto dto = ContainerDto.builder()
                .containerId("C-010")
                .conditionPregate("VACIO")
                .typeServicePregate("CARRIER")
                .billTo("CLIENTE-A")
                .num(2)
                .build();

        ContainerModel model = ContainerModel.builder()
                .containerId("C-010")
                .build();

        when(containerRepository.getById("C-010")).thenReturn(model);

        ResponseManagement response = containerService.savePregate(dto);

        assertTrue(response.getSuccess());
        assertEquals(2, model.getStatus());
        verify(containerRepository).save(model);
    }

    /**
     * <b>savePregate – contenedor LLENO sin nomenclatura</b>
     * <p>Cuando la condición es LLENO pero el campo nomenclatura está vacío o
     * nulo, el servicio debe rechazar la operación y retornar un mensaje de
     * error indicando que la nomenclatura es requerida.</p>
     */
    @Test
    @DisplayName("savePregate: condición LLENO sin nomenclatura → error de validación")
    void savePregate_condicionLlenoSinNomenclatura_retornaMensajeError() throws ConverterException {
        ContainerDto dto = ContainerDto.builder()
                .containerId("C-011")
                .conditionPregate("LLENO")
                .nomenclatura(null)
                .build();

        ResponseManagement response = containerService.savePregate(dto);

        assertFalse(Boolean.TRUE.equals(response.getSuccess()));
        assertNotNull(response.getMessage());
        assertTrue(response.getMessage().toLowerCase().contains("nomenclatura"));
        verify(containerRepository, never()).getById(anyString());
    }

    /**
     * <b>savePregate – contenedor LLENO con nomenclatura válida</b>
     * <p>Cuando se registra un contenedor LLENO con su nomenclatura, el servicio
     * debe fijar el estatus en 3, asignar la fecha de inspección y retornar
     * {@code success = true}.</p>
     */
    @Test
    @DisplayName("savePregate: condición LLENO con nomenclatura → status=3 y success=true")
    void savePregate_condicionLlenoConNomenclatura_guardaConStatus3() throws ConverterException {
        ContainerDto dto = ContainerDto.builder()
                .containerId("C-012")
                .conditionPregate("LLENO")
                .nomenclatura("TIPO-X")
                .typeServicePregate("CARRIER")
                .num(2)
                .build();

        ContainerModel model = ContainerModel.builder()
                .containerId("C-012")
                .build();

        when(containerRepository.getById("C-012")).thenReturn(model);

        ResponseManagement response = containerService.savePregate(dto);

        assertTrue(response.getSuccess());
        assertEquals(3, model.getStatus());
        assertNotNull(model.getDateInspection());
        assertEquals("TIPO-X", model.getNomenclatura());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // getContainerInformation
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>getContainerInformation – naviera correcta</b>
     * <p>Cuando el contenedor pertenece a la naviera solicitada, el servicio
     * debe retornar el DTO correspondiente.</p>
     */
    @Test
    @DisplayName("getContainerInformation: naviera coincide → retorna DTO")
    void getContainerInformation_navieraCorrecta_retornaDto() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-020")
                .shippingCompany("SHP-001")
                .build();
        ContainerDto dto = ContainerDto.builder().containerId("C-020").build();

        when(containerRepository.getContainerInformation("C-020")).thenReturn(model);
        when(containerConverter.convert(model)).thenReturn(dto);

        ContainerDto result = containerService.getContainerInformation("C-020", "SHP-001");

        assertNotNull(result);
        assertEquals("C-020", result.getContainerId());
    }

    /**
     * <b>getContainerInformation – naviera incorrecta</b>
     * <p>Cuando el contenedor NO pertenece a la naviera enviada en el parámetro,
     * el servicio debe lanzar una {@link ResponseStatusException} con estado
     * 400 BAD_REQUEST.</p>
     */
    @Test
    @DisplayName("getContainerInformation: naviera no coincide → lanza ResponseStatusException 400")
    void getContainerInformation_navieraIncorrecta_lanzaExcepcion400() {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-021")
                .shippingCompany("SHP-001")
                .build();

        when(containerRepository.getContainerInformation("C-021")).thenReturn(model);

        assertThrows(ResponseStatusException.class,
                () -> containerService.getContainerInformation("C-021", "OTRA-NAVIERA"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // saveInformationRepair
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>saveInformationRepair – datos de reparación actualizados</b>
     * <p>Verifica que el servicio asigna correctamente los campos de reparación
     * (técnico asignado, fechas de inicio y fin) al contenedor y guarda los
     * cambios en el repositorio.</p>
     */
    @Test
    @DisplayName("saveInformationRepair: actualiza datos de reparación y retorna success=true")
    void saveInformationRepair_datosValidos_actualizaYGuarda() throws ConverterException {
        ContainerDto dto = ContainerDto.builder()
                .containerId("C-030")
                .assignedTo("TECNICO-01")
                .startDate("2026-03-01")
                .finalDate("2026-03-10")
                .build();

        ContainerModel model = ContainerModel.builder()
                .containerId("C-030")
                .build();

        when(containerRepository.getById("C-030")).thenReturn(model);

        ResponseManagement response = containerService.saveInformationRepair(dto);

        assertTrue(response.getSuccess());
        assertEquals("TECNICO-01", model.getAssignedTo());
        assertEquals("2026-03-01", model.getStartDate());
        assertEquals("2026-03-10", model.getFinalDate());
        verify(containerRepository).save(model);
    }

    /**
     * <b>saveInformationRepair – excepción al buscar el contenedor</b>
     * <p>Si el repositorio lanza una excepción al recuperar el contenedor,
     * el servicio debe capturarla y devolver {@code success = false}.</p>
     */
    @Test
    @DisplayName("saveInformationRepair: excepción en repositorio → success=false")
    void saveInformationRepair_excepcionRepositorio_retornaErrorControlado() throws ConverterException {
        ContainerDto dto = ContainerDto.builder().containerId("C-ERR").build();
        when(containerRepository.getById("C-ERR")).thenThrow(new RuntimeException("DB error"));

        ResponseManagement response = containerService.saveInformationRepair(dto);

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // changeStatusApproved
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>changeStatusApproved – aprobación de cotización (status 3)</b>
     * <p>Cuando se aprueba una cotización (status == 3), el servicio debe
     * asignar la fecha de aprobación, regenerar el PDF de la cotización y
     * guardar el contenedor y sus inspecciones.</p>
     */
    @Test
    @DisplayName("changeStatusApproved: status=3 → fecha de aprobación, PDF regenerado, success=true")
    void changeStatusApproved_status3_apruebaCotizacion() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-040")
                .quoteName("ESTIMADO AGS 1")
                .build();
        InspectionModel insp = new InspectionModel();
        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(containerRepository.getById("C-040")).thenReturn(model);
        when(inspectorRepository.getAllInspectionsByContainerIdStatus("C-040"))
                .thenReturn(List.of(insp));
        when(pdfGenerationService.pdfQuote(eq("C-040"), anyList())).thenReturn(pdfBytes);

        ResponseManagement response = containerService.changeStatusApproved("C-040", "Todo correcto", 3);

        assertTrue(response.getSuccess());
        assertNotNull(model.getAprovedQuote());
        assertArrayEquals(pdfBytes, model.getQuote());
        verify(inspectorRepository).saveAll(anyList());
        verify(containerRepository).save(model);
    }

    /**
     * <b>changeStatusApproved – rechazo de cotización (status != 3)</b>
     * <p>Cuando se rechaza una cotización (status distinto de 3), el servicio
     * solo debe actualizar el estado y el comentario del contenedor, sin
     * tocar la fecha de aprobación ni las inspecciones.</p>
     */
    @Test
    @DisplayName("changeStatusApproved: status≠3 → solo actualiza statusQute y comentario")
    void changeStatusApproved_statusDistinto3_soloActualizaStatusYComentario() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-041")
                .build();

        when(containerRepository.getById("C-041")).thenReturn(model);

        ResponseManagement response = containerService.changeStatusApproved("C-041", "Rechazado", 4);

        assertTrue(response.getSuccess());
        assertNull(model.getAprovedQuote());
        assertEquals(4, model.getStatusQute());
        assertEquals("Rechazado", model.getComents());
        verify(inspectorRepository, never()).getAllInspectionsByContainerIdStatus(anyString());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // saveExitDate
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>saveExitDate – actualización correcta de fecha de salida</b>
     * <p>Verifica que el servicio actualiza la fecha de salida del contenedor,
     * los comentarios, el origen, el transportista y el destino; además fija
     * el estatus en 6 y retorna {@code success = true}.</p>
     */
    @Test
    @DisplayName("saveExitDate: datos válidos → actualiza campos y retorna success=true")
    void saveExitDate_datosValidos_actualizaYGuarda() {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-050")
                .build();
        LocalDateTime exitDate = LocalDateTime.of(2026, 3, 10, 14, 0);

        when(containerRepository.findById("C-050")).thenReturn(Optional.of(model));

        ResponseManagement response = containerService.saveExitDate(
                "C-050", exitDate, "Observación", "GUADALAJARA", "MONTERREY", "TRANS-01");

        assertTrue(response.getSuccess());
        assertEquals(exitDate,      model.getExitDateTime());
        assertEquals("Observación", model.getComents());
        assertEquals("MONTERREY",   model.getOriginPregate());
        assertEquals("TRANS-01",    model.getTransportId());
        assertEquals("GUADALAJARA", model.getDestinyPregate());
        assertEquals(6,             model.getStatus());
        verify(containerRepository).save(model);
    }

    /**
     * <b>saveExitDate – excepción al acceder al repositorio</b>
     * <p>Si el repositorio lanza una excepción al buscar el contenedor,
     * el servicio debe capturarla y retornar {@code success = false}.</p>
     */
    @Test
    @DisplayName("saveExitDate: excepción en repositorio → success=false con código error")
    void saveExitDate_excepcionRepositorio_retornaErrorControlado() {
        when(containerRepository.findById("C-ERR"))
                .thenThrow(new RuntimeException("DB error"));

        ResponseManagement response = containerService.saveExitDate(
                "C-ERR", LocalDateTime.now(), "obs", "dest", "orig", "trans");

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // evacuationUpdate
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>evacuationUpdate – actualización de datos de evacuación</b>
     * <p>Verifica que el servicio fija el estatus del contenedor en 5
     * (EVACUACIÓN) y actualiza los campos relacionados con el proceso de
     * evacuación retornando {@code success = true}.</p>
     */
    @Test
    @DisplayName("evacuationUpdate: datos válidos → status=5 y success=true")
    void evacuationUpdate_datosValidos_actualizaYGuarda() {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-060")
                .build();
        LocalDateTime exitDate = LocalDateTime.of(2026, 3, 15, 9, 0);
        ContainerDto dto = ContainerDto.builder()
                .containerId("C-060")
                .exitDateTime(exitDate)
                .billTo("CLIENTE-B")
                .definition("EVACUACION")
                .transportId("TRANS-02")
                .economicNumber("ECO-999")
                .build();

        when(containerRepository.findById("C-060")).thenReturn(Optional.of(model));

        ResponseManagement response = containerService.evacuationUpdate(dto);

        assertTrue(response.getSuccess());
        assertEquals(5,            model.getStatus());
        assertEquals(exitDate,     model.getExitDateTime());
        assertEquals("CLIENTE-B",  model.getBillTo());
        assertEquals("EVACUACION", model.getDefinition());
        assertEquals("TRANS-02",   model.getTransportId());
        assertEquals("ECO-999",    model.getEconomicNumber());
        verify(containerRepository).save(model);
    }

    /**
     * <b>evacuationUpdate – excepción en repositorio</b>
     * <p>Cuando el repositorio falla, el servicio captura el error y retorna
     * {@code success = false} con el código de error de servicio.</p>
     */
    @Test
    @DisplayName("evacuationUpdate: excepción en repositorio → success=false con código error")
    void evacuationUpdate_excepcionRepositorio_retornaErrorControlado() {
        ContainerDto dto = ContainerDto.builder().containerId("C-ERR").build();
        when(containerRepository.findById("C-ERR"))
                .thenThrow(new RuntimeException("DB error"));

        ResponseManagement response = containerService.evacuationUpdate(dto);

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // saveInvoiceNumber
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>saveInvoiceNumber – contenedor encontrado</b>
     * <p>Verifica que cuando el contenedor existe, el número de factura se
     * asigna correctamente y el repositorio guarda el cambio.</p>
     */
    @Test
    @DisplayName("saveInvoiceNumber: contenedor encontrado → asigna número y guarda")
    void saveInvoiceNumber_contenedorEncontrado_asignaNumeroFactura() {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-070")
                .build();

        when(containerRepository.findById("C-070")).thenReturn(Optional.of(model));

        containerService.saveInvoiceNumber("C-070", "INV-2026-001");

        assertEquals("INV-2026-001", model.getNoInvoice());
        verify(containerRepository).save(model);
    }

    /**
     * <b>saveInvoiceNumber – contenedor no encontrado</b>
     * <p>Cuando el repositorio no encuentra el contenedor (retorna
     * {@code Optional.empty()}), el servicio no debe intentar asignar la
     * factura ni lanzar ninguna excepción.</p>
     */
    @Test
    @DisplayName("saveInvoiceNumber: contenedor no encontrado → no guarda ni lanza excepción")
    void saveInvoiceNumber_contenedorNoEncontrado_noGuarda() {
        when(containerRepository.findById("C-NOPE")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> containerService.saveInvoiceNumber("C-NOPE", "INV-XXX"));
        verify(containerRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // getContainersbyShippingCompany
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>getContainersbyShippingCompany – retorna contenedores de la naviera</b>
     * <p>Verifica que el servicio recupera el código de usuario, obtiene la
     * naviera asociada y devuelve los contenedores convertidos a DTO.</p>
     */
    @Test
    @DisplayName("getContainersbyShippingCompany: usuario con naviera → retorna lista de DTOs")
    void getContainersbyShippingCompany_usuarioConNaviera_retornaListaDtos() throws ConverterException {
        UserRegisterModel user = new UserRegisterModel();
        user.setCode("COD-001");
        CatShippingCompanyModel company = CatShippingCompanyModel.builder()
                .shippingCompanyId("SHP-001")
                .build();
        ContainerModel model = ContainerModel.builder().containerId("C-080").build();
        ContainerDto   dto   = ContainerDto.builder().containerId("C-080").build();

        when(userRegisterRepository.getcode(1)).thenReturn(user);
        when(catShippingCompanyReposirtory.getidclient("COD-001")).thenReturn(company);
        when(containerRepository.findAllByClient("SHP-001")).thenReturn(List.of(model));
        when(containerConverter.convert(model)).thenReturn(dto);

        List<ContainerDto> result = containerService.getContainersbyShippingCompany(1);

        assertEquals(1, result.size());
        assertEquals("C-080", result.get(0).getContainerId());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // gateOutEvent
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * <b>gateOutEvent – salida sin transmisión a integración (transmit != 2)</b>
     * <p>Verifica que cuando el campo {@code transmit} del DTO es distinto de 2,
     * el servicio registra el histórico, actualiza el estatus a 7, borra la
     * asignación y elimina el contenedor del inventario activo.</p>
     */
    @Test
    @DisplayName("gateOutEvent: transmit≠2 → registra histórico, status=7, elimina contenedor")
    void gateOutEvent_sinTransmision_registraHistoricoYEliminaContenedor() throws ConverterException {
        ContainerModel model = ContainerModel.builder()
                .containerId("C-090")
                .container("TTTT9999999")
                .conditionPregate("VACIO")
                .location("AGS")
                .containerType(1)
                .contaierSize("40HC")
                .shippingCompany("SHP-001")
                .operatorName("JOSE")
                .economicNumber("ECO-001")
                .condition("1")
                .build();

        ContainerDto dto = ContainerDto.builder()
                .containerId("C-090")
                .booking("BKG-001")
                .transmit(1)
                .newEventDate(LocalDateTime.now())
                .build();

        when(containerRepository.getById("C-090")).thenReturn(model);

        ResponseManagement response = containerService.gateOutEvent(dto);

        assertTrue(response.getSuccess());
        assertEquals(7, model.getStatus());
        verify(containerHistoricRepository).save(any(ContainerHistoricModel.class));
        verify(assignmentRepository).deleteUnit("TTTT9999999");
        verify(containerRepository).deleteById("C-090");
    }

    /**
     * <b>gateOutEvent – excepción en repositorio</b>
     * <p>Cuando el repositorio lanza un error inesperado durante la salida,
     * el servicio captura la excepción y devuelve {@code success = false}.</p>
     */
    @Test
    @DisplayName("gateOutEvent: excepción en repositorio → success=false con código error")
    void gateOutEvent_excepcionRepositorio_retornaErrorControlado() throws ConverterException {
        ContainerDto dto = ContainerDto.builder().containerId("C-ERR").build();
        when(containerRepository.getById("C-ERR")).thenThrow(new RuntimeException("DB error"));

        ResponseManagement response = containerService.gateOutEvent(dto);

        assertFalse(response.getSuccess());
        assertEquals(KeyConstants.SERVICE_ERROR_CODE, response.getErrorCode());
    }
}







