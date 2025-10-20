
package com.tmm.myre.base.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
// import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HapagEdiFileGeneratorService {
/*
    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    @Value("${hapag.customerId:400140}")
    private int customerId;
    @Value("${hapag.outDir:/export/home/tibcobw/MandR/HapagLloyd/OutBoundEDIFiles}")
    private String outDir;
    @Value("${hapag.sequenceName:HP_EDI_OutBoundSeq}")
    private String sequenceName;
    @Value("${hapag.customerName:HLC}")
    private String customerName;
    @Value("${hapag.timezone:America/Mexico_City}")
    private String tz;

    // SQL constants (Java 8-friendly)
    private static final String SQL_FIND_PENDING =
            "SELECT gateEventIdentifier " +
                    "FROM GateActivityOutbound_EDI " +
                    "WHERE customerIdentifier = ? AND errorNumber IS NULL " +
                    "ORDER BY gateEventIdentifier";

    private static final String SQL_FETCH_EVENT =
            "SELECT " +
                    "  UPPER(eventType) AS eventType, " +
                    "  eventDate, " +
                    "  booking, " +
                    "  UPPER(fillState) AS fillState, " +
                    "  type, model, location, container, " +
                    "  alternateUnit, associatedUnit, " +
                    "  transportType, " +
                    "  shopId " +
                    "FROM GateEventsIntegration " +
                    "WHERE gateEventIdentifier = ?";

    private static final String SQL_MARK_OK_PREFIX =
            "UPDATE GateActivityOutbound_EDI " +
                    "   SET errorNumber = 0, errorMessage = 'OK', ediFileName = :fileName, processTimeStamp = GETDATE() " +
                    " WHERE gateEventIdentifier IN (:ids)";

    private static final String SQL_MARK_ERROR =
            "UPDATE GateActivityOutbound_EDI " +
                    "   SET errorNumber = -1, errorMessage = ?, processTimeStamp = GETDATE() " +
                    " WHERE gateEventIdentifier = ?";

    public HapagEdiFileGeneratorService(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    // Actívalo si quieres ejecución periódica (cada 5 minutos, por ejemplo desde application.yml)
    // @Scheduled(fixedDelayString = "${hapag.pollingMillis:300000}")
    public void runCycle() { runOnce(); }

    */
