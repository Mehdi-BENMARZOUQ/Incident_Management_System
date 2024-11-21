package projectbp.bp_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.TechnicienRepo;
import projectbp.bp_backend.dto.CRUD.TechnicienRequest;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TechnicienService {

    public Optional<Technicien> findByMatricule(String matricule) {
        return techRepo.findByMatricule(matricule);
    }

    public List<Technicien> findAll() {
        return techRepo.findAll();
    }

    public Technicien saveTechnicien(TechnicienRequest technicienRequest) {
        Technicien technicien = Technicien.builder()
                .nom(technicienRequest.getNom())
                .matricule(technicienRequest.getMatricule())
                .build();
        return techRepo.save(technicien);
    }
    public Technicien findById(Long id) {
        return techRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Technicien non trouvé avec l'ID : " + id));
    }

    public Technicien updateTechnicien(Long id, TechnicienRequest technicienRequest) {
        Technicien existingTechnicien = findById(id);
        if (technicienRequest.getNom() != null) {
            existingTechnicien.setNom(technicienRequest.getNom());
        }
        if (technicienRequest.getMatricule() != null) {
            existingTechnicien.setMatricule(technicienRequest.getMatricule());
        }
        return techRepo.save(existingTechnicien);
    }
    public ResponseEntity<Object> deleteTechnicien(Long id) {
        Technicien technicienToDelete = techRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Technicien non trouvé avec l'ID : " + id));

        List<Devis> relatedDevis = devis_repo.findByTechnicien(technicienToDelete);

        if (!relatedDevis.isEmpty()) {

            return ResponseEntity.badRequest().body("Ce Technicien est liee aux devis");
        }
        techRepo.delete(technicienToDelete);
        return ResponseEntity.ok("Done");
    }

    private final TechnicienRepo techRepo;
    private final DevisRepo devis_repo;
}
