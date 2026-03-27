package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.etablissement.entity.MockStudent;
import tn.esprit.etablissement.entity.SchoolClass;
import tn.esprit.etablissement.repository.MockStudentRepository;
import tn.esprit.etablissement.repository.SchoolClassRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockStudentService {

    private final MockStudentRepository repo;
    private final SchoolClassRepository classRepo;

    public List<MockStudent> getByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentId(establishmentId);
    }

    public List<MockStudent> getByClass(Long classId) {
        return repo.findByClassId(classId);
    }

    public List<MockStudent> getUnassigned(
            Long establishmentId, Long gradeId, Long departmentId) {
        return repo.findByEstablishmentIdAndGradeIdAndDepartmentId(
                        establishmentId, gradeId, departmentId)
                .stream()
                .filter(s -> s.getClassId() == null)
                .toList();
    }

    public MockStudent save(MockStudent student) {
        return repo.save(student);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public List<SchoolClass> autoCreateClasses(
            Long establishmentId, Long gradeId,
            Long departmentId, String academicYear,
            String gradeName) {

        List<MockStudent> students = repo
                .findByEstablishmentIdAndGradeIdAndDepartmentId(
                        establishmentId, gradeId, departmentId)
                .stream()
                .filter(s -> s.getClassId() == null)
                .toList();

        if (students.isEmpty())
            throw new RuntimeException(
                    "No unassigned students found for this grade and department.");

        List<SchoolClass> createdClasses = new ArrayList<>();
        int classIndex = 1;
        int studentIndex = 0;

        while (studentIndex < students.size()) {
            SchoolClass sc = new SchoolClass();
            sc.setName(gradeName + " — Group " + (char)('A' + classIndex - 1));
            sc.setLevel(gradeName);
            sc.setAcademicYear(academicYear);
            sc.setMaxStudents(25);
            sc.setCurrentStudents(0);
            sc.setActive(true);
            sc.setGradeId(gradeId);

            tn.esprit.etablissement.entity.Department dept =
                    new tn.esprit.etablissement.entity.Department();
            dept.setId(departmentId);
            sc.setDepartment(dept);

            sc = classRepo.save(sc);
            createdClasses.add(sc);

            int count = 0;
            while (studentIndex < students.size() && count < 25) {
                MockStudent s = students.get(studentIndex);
                s.setClassId(sc.getId());
                repo.save(s);
                studentIndex++;
                count++;
            }

            sc.setCurrentStudents(count);
            classRepo.save(sc);
            classIndex++;
        }

        return createdClasses;
    }
    @Transactional
    public MockStudent assignToClass(Long studentId, Long classId) {
        MockStudent student = repo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        SchoolClass sc = classRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        if (sc.getCurrentStudents() >= sc.getMaxStudents()) {
            throw new RuntimeException(
                    "CLASS_FULL|This class is full (" +
                            sc.getMaxStudents() + " max students).");
        }

        // Unassign from previous class if any
        if (student.getClassId() != null) {
            classRepo.findById(student.getClassId()).ifPresent(prev -> {
                prev.setCurrentStudents(
                        Math.max(0, prev.getCurrentStudents() - 1));
                classRepo.save(prev);
            });
        }

        student.setClassId(classId);
        repo.save(student);

        sc.setCurrentStudents(sc.getCurrentStudents() + 1);
        classRepo.save(sc);

        return student;
    }

    @Transactional
    public MockStudent unassignFromClass(Long studentId) {
        MockStudent student = repo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getClassId() != null) {
            classRepo.findById(student.getClassId()).ifPresent(sc -> {
                sc.setCurrentStudents(
                        Math.max(0, sc.getCurrentStudents() - 1));
                classRepo.save(sc);
            });
        }

        student.setClassId(null);
        repo.save(student);
        return student;
    }
    public List<MockStudent> getUnassignedByGrade(
            Long establishmentId, Long gradeId) {
        return repo.findByEstablishmentId(establishmentId).stream()
                .filter(s -> s.getGradeId().equals(gradeId)
                        && s.getClassId() == null)
                .collect(java.util.stream.Collectors.toList());
    }
}