/** Un ciclo: leer pendientes, armar archivo, escribir y marcar en BD. *//*

    public void runOnce() {
        List<Long> pendingIds = findPendingGateEvents(customerId);
        if (pendingIds.isEmpty()) {
            return;
        }

        StringBuilder ediBodies = new StringBuilder();
        List<Long> successIds = new ArrayList<Long>();

        for (Long id : pendingIds) {
            try {
                GateEvent ev = fetchGateEventDetail(id);
                LocationCode loc = mapLocationToHapag(ev.getLocation(), ev.getShopId());
                if (!loc.isValid()) {
                    markError(id, "Localidad sin mapeo a Hapag");
                    continue;
                }
                BookingInfo booking = resolveBooking(ev.getContainer(), ev.getLocation(), ev.getBooking());
                String body = buildCodecoBody(ev, loc, booking);
                ediBodies.append(body);
                successIds.add(id);
            } catch (Exception ex) {
                markError(id, abbreviate("EXCEPTION: " + safeMsg(ex), 240));
            }
        }

        if (successIds.isEmpty()) return;

        long seq = nextVal(sequenceName);
        String header = buildUNB(seq, customerName);
        String footer = buildUNZ(seq, successIds.size());
        String ediMessage = header + ediBodies.toString() + footer;
        String fileName = makeFileName();

        try {
            Path dir = Paths.get(outDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Files.write(dir.resolve(fileName), ediMessage.getBytes(StandardCharsets.UTF_8));
            markAsProcessed(successIds, fileName);
        } catch (IOException io) {
            for (Long id : successIds) {
                markError(id, abbreviate("IO al escribir " + fileName + ": " + safeMsg(io), 200));
            }
        }
    }

    // ===================== Data Access =====================

    private List<Long> findPendingGateEvents(int customerId) {
        return jdbc.query(SQL_FIND_PENDING, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getLong(1);
            }
        }, customerId);
    }

    private GateEvent fetchGateEventDetail(Long id) {
        return jdbc.queryForObject(SQL_FETCH_EVENT, new RowMapper<GateEvent>() {
            @Override
            public GateEvent mapRow(ResultSet rs, int i) throws SQLException {
                LocalDateTime dt = rs.getTimestamp("eventDate") != null
                        ? rs.getTimestamp("eventDate").toLocalDateTime() : null;
                return new GateEvent(
                        id,
                        rs.getString("eventType"),
                        dt,
                        rs.getString("booking"),
                        rs.getString("fillState"),
                        rs.getString("type"),
                        rs.getString("model"),
                        rs.getString("location"),
                        rs.getString("container"),
                        rs.getString("alternateUnit"),
                        rs.getString("associatedUnit"),
                        rs.getString("transportType"),
                        rs.getString("shopId")
                );
            }
        }, id);
    }

    private void markAsProcessed(List<Long> ids, String fileName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("fileName", fileName);
        params.put("ids", ids);
        namedJdbc.update(SQL_MARK_OK_PREFIX, params);
    }

    private void markError(Long id, String msg) {
        jdbc.update(SQL_MARK_ERROR, msg, id);
    }

    // ===================== SP/Helpers =====================

    private BookingInfo resolveBooking(String container, String location, String bookingInEvent) {
        if (hasText(bookingInEvent)) {
            return new BookingInfo(bookingInEvent);
        }
        try {
            SimpleJdbcCall call = new SimpleJdbcCall(jdbc).withProcedureName("GetBookingForGATEIN_HALO");
            MapSqlParameterSource in = new MapSqlParameterSource()
                    .addValue("Contenedor", container)
                    .addValue("Localidad", location);
            Map<String, Object> out = call.execute(in);
            String booking = asString(out.get("Booking_out"));
            if (hasText(booking)) {
                return new BookingInfo(booking);
            }
        } catch (Exception ignore) {
            // Si falla el SP, sigue sin booking
        }
        return BookingInfo.empty();
    }

    private long nextVal(String seqName) {
        try {
            SimpleJdbcCall call = new SimpleJdbcCall(jdbc).withProcedureName("nextval");
            MapSqlParameterSource in = new MapSqlParameterSource().addValue("sequence", seqName);
            Map<String, Object> out = call.execute(in);
            Object seqObj = out.get("sequence_id");
            if (seqObj != null) {
                return Long.parseLong(String.valueOf(seqObj));
            }
        } catch (Exception e) {
            // fallback razonable si el SP no existe
        }
        return System.currentTimeMillis();
    }

    // ===================== EDI Builders =====================

    private String buildUNB(long seq, String customer) {
        String procDate = ZonedDateTime.now(ZoneId.of(tz))
                .format(DateTimeFormatter.ofPattern("yyMMdd:HHmm"));
        return "UNB+UNOA:1+TMML+" + customer + "+" + procDate + "+" + seq + "'\n";
    }

    private String buildUNZ(long seq, int count) {
        return "UNZ+" + count + "+" + seq + "'\n";
    }

    private String buildCodecoBody(GateEvent ev, LocationCode loc, BookingInfo booking) {
        String localType = mapModelToLocalType(ev.getModel(), ev.getType());
        String transport = mapTransport(ev.getTransportType());
        String eventType = isGateIn(ev.getEventType()) ? "34" : "35";
        String eventDate = formatEventDate203(ev);
        String status = !"F".equalsIgnoreCase(ev.getFillState()) ? "4" : "5";

        int lines = 8;
        if (booking.hasBooking()) lines++;
        if (hasText(ev.getAlternateUnit()) && !"AGS".equalsIgnoreCase(loc.getShopId())) lines++;
        if (hasText(ev.getAssociatedUnit()) && !"AGS".equalsIgnoreCase(loc.getShopId())) lines++;

        String unhId = ev.getId() + "A";
        StringBuilder sb = new StringBuilder();
        sb.append("UNH+").append(unhId).append("+CODECO:D:95B:UN:ITG14'\n");
        sb.append("BGM+").append(eventType).append("+I").append(ev.getId()).append("+9'\n");
        sb.append("NAD+MS+TMML'\n");
        sb.append("EQD+").append(localType).append("+").append(ev.getContainer())
                .append("+").append(ev.getModel()).append(":102:5+++").append(status).append("'\n");
        if (booking.hasBooking()) {
            sb.append("RFF+BN:").append(booking.getBooking()).append("'\n");
        }
        sb.append("DTM+7:").append(eventDate).append(":203'\n");
        sb.append("LOC+165+MX").append(loc.getShopId()).append(":139:6+").append(loc.getHapagCode()).append("'\n");
        if (hasText(ev.getAlternateUnit()) && !"AGS".equalsIgnoreCase(loc.getShopId())) {
            sb.append("EQA+RG+").append(ev.getAlternateUnit()).append("'\n");
        }
        if (hasText(ev.getAssociatedUnit()) && !"AGS".equalsIgnoreCase(loc.getShopId())) {
            sb.append("EQA+CH+").append(ev.getAssociatedUnit()).append("'\n");
        }
        sb.append("TDT+1+CARRIER+").append(transport).append("'\n");
        sb.append("CNT+16:1'\n");
        sb.append("UNT+").append(lines).append("+").append(unhId).append("'\n");
        return sb.toString();
    }

    // ===================== Mapeos / Utils =====================

    private boolean isGateIn(String t) {
        return t != null && ("GATEIN".equalsIgnoreCase(t) || "IN".equalsIgnoreCase(t));
    }

    private String formatEventDate203(GateEvent ev) {
        if (ev.getEventDate() == null) return "";
        return ev.getEventDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }

    */
