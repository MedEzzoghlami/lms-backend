package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.entity.Department;
import tn.esprit.etablissement.entity.SchoolClass;
import tn.esprit.etablissement.repository.DepartmentRepository;
import tn.esprit.etablissement.repository.SchoolClassRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository repo;
    private final DepartmentRepository departmentRepo;
    private final SchoolClassRepository repository; // ✅ THIS WAS MISSING


    // 🔹 Get classes by department
    public List<SchoolClass> getByDepartment(Long departmentId) {
        return repo.findByDepartmentId(departmentId);
    }

    // 🔹 Get active classes by establishment
    public List<SchoolClass> getActiveByEstablishment(Long establishmentId) {
        return repo.findByDepartmentEstablishmentIdAndActiveTrue(establishmentId);
    }

    // 🔹 Get active classes by establishment and level
    public List<SchoolClass> getActiveByEstablishmentAndLevel(Long establishmentId, String level) {
        return repo.findByDepartmentEstablishmentIdAndLevelAndActiveTrue(establishmentId, level);
    }

    // 🔹 Create a class
    public SchoolClass create(Long departmentId, SchoolClass sc) {
        Department dept = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));

        // ✅ Sécurisation nulls pour Integer et Boolean
        if (sc.getMaxStudents() == null) sc.setMaxStudents(25);
        if (sc.getCurrentStudents() == null) sc.setCurrentStudents(0);
        if (sc.getActive() == null) sc.setActive(true);

        if (sc.getMaxStudents() > 25)
            throw new RuntimeException("Max students cannot exceed 25");

        sc.setDepartment(dept);
        return repo.save(sc);
    }

    // 🔹 Update a class
    public SchoolClass update(Long id, SchoolClass updated) {
        SchoolClass existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found: " + id));

        existing.setName(updated.getName());
        existing.setAcademicYear(updated.getAcademicYear());
        existing.setLevel(updated.getLevel());

        // ✅ Null safe update
        if (updated.getMaxStudents() != null) existing.setMaxStudents(updated.getMaxStudents());
        if (updated.getCurrentStudents() != null) existing.setCurrentStudents(updated.getCurrentStudents());
        if (updated.getActive() != null) existing.setActive(updated.getActive());

        return repo.save(existing);
    }

    // 🔹 Increment student count
    public void incrementStudentCount(Long id) {
        SchoolClass sc = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found: " + id));

        if (sc.getCurrentStudents() >= sc.getMaxStudents())
            throw new RuntimeException("Class is full");

        sc.setCurrentStudents(sc.getCurrentStudents() + 1);
        repo.save(sc);
    }

    // 🔹 Delete class
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Optional<SchoolClass> getById(Long id) {
        return repository.findById(id);
    }
}