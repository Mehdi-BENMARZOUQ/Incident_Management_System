package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.AgenceRepo;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.NotificationRepo;
import projectbp.bp_backend.dao.TechnicienRepo;
import projectbp.bp_backend.dto.CRUD.DevisRequest;
import org.springframework.transaction.annotation.Transactional;
import projectbp.bp_backend.dto.CRUD.UserRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


@Service
@RequiredArgsConstructor
public class DevisService {

    public Optional<Devis> findByNumero(String numero) {
        return devis_repo.findByNumero(numero);
    }


    public List<DevisRequest> findAll() {
        List<Devis> devisList = devis_repo.findAll();
        return devisList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DevisRequest convertToDTO(Devis devis) {
        DevisRequest devisDTO = new DevisRequest();
        devisDTO.setId(devis.getId());
        devisDTO.setNumero(devis.getNumero());
        devisDTO.setDate(devis.getDate());
        devisDTO.setEquipementE(devis.getEquipementE());
        devisDTO.setPrestataire(devis.getPrestataire());
        devisDTO.setMontant(devis.getMontant());
        devisDTO.setAssurance(devis.getAssurance());
        devisDTO.setRejected(devis.getRejected());
        devisDTO.setTechnicien(devis.getTechnicien());
        devisDTO.setAgence(devis.getAgence());

        UserRequest userDTO = new UserRequest();
        userDTO.setId(devis.getTraitepar().getId());
        userDTO.setNom(devis.getTraitepar().getNom());
        userDTO.setPrenom(devis.getTraitepar().getPrenom());
        userDTO.setEmail(devis.getTraitepar().getEmail());
        userDTO.setRole(devis.getTraitepar().getRole());
        devisDTO.setTraitepar(userDTO);

        return devisDTO;
    }

    public ResponseEntity<Object> createDevis(DevisRequest devisRequest) {
        User user = authenticationUserService.getCurrentUser();

        Optional<Devis> existingDevis = devis_repo.findByNumero(devisRequest.getNumero());
        if (existingDevis.isPresent()) {
            return ResponseEntity.badRequest().body("Devis with this numero already exists");
        }
        Optional<Technicien> existingTechnicien = techRepo.findByNom(devisRequest.getTechnicien().getNom());
        if (!existingTechnicien.isPresent()) {
            return ResponseEntity.badRequest().body("Technicien not found");
        }
        Optional<Agence> existingAgence = agenceRepo.findByNom(devisRequest.getAgence().getNom());
        if (!existingAgence.isPresent()) {
            return ResponseEntity.badRequest().body("Agence not found");
        }
        Devis devis = new Devis();
        devis.setNumero(devisRequest.getNumero());
        devis.setDate(new Date());
        devis.setEquipementE(devisRequest.getEquipementE());
        devis.setPrestataire(devisRequest.getPrestataire());
        devis.setMontant((devisRequest.getMontant() * 1.2));
        devis.setAssurance(devisRequest.getAssurance());
        devis.setTechnicien(existingTechnicien.get());
        devis.setAgence(existingAgence.get());
        devis.setRejected(devisRequest.getRejected());
        devis.setTraitepar(user);
        devis_repo.save(devis);
        return ResponseEntity.ok("Your Devis is created successfully");
    }


    public ResponseEntity<Object> updateDevis(Long id, DevisRequest devisRequest) {
        Devis existingDevis = devis_repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Devis non trouvé avec l'ID : " + id));
        if (devisRequest.getNumero() != null) {
            existingDevis.setNumero(devisRequest.getNumero());
        }
        if (devisRequest.getDate() != null) {
            existingDevis.setDate(devisRequest.getDate());
        }
        if (devisRequest.getEquipementE() != null) {
            existingDevis.setEquipementE(devisRequest.getEquipementE());
        }
        if (devisRequest.getPrestataire() != null) {
            existingDevis.setPrestataire(devisRequest.getPrestataire());
        }
        if (devisRequest.getMontant() != null) {
            if (existingDevis.getMontant().doubleValue() != devisRequest.getMontant().doubleValue()) {
                existingDevis.setMontant(devisRequest.getMontant() * 1.2);
            } else {
                existingDevis.setMontant(devisRequest.getMontant());
            }
        }
        if (devisRequest.getAssurance() != null) {
            existingDevis.setAssurance(devisRequest.getAssurance());
        }
        if (devisRequest.getTechnicien() != null) {
            existingDevis.setTechnicien(devisRequest.getTechnicien());
        }
        if (devisRequest.getAgence() != null) {
            existingDevis.setAgence(devisRequest.getAgence());
        }
        devis_repo.save(existingDevis);
        return ResponseEntity.ok("Devis updated successfully");
    }

    @Transactional
    public void deleteDevis(Long id) {

        if (!devis_repo.existsById(id)) {
            throw new EntityNotFoundException("Devis non trouvé avec l'ID : " + id);
        }
        notificationRepo.deleteByDevisId(id);


        devis_repo.deleteById(id);
    }
    private final AuthenticationUserService authenticationUserService;
    private final DevisRepo devis_repo;
    private final AgenceRepo agenceRepo;
    private final TechnicienRepo techRepo;
    private final NotificationRepo notificationRepo;
}
