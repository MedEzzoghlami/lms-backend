package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.etablissement.service.ImportExportService;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImportExportController {

    private final ImportExportService service;

    @GetMapping("/class/{classId}/csv")
    public ResponseEntity<byte[]> exportClassCSV(@PathVariable Long classId)
            throws Exception {
        byte[] data = service.exportClassStudentsCSV(classId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=class_" + classId + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/establishment/{id}/departments/excel")
    public ResponseEntity<byte[]> exportDepartmentsExcel(@PathVariable Long id)
            throws Exception {
        byte[] data = service.exportDepartmentStatsExcel(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=departments_" + id + ".xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/establishment/{id}/departments/pdf")
    public ResponseEntity<byte[]> exportDepartmentsPDF(@PathVariable Long id)
            throws Exception {
        byte[] data = service.exportDepartmentStatsPDF(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=departments_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    @PostMapping("/class/{classId}/import")
    public ResponseEntity<List<String>> importStudents(
            @PathVariable Long classId,
            @RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(service.importStudentsFromExcel(file, classId));
    }
}