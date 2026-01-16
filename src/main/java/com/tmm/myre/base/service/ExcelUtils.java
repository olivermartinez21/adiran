package com.tmm.myre.base.service;

import com.tmm.myre.base.configuration.secondary.IMandREmptyContainerReleaseRepository;
import com.tmm.myre.base.configuration.secondary.MandREmptyContainerReleaseModel;
import com.tmm.myre.base.service.core.IExcelUtils;
import com.tmm.myre.base.specifications.MandREmptyContainerReleaseSpecification;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.catalog.model.CatNomenclaturaModel;
import com.tmm.myre.catalog.model.CatShippingCompanyModel;
import com.tmm.myre.catalog.repository.ICatNomenclaturaRepository;
import com.tmm.myre.catalog.repository.ICatShippingCompanyReposirtory;
import com.tmm.myre.containers.dto.ReportFilterDto;
import com.tmm.myre.containers.dto.ResumentInformationDto;
import com.tmm.myre.containers.model.ContainerHistoricModel;
import com.tmm.myre.containers.model.ContainerModel;
import com.tmm.myre.containers.model.ReporteManiobraModel;
import com.tmm.myre.containers.repository.IContainerHistoricRepository;
import com.tmm.myre.containers.repository.IContainerRepository;
import com.tmm.myre.containers.repository.IReporteManiobraRepository;
import com.tmm.myre.containers.specifications.ContainerSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("excleUtils")
public class ExcelUtils  implements IExcelUtils {

    @Autowired
    private IContainerRepository containerRepository;

    @Autowired
    private IContainerHistoricRepository containerHitoricRepository;

    @Autowired
    private ICatShippingCompanyReposirtory catShippingCompanyReposirtory;

    @Autowired
    private ICatNomenclaturaRepository catNomencalturaRepository;

    @Autowired
    private IMandREmptyContainerReleaseRepository mandREmptyContainerReleaseRepository;
    @Autowired
    private IReporteManiobraRepository reporteManiobraRepository;

    @Override
    public byte[] inventoryExcel(ReportFilterDto reportFilterDto) {
        Specification<ContainerModel> specification = ContainerSpecification.byFilterShipping(reportFilterDto);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("INVENTARIO");

            // Estilo general
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            XSSFColor lilac = new XSSFColor(new java.awt.Color(200, 160, 255));
            ((XSSFCellStyle) style).setTopBorderColor(lilac);
            ((XSSFCellStyle) style).setBottomBorderColor(lilac);
            ((XSSFCellStyle) style).setLeftBorderColor(lilac);
            ((XSSFCellStyle) style).setRightBorderColor(lilac);

            // Estilo encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(style);
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Estilo título
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Estilo fecha
            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateStyle.setFont(dateFont);
            dateStyle.setAlignment(HorizontalAlignment.CENTER);
            dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Fila 0: Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte de Inventario");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));

            // Obtener el nombre del propietario si se ha especificado un ID

            // Fila 1: Fecha
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            dateCell.setCellValue("Fecha de generación: " + fechaActual);
            dateCell.setCellStyle(dateStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 15));

            int rowNum = 2;

            // Encabezado del bloque resumen
            Row summaryHeader = sheet.createRow(rowNum++);
            String[] summaryTitles = {"TIPO", "NOMENCLATURA", "DISPONIBLES", "DAÑADOS", "PPTI", "A", "B", "C", "BL", "D",
                    "FS", "FX"};
            for (int i = 0; i < summaryTitles.length; i++) {
                Cell cell = summaryHeader.createCell(i);
                cell.setCellValue(summaryTitles[i]);
                cell.setCellStyle(headerStyle);
            }

            // Generación de resumen
            List<ResumentInformationDto> list = new ArrayList<>();