/** Catálogo demo: cambia por tu tabla/catálogo real *//*

    private String mapModelToLocalType(String model, String fallbackType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("22G1", "CN");  // 20' General
        map.put("45G1", "HC");  // 40' High Cube
        map.put("30CL", "RG");  // Reefer/Genset ejemplo
        if (model != null && map.containsKey(model.toUpperCase())) {
            return map.get(model.toUpperCase());
        }
        return defaultOr(fallbackType, "CN");
    }

    */
/** TRUCK -> 3; otros (rail/barge) -> 2 *//*

    private String mapTransport(String t) {
        if (t == null) return "3";
        return t.equalsIgnoreCase("TRUCK") ? "3" : "2";
    }

    */
/** Reemplázalo por consulta a tu tabla de mapeo de patios *//*

    private LocationCode mapLocationToHapag(String location, String shopId) {
        Map<String, LocationCode> demo = new HashMap<String, LocationCode>();
        demo.put("GUADALAJARA", new LocationCode("GDL", "MXGDL"));
        demo.put("AGS", new LocationCode("AGS", "MXAGS"));

        String key = defaultOr(shopId, location);
        if (key != null) {
            LocationCode lc = demo.get(key.toUpperCase());
            if (lc != null) return lc;
        }
        return new LocationCode(null, null);
    }

    private boolean hasText(String s) { return s != null && !s.trim().isEmpty(); }
    private String defaultOr(String v, String def) { return (v == null || v.trim().isEmpty()) ? def : v; }
    private String asString(Object o) { return o == null ? null : String.valueOf(o); }

    private String makeFileName() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(tz));
        String ts = now.format(DateTimeFormatter.ofPattern("MMddyyHHmm"));
        return "HAPACODECO" + ts + ".txt";
    }

    private String safeMsg(Throwable t) { return t == null ? "" : String.valueOf(t.getMessage()); }

    private String abbreviate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }

*/
}


