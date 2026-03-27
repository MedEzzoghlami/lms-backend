package tn.esprit.etablissement.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.etablissement.entity.SchoolClass;
import tn.esprit.etablissement.repository.DepartmentRepository;
import tn.esprit.etablissement.repository.SchoolClassRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportExportService {

    private final SchoolClassRepository classRepo;
    private final DepartmentRepository departmentRepo;

    public byte[] exportClassStudentsCSV(Long classId) throws IOException {
        SchoolClass sc = classRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        StringWriter sw = new StringWriter();
        sw.append("Class Name,Level,Academic Year,Max Students,Current Students,Fill Rate\n");
        double fillRate = sc.getMaxStudents() > 0
                ? (double) sc.getCurrentStudents() / sc.getMaxStudents() * 100 : 0;
        sw.append(String.format("%s,%s,%s,%d,%d,%.1f%%\n",
                sc.getName(), sc.getLevel(), sc.getAcademicYear(),
                sc.getMaxStudents(), sc.getCurrentStudents(), fillRate));

        return sw.toString().getBytes();
    }

    public byte[] exportDepartmentStatsExcel(Long establishmentId) throws IOException {
        List<tn.esprit.etablissement.entity.Department> departments =
                departmentRepo.findByEstablishmentId(establishmentId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Department Stats");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row header = sheet.createRow(0);
        String[] cols = {"Department", "Code", "Description"};
        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (tn.esprit.etablissement.entity.Department d : departments) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(d.getName());
            row.createCell(1).setCellValue(d.getCode());
            row.createCell(2).setCellValue(
                    d.getDescription() != null ? d.getDescription() : ""
            );
        }

        for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    public byte[] exportDepartmentStatsPDF(Long establishmentId) throws Exception {
        List<tn.esprit.etablissement.entity.Department> departments =
                departmentRepo.findByEstablishmentId(establishmentId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 18,
                com.itextpdf.text.Font.BOLD);
        document.add(new Paragraph("Department Statistics", titleFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        com.itextpdf.text.Font headFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12,
                com.itextpdf.text.Font.BOLD, BaseColor.WHITE);

        for (String col : new String[]{"Department", "Code", "Description"}) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(new BaseColor(13, 110, 253));
            cell.setPadding(8);
            table.addCell(cell);
        }

        for (tn.esprit.etablissement.entity.Department d : departments) {
            table.addCell(new PdfPCell(new Phrase(d.getName())));
            table.addCell(new PdfPCell(new Phrase(d.getCode())));
            table.addCell(new PdfPCell(new Phrase(
                    d.getDescription() != null ? d.getDescription() : ""
            )));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public List<String> importStudentsFromExcel(MultipartFile file, Long classId)
            throws IOException {
        List<String> results = new ArrayList<>();
        SchoolClass sc = classRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        int imported = 0;
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Cell nameCell = row.getCell(0);
            Cell emailCell = row.getCell(1);
            if (nameCell == null || emailCell == null) continue;

            String name = nameCell.getStringCellValue().trim();
            String email = emailCell.getStringCellValue().trim();

            if (!name.isEmpty() && !email.isEmpty()) {
                results.add("Imported: " + name + " (" + email + ")");
                imported++;
            }
        }

        sc.setCurrentStudents(sc.getCurrentStudents() + imported);
        classRepo.save(sc);

        workbook.close();
        results.add(0, "Total imported: " + imported + " students into " + sc.getName());
        return results;
    }
}