            //Filtro para AGUASCALIENTES por propietario
            log.info("----------" + reportFilterDto.getShippingCompany());
            if (reportFilterDto.getShippingCompany() == null
                    || "null".equalsIgnoreCase(reportFilterDto.getShippingCompany().trim())
                    || reportFilterDto.getShippingCompany().trim().isEmpty()) {

                List<?> distinct = containerRepository.getContainerTypes("AGUASCALIENTES");
                for (int i = 0; i < distinct.size(); i++) {
                    List<?> nomenclaturas = containerRepository.getNomenclaturas("AGUASCALIENTES", distinct.get(i).toString());
                    for (int j = 0; j < nomenclaturas.size(); j++) {
                        list.add(ResumentInformationDto.builder()
                                .tipo(distinct.get(i).toString())
                                .nomenclatura(nomenclaturas.get(j).toString())
                                .disponibles(containerRepository.getCount("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 1))
                                .dañados(containerRepository.getCount("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 2))
                                .ppti(containerRepository.getCount("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 5))
                                .totA(containerRepository.getCountClasifiaction("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 1))
                                .totB(containerRepository.getCountClasifiaction( "AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 2))
                                .totC(containerRepository.getCountClasifiaction("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 3))
                                .totBL(containerRepository.getCountClasifiaction("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 4))
                                .totD(containerRepository.getCountClasifiaction("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 5))
                                .totFS(containerRepository.getCountClasifiaction("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 6))
                                .totFX(containerRepository.getCountClasifiaction("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 7))
                                .build());
                    }
                }


            } else{
                //Filtro para contar todos los contenedores de AGUASCALIENTES
                List<?> distinct = containerRepository.getContainerTypesAndShippingCompany("AGUASCALIENTES" , reportFilterDto.getShippingCompany());
                for (int i = 0; i < distinct.size(); i++) {
                    List<?> nomenclaturas = containerRepository.getNomenclaturas("AGUASCALIENTES", distinct.get(i).toString());
                    for (int j = 0; j < nomenclaturas.size(); j++) {
                        list.add(ResumentInformationDto.builder()
                                .tipo(distinct.get(i).toString())
                                .nomenclatura(nomenclaturas.get(j).toString())
                                .disponibles(containerRepository.getCountByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 1, reportFilterDto.getShippingCompany()))
                                .dañados(containerRepository.getCountByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 2, reportFilterDto.getShippingCompany()))
                                .ppti(containerRepository.getCountByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 5, reportFilterDto.getShippingCompany()))
                                .totA(containerRepository.getCountClasifiactionByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 1, reportFilterDto.getShippingCompany()))
                                .totB(containerRepository.getCountClasifiactionByShippingCompany( "AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 2, reportFilterDto.getShippingCompany()))
                                .totC(containerRepository.getCountClasifiactionByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 3, reportFilterDto.getShippingCompany()))
                                .totBL(containerRepository.getCountClasifiactionByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 4, reportFilterDto.getShippingCompany()))
                                .totD(containerRepository.getCountClasifiactionByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 5, reportFilterDto.getShippingCompany()))
                                .totFS(containerRepository.getCountClasifiactionByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 6, reportFilterDto.getShippingCompany()))
                                .totFX(containerRepository.getCountClasifiactionByShippingCompany("AGUASCALIENTES", distinct.get(i).toString(), nomenclaturas.get(j).toString(), 7, reportFilterDto.getShippingCompany()))
                                .build());
                    }
                }
            }


            for (ResumentInformationDto dto : list) {
                Row row = sheet.createRow(rowNum++);
                String tipo = "UNKNOWN";
                switch (dto.getTipo()) {
                    case "1": tipo = "CH"; break;
                    case "2": tipo = "OT"; break;
                    case "3": tipo = "DC"; break;
                    case "4": tipo = "GS"; break;
                    case "5": tipo = "IMO"; break;
                    case "6": tipo = "RF"; break;
                    case "7": tipo = "HC"; break;
                }

                String[] values = {
                        tipo,
                        dto.getNomenclatura(),
                        String.valueOf(dto.getDisponibles()),
                        String.valueOf(dto.getDañados()),
                        String.valueOf(dto.getPpti()),
                        String.valueOf(dto.getTotA()),
                        String.valueOf(dto.getTotB()),
                        String.valueOf(dto.getTotC()),
                        String.valueOf(dto.getTotBL()),
                        String.valueOf(dto.getTotD()),
                        String.valueOf(dto.getTotFS()),
                        String.valueOf(dto.getTotFX()),
                };

                for (int i = 0; i < values.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values[i]);
                    cell.setCellStyle(style);
                }
            }
            // Paso 1: Inicializa los acumuladores
            int totalDisponibles = 0, totalDañados = 0, totalPpti = 0, totalA = 0, totalB = 0, totalC = 0, totalBL = 0, totalD = 0, totalFS = 0, totalFX = 0;

            // Paso 2: Suma los valores mientras recorres la lista
            for (ResumentInformationDto dto : list) {
                totalDisponibles += dto.getDisponibles();
                totalDañados += dto.getDañados();
                totalPpti += dto.getPpti();
                totalA += dto.getTotA();
                totalB += dto.getTotB();
                totalC += dto.getTotC();
                totalBL += dto.getTotBL();
                totalD += dto.getTotD();
                totalFS += dto.getTotFS();
                totalFX += dto.getTotFX();
                // ... (tu código para crear filas)
            }

            // Paso 3: Crea la fila de totales
            Row totalRow = sheet.createRow(rowNum++);
            int col = 0;
            Cell totalLabelCell = totalRow.createCell(col++);
            totalLabelCell.setCellValue("TOTALES");
            totalLabelCell.setCellStyle(headerStyle); // o el estilo que prefieras

            totalRow.createCell(col++).setCellValue(""); // NOMENCLATURA vacía
            Cell[] totalCells = new Cell[] {
                    totalRow.createCell(col++), // DISPONIBLES
                    totalRow.createCell(col++), // DAÑADOS
                    totalRow.createCell(col++), // PPTI
                    totalRow.createCell(col++), // A
                    totalRow.createCell(col++), // B
                    totalRow.createCell(col++), // C
                    totalRow.createCell(col++), // BL
                    totalRow.createCell(col++), // D
                    totalRow.createCell(col++), // FS
                    totalRow.createCell(col++), // FX
            };
            int[] totals = { totalDisponibles, totalDañados, totalPpti, totalA, totalB, totalC, totalBL, totalD, totalFS, totalFX };
            for (int i = 0; i < totals.length; i++) {
                totalCells[i].setCellValue(totals[i]);
                totalCells[i].setCellStyle(headerStyle); // o el estilo que prefieras
            }

            // Separación entre bloques
            rowNum++;

            // Encabezado del inventario
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {
                    "PROPIETARIO", "PLANTA-DESTINO","CLIENTE", "UNIDAD", "ESTADO DE LA UNIDAD", "TIPO DE UNIDAD", "TAMAÑO",
                    "CONDICION DE LA UNIDAD", "LOCALIDAD", "FECHA GATEIN", "GRADO-CALIDAD",
                    "DIAS DE ESTADIA", "FECHA FIN DE REPARACION", "EDO. ESTIMADO NAVIERA",
                    "APTO PARA", "MAQUINARIA DE UNIDAD (RF)", "FECHA DE INSPECCION", "OBSERVACIONES"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos de inventario
            List<ContainerModel> records = containerRepository.findAll(specification);
            for (ContainerModel record : records) {
                Row row = sheet.createRow(rowNum++);
                int cellIndex = 0;

                List<CatShippingCompanyModel> shippingCompany = catShippingCompanyReposirtory.getShippingDesc(record.getShippingCompany());
                String propietario = (shippingCompany.size() > 0) ? shippingCompany.get(0).getDescription() : "";
                row.createCell(cellIndex).setCellValue(propietario); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getDestinyPregate()); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getBillTo()); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getContainer()); row.getCell(cellIndex++).setCellStyle(style);

                String condicion = "UNKNOWN";
                switch (record.getCondition()) {
                    case "1": condicion = "DISPONIBLE"; break;
                    case "2": condicion = "DAÑADO"; break;
                    case "4": condicion = "EVACUACION"; break;
                    case "6": condicion = "BLOQUEADO/GX"; break;
                    case "7": condicion = "TOTAL LOSS"; break;
                    case "8": condicion = "VENTA"; break;
                    case "9": condicion = "ACCIDENTADO"; break;
                }
                row.createCell(cellIndex).setCellValue(condicion); row.getCell(cellIndex++).setCellStyle(style);

                String tipoUnidad = "UNKNOWN";
                switch (record.getContainerType()) {
                    case 1: tipoUnidad = "CH"; break;
                    case 2: tipoUnidad = "OT"; break;
                    case 3: tipoUnidad = "DC"; break;
                    case 4: tipoUnidad = "GS"; break;
                    case 5: tipoUnidad = "IMO"; break;
                    case 6: tipoUnidad = "RF"; break;
                    case 7: tipoUnidad = "HC"; break;
                }
                row.createCell(cellIndex).setCellValue(tipoUnidad); row.getCell(cellIndex++).setCellStyle(style);

                List<CatNomenclaturaModel> nomenclatura = catNomencalturaRepository.findBYTransportType(tipoUnidad, record.getContaierSize());
                String nom = (nomenclatura.size() > 0) ? nomenclatura.get(0).getNomenclatura() : "";
                row.createCell(cellIndex).setCellValue(nom); row.getCell(cellIndex++).setCellStyle(style);

                row.createCell(cellIndex).setCellValue(record.getConditionPregate()); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getLocation()); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getDateGateIn().toString()); row.getCell(cellIndex++).setCellStyle(style);

                String clasificacion = "UNKNOWN";
                switch (record.getClasification()) {
                    case "1": clasificacion = "A"; break;
                    case "2": clasificacion = "B"; break;
                    case "3": clasificacion = "C"; break;
                    case "4": clasificacion = "BL"; break;
                    case "5": clasificacion = "D"; break;
                    case "6": clasificacion = "FS"; break;
                    case "7": clasificacion = "FX"; break;
                }
                row.createCell(cellIndex).setCellValue(clasificacion); row.getCell(cellIndex++).setCellStyle(style);

                long dias = 0L;

                if (record.getDateGateIn() != null) {
                    // Obtenemos la fecha actual como LocalDateTime
                    LocalDateTime hoy = LocalDateTime.now();

                    // Calculamos la diferencia directamente en días
                    dias = ChronoUnit.DAYS.between(record.getDateGateIn(), hoy);

                    // Aseguramos que no sea negativo si así lo requieres
                    dias = Math.max(0L, dias);
                }
                row.createCell(cellIndex).setCellValue(dias); row.getCell(cellIndex++).setCellStyle(style);

                String fechaFinal = (record.getFinalDate() == null) ? "PENDIENTE" : record.getFinalDate();
                row.createCell(cellIndex).setCellValue(fechaFinal); row.getCell(cellIndex++).setCellStyle(style);

                String estado = "Pendiente";
                switch (record.getStatusQute()) {
                    case 1: estado = "Por crear"; break;
                    case 2: estado = "Creado"; break;
                    case 3: estado = "Aprobado"; break;
                    case 4: estado = "Reparación Confirmada"; break;
                    case 5: estado = "Actualizar"; break;
                    case 6: estado = "Cancelado"; break;
                    case 7: estado = "Actualizado"; break;
                    case 8: estado = "Rechazado"; break;
                    case 9: estado = "Cerrado"; break;
                    case 10: estado = "N/A"; break;
                }
                row.createCell(cellIndex).setCellValue(estado); row.getCell(cellIndex++).setCellStyle(style);

                row.createCell(cellIndex).setCellValue(record.getAptTo()); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getTypeServicePregate()); row.getCell(cellIndex++).setCellStyle(style);
                //Fecha en que se registro para preGate
                row.createCell(cellIndex).setCellValue(record.getDateInspection().toString()); row.getCell(cellIndex++).setCellStyle(style);
                row.createCell(cellIndex).setCellValue(record.getComents()); row.getCell(cellIndex++).setCellStyle(style);
            }

            for (int i = 0; i <= 17; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.toString());
            return null;
        }
    }





    @Override
    public byte[] maneuverExcel(ReportFilterDto reportFilterDto) {
        Specification<ReporteManiobraModel> specification = ContainerSpecification.byFilter(reportFilterDto);
        Specification<ContainerHistoricModel> specificationHistoric = ContainerSpecification.byShippingCompany(reportFilterDto);

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("REPORTE DE MANIOBRAS");

            // COLORES PERSONALIZADOS
            XSSFColor purpleBorder = new XSSFColor(new java.awt.Color(178, 144, 208), new DefaultIndexedColorMap());
            XSSFColor headerFill = new XSSFColor(new java.awt.Color(0, 255, 255), new DefaultIndexedColorMap());

            // ESTILO ENCABEZADO
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            ((XSSFCellStyle) headerStyle).setFillForegroundColor(headerFill);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setAllBorders(headerStyle, BorderStyle.THIN, purpleBorder);

            // ESTILO CELDAS
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setAllBorders(cellStyle, BorderStyle.THIN, purpleBorder);

            // === FILA DE TÍTULO ===
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte de maniobras");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            String[] headers = {
                    "LOCALIDAD", "TIPO DE ACTIVIDAD", "ESTADO", "EIR", "FECHA DE EVENTO", "UNIDAD",
                    "GRADO-CALIDAD", "NUMERO DE BOOKING", "NOMECLATURA",
                    "AUTORIZACION CLIENTE", "FACTURA SAP", "COSTO DE LA MANIOBRA", "PROPIETARIO", "COBRAR A:", "TIPO DE SERVICIO",
                    "COMPAÑIA TRANSPORTISTA", "NOMBRE DEL OPERADOR", "NÚMERO DEL ECONÓMICO",
                    "ORIGEN", "PLANTA DESTINO", "ANDEN", "TIPO DE ENTREGA", "MONEDA", "SELLO DE CALIDAD",
                    "SELLO DE SEGURIDAD", "TRANSMITIR POR EDI", "ESTATUS DEL EDI", "REG INSERTADO TABLA EDI",
                    "REG ENVIADO A EDI", "ARCHIVO EDI",
            };

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

            // === FILA DE FECHA ===
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            dateCell.setCellValue("Fecha de generación: " + fechaActual + " Fecha de Rango:" + reportFilterDto.getDateInit() + " - " + reportFilterDto.getDateEnd());

            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateStyle.setFont(dateFont);
            dateStyle.setAlignment(HorizontalAlignment.CENTER);
            dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dateCell.setCellStyle(dateStyle);

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.length - 1));

            // === ENCABEZADOS ===
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 3;

            List<ReporteManiobraModel> records = reporteManiobraRepository.findAll(specification);

            for (ReporteManiobraModel record : records) {
                Row row = sheet.createRow(rowNum++);
                List<CatShippingCompanyModel> shippingCompany = catShippingCompanyReposirtory.getShippingDesc(record.getShippingCompany());

                createStyledCell(row, 0, record.getLocalidad(), cellStyle);

                createStyledCell(row, 1, record.getTipoActividad(), cellStyle);
                createStyledCell(row, 2, record.getTipoUnidad(), cellStyle);
                createStyledCell(row, 3, record.getEir(), cellStyle);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String fechaEventoStr = record.getFechaEvento() == null ? "PENDIENTE" : record.getFechaEvento().format(formatter);
                createStyledCell(row, 4, fechaEventoStr, cellStyle);
                createStyledCell(row, 5, record.getUnidad(), cellStyle);

                createStyledCell(row, 6, record.getGradoCalidad(), cellStyle);
                createStyledCell(row, 7, record.getNumeroBooking(), cellStyle);



                //List<CatNomenclaturaModel> nomenclatura = catNomencalturaRepository.findBYTransportType(containerType, record.getContaierSize());
                createStyledCell(row, 8, record.getNomenclatura(), cellStyle);
                createStyledCell(row, 9, "", cellStyle);
                createStyledCell(row, 10, "", cellStyle);
                createStyledCell(row, 11, record.getCostoManiobra(), cellStyle);

                createStyledCell(row, 12, record.getPropietario(), cellStyle);

                createStyledCell(row, 13, record.getCobrarA(), cellStyle);
                createStyledCell(row, 14, record.getTipoServicio(), cellStyle);
                createStyledCell(row, 15, record.getCompaniaTransportista(), cellStyle);
                createStyledCell(row, 16, record.getNombreOperador(), cellStyle);
                createStyledCell(row, 17, record.getNumeroEconomico(), cellStyle);
                createStyledCell(row, 18, record.getOrigen(), cellStyle);
                createStyledCell(row, 19, record.getPlantaDestino(), cellStyle);
                createStyledCell(row, 20, " ", cellStyle);
                createStyledCell(row, 21, " ", cellStyle);

                for (int i = 22; i <= 29; i++) createStyledCell(row, i, " ", cellStyle);


            }

            // === HISTÓRICO ===
            List<ContainerHistoricModel> recordsHistoric = containerHitoricRepository.findAll(specificationHistoric);
            log.info("recordsHistoric: " + recordsHistoric.size());
            for (ContainerHistoricModel record : recordsHistoric) {
                Row row = sheet.createRow(rowNum++);
                List<CatShippingCompanyModel> shippingCompany = catShippingCompanyReposirtory.getShippingDesc(record.getShippingCompany());

                createStyledCell(row, 0, record.getLocation(), cellStyle);
                String typeActivity;
                switch (record.getStatus()) {
                    case 1: typeActivity = "ENTRADA"; break;
                    case 2:
                    case 3: typeActivity = "PREGATE"; break;
                    case 4: typeActivity = "GATEIN"; break;
                    case 5: typeActivity = "REPARACION"; break;
                    case 7: typeActivity = "GATEOUT"; break;
                    default: typeActivity = "UNKNOWN"; break;
                }
                createStyledCell(row, 1, typeActivity, cellStyle);
                createStyledCell(row, 2, record.getConditionPregate(), cellStyle);
                createStyledCell(row, 3, record.getEirOutName(), cellStyle);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String dateGateOutStr = record.getDateGateOut() == null ? "PENDIENTE" : record.getDateGateOut().format(formatter);
                createStyledCell(row, 4, dateGateOutStr, cellStyle);
                createStyledCell(row, 5, record.getContainer(), cellStyle);

                String clasification;
                switch (record.getClasification() != null ? record.getClasification() : "null") {
                    case "1": clasification = "A"; break;
                    case "2": clasification = "B"; break;
                    case "3": clasification = "C"; break;
                    case "4": clasification = "BL"; break;
                    case "5": clasification = "D"; break;
                    case "6": clasification = "FS"; break;
                    case "7": clasification = "FX"; break;
                    case "null": clasification = "PENDIENTE"; break;
                    default: clasification = "UNKNOWN"; break;
                }
                createStyledCell(row, 6, clasification, cellStyle);
                createStyledCell(row, 7, record.getBokking(), cellStyle);

                String containerType;
                switch (record.getContainerType()) {
                    case 1: containerType = "CH"; break;
                    case 2: containerType = "OT"; break;
                    case 3: containerType = "DC"; break;
                    case 4: containerType = "GS"; break;
                    case 5: containerType = "IMO"; break;
                    case 6: containerType = "RF"; break;
                    case 7: containerType = "HC"; break;
                    default: containerType = "UNKNOWN"; break;
                }

                List<CatNomenclaturaModel> nomenclatura = catNomencalturaRepository.findBYTransportType(containerType, record.getContaierSize());
                createStyledCell(row, 8, nomenclatura.isEmpty() ? " " : nomenclatura.get(0).getNomenclatura(), cellStyle);
                createStyledCell(row, 9, " ", cellStyle);
                createStyledCell(row, 10, " ", cellStyle);
                createStyledCell(row, 11, " ", cellStyle);


                if (!shippingCompany.isEmpty()) {
                    createStyledCell(row, 12, shippingCompany.get(0).getDescription(), cellStyle);
                } else {
                    createStyledCell(row, 12, " ", cellStyle);
                }

                if(record.getConditionPregate().equals("VACIO")){
                    createStyledCell(row, 13, record.getDefinition(), cellStyle);
                } else {
                    createStyledCell(row, 13, record.getBillTo(), cellStyle);
                }

                createStyledCell(row, 14, record.getTypeServicePregate(), cellStyle);
                createStyledCell(row, 15, record.getTransportId(), cellStyle);
                createStyledCell(row, 16, record.getOperatorName(), cellStyle);
                createStyledCell(row, 17, record.getEconomicNumber(), cellStyle);
                createStyledCell(row, 18, record.getOriginPregate(), cellStyle);
                if(record.getConditionPregate().equals("VACIO")){
                    createStyledCell(row, 19, record.getBillTo(), cellStyle);
                } else {
                    createStyledCell(row, 19, record.getDestinyPregate(), cellStyle);
                }
                createStyledCell(row, 20, " ", cellStyle);
                createStyledCell(row, 21, " ", cellStyle);

                for (int i = 22; i <= 29; i++) createStyledCell(row, i, " ", cellStyle);
            }

            for (int i = 0; i <= 29; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.toString());
            return null;
        }
    }



    private void createStyledCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private void setAllBorders(CellStyle style, BorderStyle borderStyle, XSSFColor color) {
        if (style instanceof XSSFCellStyle) {
            XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
            xssfStyle.setBorderTop(borderStyle);
            xssfStyle.setBorderBottom(borderStyle);
            xssfStyle.setBorderLeft(borderStyle);
            xssfStyle.setBorderRight(borderStyle);
            xssfStyle.setTopBorderColor(color);
            xssfStyle.setBottomBorderColor(color);
            xssfStyle.setLeftBorderColor(color);
            xssfStyle.setRightBorderColor(color);
        }
    }



    public byte[] hapagAds(ReportFilterDto reportFilterDto) {
        Specification<MandREmptyContainerReleaseModel> specification = MandREmptyContainerReleaseSpecification.byFilter(reportFilterDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("IN");

            // Estilo general centrado con bordes lilas
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            XSSFColor lilac = new XSSFColor(new java.awt.Color(200, 160, 255));
            ((XSSFCellStyle) style).setTopBorderColor(lilac);
            ((XSSFCellStyle) style).setBottomBorderColor(lilac);
            ((XSSFCellStyle) style).setLeftBorderColor(lilac);
            ((XSSFCellStyle) style).setRightBorderColor(lilac);

            // Estilo para encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(style);
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Estilo para título
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Estilo para fecha
            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateStyle.setFont(dateFont);
            dateStyle.setAlignment(HorizontalAlignment.CENTER);
            dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Definir encabezados
            String[] headers = {
                    "UNITNUMBER", "TIPO", "MODELO", "BOOKING", "FECHA EDI", "PROCESS_TIME_STAMP", "ExpirationDate",
                    "CANTIDAD_UNIDADES", "EDIFILENAME", "CUSTID", "STATUS", "localidadTmm", "Remark", "AdviceNum", "Shipment",
                    "ReleaseDate", "FreeTxt", "Assigned", "sequence", "Haulage", "TareWeight", "TareWeightUnit",
                    "Temperature", "TemperatureUnit", "VentilationOption", "VentilationOptionUnit", "FreshAir",
                    "FreshAirUnit", "Humidity", "HumidityUnit", "CO2", "CO2Unit", "O2", "O2Unit", "N", "NUnit",
                    "CarrierCustomer", "CarrierShipper", "Localidad"
            };

            // 1. Fila de título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Anuncios HApag");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

            // 2. Fila de fecha
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            dateCell.setCellValue("Fecha de generación: " + fechaActual+ " Fecha de Rango:" + reportFilterDto.getDateInit() + " - " + reportFilterDto.getDateEnd());
            dateCell.setCellStyle(dateStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.length - 1));

            // 3. Fila de encabezados
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Inicio de datos
            int rowNum = 3;
            List<MandREmptyContainerReleaseModel> mandREmpty = mandREmptyContainerReleaseRepository.findAll(specification);

            for (MandREmptyContainerReleaseModel entity : mandREmpty) {
                Row row = sheet.createRow(rowNum++);
                int col = 0;

                row.createCell(col).setCellValue(entity.getContenedor()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getTipo()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getModel()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getBooking()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getFechaEDI().format(formatter)); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getProcessTimeStamp().format(formatter)); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getExpirationDate().format(formatter)); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getCantidadUnidades()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getEdiFilename()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue("400140"); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getStatus()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue("AGS"); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getRemark()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getAdviceNum()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getShipment()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getReleaseDate().format(formatter)); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getFreeTxt()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getAssigned() ? "SI" : "NO"); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getSequence()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getHaulage()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getTareWeight()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getTareWeightUnit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getTemperature()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getTemperatureUnit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getVentilationOption()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getVentilationOptionUnit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getFreshAir()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getFreshAirUnit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getHumidity()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getHumidityUnit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getCo2()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getCo2Unit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getO2()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getO2Unit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getN()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getNUnit()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getCarrierCustomer()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getCarrierShipper()); row.getCell(col++).setCellStyle(style);
                row.createCell(col).setCellValue(entity.getLocalidad()); row.getCell(col++).setCellStyle(style);
            }

            for (int i = 0; i <= headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.toString());
            return null;
        }
    }

    @Override
    public byte[] exitDateReport(ReportFilterDto reportFilterDto) {
        // TODO: sustituye ExitModel y ExitSpecification por tus clases reales

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("EXIT_REPORT");

            // --- Estilos ---
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            XSSFColor lilac = new XSSFColor(new java.awt.Color(200, 160, 255), new DefaultIndexedColorMap());
            ((XSSFCellStyle) style).setTopBorderColor(lilac);
            ((XSSFCellStyle) style).setBottomBorderColor(lilac);
            ((XSSFCellStyle) style).setLeftBorderColor(lilac);
            ((XSSFCellStyle) style).setRightBorderColor(lilac);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(style);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateStyle.setFont(dateFont);
            dateStyle.setAlignment(HorizontalAlignment.CENTER);
            dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // --- Encabezados según tu imagen ---
            String[] headers = {
                    "CONTENEDOR", "NAVIERA", "TAM", "DESTINO", "COMENTARIOS", "HRCITA"
                    //, "CAMPO_EXTRA1", "CAMPO_EXTRA2" // añade más si los necesitas
            };

            // 1. Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte de Salida");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

            // 2. Fecha y rango
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            dateCell.setCellValue(
                    "Generado: " + ahora +
                            " | Busqueda: " + reportFilterDto.getDateInit()
            );
            dateCell.setCellStyle(dateStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.length - 1));

            // 3. Encabezados de columna
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 4. Datos
            int rowNum = 3;
            // TODO: inyecta o llama a tu repository real
            LocalDate date = reportFilterDto.getDateInit().toLocalDate();
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);

            List<ContainerModel> lista = containerRepository.findByExitDateTimeBetween(start, end);
            for (ContainerModel e : lista) {
                Row row = sheet.createRow(rowNum++);
                int col = 0;

                row.createCell(col).setCellValue(e.getContainer());
                row.getCell(col++).setCellStyle(style);

                CatShippingCompanyModel naviera = catShippingCompanyReposirtory.getPreLaborFinal(e.getShippingCompany());
                row.createCell(col).setCellValue(naviera.getDescription());
                row.getCell(col++).setCellStyle(style);

                row.createCell(col).setCellValue(e.getNomenclatura());
                row.getCell(col++).setCellStyle(style);

                row.createCell(col).setCellValue(e.getDestinyPregate());
                row.getCell(col++).setCellStyle(style);

                row.createCell(col).setCellValue(e.getComents());
                row.getCell(col++).setCellStyle(style);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String exitDateTimeStr = e.getExitDateTime() != null ? e.getExitDateTime().format(formatter) : "";
                row.createCell(col).setCellValue(exitDateTimeStr);
                row.getCell(col++).setCellStyle(style);


                // TODO: añade aquí más celdas si agregas más columnas
            }

            // 5. Auto-ajuste
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 6. Escritura
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("Error generando Excel", ex);
            return null;
        }
    }



}
