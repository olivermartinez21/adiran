package com.tmm.myre.base.service;

import com.tmm.myre.base.model.ContainerInventoryHistoric;
import com.tmm.myre.base.repository.IContainerInventoryHistoricRepository;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.catalog.model.CatNomenclaturaModel;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatNomenclaturaRepository;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.repository.IContainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ScheduledTasks {

    @Autowired
    private IContainerRepository containerRepository;

    @Autowired
    private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;

    @Autowired
    private ICatNomenclaturaRepository catNomencalturaRepository;

    @Autowired
    private IContainerInventoryHistoricRepository containerInventoryHistoricRepository;

    //@Scheduled(cron = "*/15 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    public void saveInventory() {

        List<ContainerModel> containers = containerRepository.getAllAndStatus();
        for (ContainerModel record : containers) {
            log.info("Registro a guardar: " + record.toString());
            ContainerInventoryHistoric containerEntity = new ContainerInventoryHistoric();

            // Propietario
            List<CatShippingCompanyModel> shippingCompany = catShippingCompanyReposirtory.getShippingDesc(record.getShippingCompany());
            for (CatShippingCompanyModel catShippingCompanyEntity : shippingCompany) {
                containerEntity.setShippingCompanyDescription(catShippingCompanyEntity.getDescription());
            }

            // Unidad
            containerEntity.setContainer(record.getContainer());

            // Estado de la unidad
            switch (record.getCondition()) {
                case "1":
                    containerEntity.setCondition("DISPONIBLE");
                    break;
                case "2":
                    containerEntity.setCondition("DAÑADO");
                    break;
                case "4":
                    containerEntity.setCondition("EVACUACION");
                    break;
                case "6":
                    containerEntity.setCondition("BLOQUEADO/GX");
                    break;
                case "7":
                    containerEntity.setCondition("TOTAL LOSS");
                    break;
                case "8":
                    containerEntity.setCondition("VENTA");
                    break;
                case "9":
                    containerEntity.setCondition("ACCIDENTADO");
                    break;
                default:
                    containerEntity.setCondition("UNKNOWN");
                    break;
            }

            // Tipo de unidad
            switch (record.getContainerType()) {
                case 1:
                    containerEntity.setContainerType("CH");
                    break;
                case 2:
                    containerEntity.setContainerType("OT");
                    break;
                case 3:
                    containerEntity.setContainerType("DC");
                    break;
                case 4:
                    containerEntity.setContainerType("GS");
                    break;
                case 5:
                    containerEntity.setContainerType("IMO");
                    break;
                case 6:
                    containerEntity.setContainerType("RF");
                    break;
                case 7:
                    containerEntity.setContainerType("HC");
                    break;
                default:
                    containerEntity.setContainerType("UNKNOWN");
                    break;
            }

            // Tamaño
            List<CatNomenclaturaModel> nomenclatura = catNomencalturaRepository.findBYTransportType(containerEntity.getContainerType(), record.getContaierSize());
            for (CatNomenclaturaModel catNomenclaturaEntity : nomenclatura) {
                containerEntity.setNomenclatura(catNomenclaturaEntity.getNomenclatura());
            }

            // Condición de la unidad
            containerEntity.setConditionPregate(record.getConditionPregate());

            // Localidad
            containerEntity.setLocation(record.getLocation());

            // Fecha Gatein
            containerEntity.setDateInspection(record.getDateInspection());

            // Grado-Calidad
            switch (record.getClasification()) {
                case "1":
                    containerEntity.setClasification("A");
                    break;
                case "2":
                    containerEntity.setClasification("B");
                    break;
                case "3":
                    containerEntity.setClasification("C");
                    break;
                case "4":
                    containerEntity.setClasification("BL");
                    break;
                case "5":
                    containerEntity.setClasification("D");
                    break;
                case "6":
                    containerEntity.setClasification("FS");
                    break;
                case "7":
                    containerEntity.setClasification("FX");
                    break;
                default:
                    containerEntity.setClasification("UNKNOWN");
                    break;
            }

            // Días de estadía
            long diff = DateManagement.todayDate().getTime() - record.getDateInspection().getTime();
            long difference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            containerEntity.setDaysOfStay(difference);

            // Fecha fin de reparación
            containerEntity.setFinalDate((record.getFinalDate() == null) ? "PENDIENTE" : record.getFinalDate());

            // Estado estimado naviera
            switch (record.getStatusQute()) {
                case 1:
                    containerEntity.setStatusQuote("Por crear");
                    break;
                case 2:
                    containerEntity.setStatusQuote("Creado");
                    break;
                case 3:
                    containerEntity.setStatusQuote("Aprobado");
                    break;
                case 4:
                    containerEntity.setStatusQuote("Reparación Confirmada");
                    break;
                case 5:
                    containerEntity.setStatusQuote("Actualizar");
                    break;
                case 6:
                    containerEntity.setStatusQuote("Cancelado");
                    break;
                case 7:
                    containerEntity.setStatusQuote("Actualizado");
                    break;
                case 8:
                    containerEntity.setStatusQuote("Rechazado");
                    break;
                case 9:
                    containerEntity.setStatusQuote("Cerrado");
                    break;
                default:
                    containerEntity.setStatusQuote("Pendiente");
                    break;
            }

            // Apto para
            containerEntity.setAptTo(record.getAptTo());

            // Maquinaria de unidad (RF)
            containerEntity.setTypeServicePregate(record.getTypeServicePregate());

            // Fecha de llegada
            containerEntity.setRegisterDate(record.getRegisterDate());

            // Observaciones
            containerEntity.setComments(record.getComents());

            containerEntity.setUploadDate(DateManagement.todayDate());

            // Guardar en la base de datos
            containerInventoryHistoricRepository.save(containerEntity);
            log.info("Registro guardado: " + containerEntity.getContainer());
        }




    }
}
