package com.playa.controller;

import com.playa.service.CommentService;
import com.playa.service.PlaylistService;
import com.playa.service.SongService;
import com.playa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final CommentService commentService;
    private final SongService songService;
    private final PlaylistService playlistService;
    private final UserService userService;

    // POST /reports/content - Reportar contenido inadecuado (US-014)
    @PostMapping("/content")
    @PreAuthorize("hasRole('LISTENER') or hasRole('ARTIST') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> reportContent(
            @RequestHeader("idUser") Long idUser,
            @RequestBody Map<String, Object> reportData) {

        String contentType = (String) reportData.get("contentType");
        String reason = (String) reportData.get("reason");

        // Validar que se proporcione el motivo del reporte
        if (reason == null || reason.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Debes indicar el motivo del reporte");
            errorResponse.put("message", "El campo 'reason' es obligatorio");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Validar tipo de contenido
        if (contentType == null || contentType.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Tipo de contenido no especificado");
            errorResponse.put("message", "Debes especificar el tipo de contenido a reportar");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Verificar que el usuario existe
        userService.getUserById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> response = new HashMap<>();
        response.put("reportId", System.currentTimeMillis()); // Simulado
        response.put("reporterId", idUser);
        response.put("contentType", contentType);
        response.put("contentId", reportData.get("contentId"));
        response.put("reason", reason);
        response.put("description", reportData.get("description"));
        response.put("status", "PENDING");
        response.put("reportDate", java.time.LocalDateTime.now().toString());
        response.put("message", "Reporte enviado exitosamente. Será revisado por moderación.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /reports - Obtener todos los reportes (Solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllReports() {
        List<Map<String, Object>> reports = new ArrayList<>();

        // Simulando reportes existentes
        Map<String, Object> report1 = new HashMap<>();
        report1.put("reportId", 1L);
        report1.put("contentType", "SONG");
        report1.put("contentId", 1L);
        report1.put("reason", "Contenido ofensivo");
        report1.put("status", "PENDING");
        report1.put("reportDate", "2024-11-07T10:30:00");
        reports.add(report1);

        Map<String, Object> report2 = new HashMap<>();
        report2.put("reportId", 2L);
        report2.put("contentType", "COMMENT");
        report2.put("contentId", 5L);
        report2.put("reason", "Spam");
        report2.put("status", "RESOLVED");
        report2.put("reportDate", "2024-11-06T15:20:00");
        reports.add(report2);

        return ResponseEntity.ok(reports);
    }

    // GET /reports/pending - Obtener reportes pendientes (Solo ADMIN)
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getPendingReports() {
        List<Map<String, Object>> pendingReports = new ArrayList<>();

        // Simulando reportes pendientes
        Map<String, Object> report = new HashMap<>();
        report.put("reportId", 1L);
        report.put("contentType", "SONG");
        report.put("contentId", 1L);
        report.put("reason", "Contenido ofensivo");
        report.put("status", "PENDING");
        report.put("reportDate", "2024-11-07T10:30:00");
        report.put("priority", "HIGH");
        pendingReports.add(report);

        return ResponseEntity.ok(pendingReports);
    }

    // PUT /reports/{id}/resolve - Resolver reporte (Solo ADMIN)
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> resolveReport(
            @PathVariable Long id,
            @RequestParam String action) {

        Map<String, String> response = new HashMap<>();

        if ("APPROVE".equals(action)) {
            // Simular acción de aprobar (ocultar contenido)
            response.put("message", "Reporte #" + id + " resuelto. Contenido ocultado exitosamente");
            response.put("action", "CONTENT_HIDDEN");
        } else if ("REJECT".equals(action)) {
            // Simular acción de rechazar reporte
            response.put("message", "Reporte #" + id + " rechazado. No se requiere acción");
            response.put("action", "NO_ACTION");
        } else {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Acción no válida. Use 'APPROVE' o 'REJECT'")
            );
        }

        response.put("reportId", id.toString());
        response.put("status", "RESOLVED");
        response.put("resolvedDate", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    // GET /reports/user/{userId} - Obtener reportes de un usuario (Solo el usuario o ADMIN)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getReportsByUser(@PathVariable Long userId) {
        // Verificar que el usuario existe
        userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Map<String, Object>> userReports = new ArrayList<>();

        // Simulando reportes del usuario
        Map<String, Object> report = new HashMap<>();
        report.put("reportId", 3L);
        report.put("reporterId", userId);
        report.put("contentType", "PLAYLIST");
        report.put("contentId", 2L);
        report.put("reason", "Contenido inapropiado");
        report.put("status", "PENDING");
        report.put("reportDate", "2024-11-07T12:00:00");
        userReports.add(report);

        return ResponseEntity.ok(userReports);
    }

    // GET /reports/statistics - Obtener estadísticas de reportes (Solo ADMIN)
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getReportStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReports", 150);
        stats.put("pendingReports", 12);
        stats.put("resolvedReports", 138);
        stats.put("reportsByType", Map.of(
                "SONG", 85,
                "COMMENT", 45,
                "PLAYLIST", 20
        ));
        stats.put("reportsByReason", Map.of(
                "Contenido ofensivo", 60,
                "Spam", 35,
                "Violencia", 25,
                "Otros", 30
        ));

        return ResponseEntity.ok(stats);
    }
}
