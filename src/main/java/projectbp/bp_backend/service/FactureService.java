package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Facture;
import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dto.CRUD.DevisRequest;
import projectbp.bp_backend.dto.CRUD.FactureRequest;
import projectbp.bp_backend.dto.CRUD.UserRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FactureService {


    private final DevisRepo devisRepo;

    public Optional<Facture> findByNumero(String numero) {
        return facture_repo.findByNumero(numero);
    }


    public List<FactureRequest> findAll() {
        List<Facture> factureList = facture_repo.findAll();
        return factureList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private FactureRequest convertToDTO(Facture facture) {
        FactureRequest factureDTO = new FactureRequest();
        factureDTO.setNumero(facture.getNumero());
        factureDTO.setDate_traitement(facture.getDate_traitement());
        factureDTO.setMontant(facture.getMontant());
        factureDTO.setDate_facture(facture.getDate_facture());
        factureDTO.setId(facture.getId());

        UserRequest userDTO = new UserRequest();
        userDTO.setId(facture.getTraitepar().getId());
        userDTO.setNom(facture.getTraitepar().getNom());
        userDTO.setPrenom(facture.getTraitepar().getPrenom());
        userDTO.setEmail(facture.getTraitepar().getEmail());
        userDTO.setRole(facture.getTraitepar().getRole());
        factureDTO.setTraitepar(userDTO);

        Devis devisDTO = new Devis();
        devisDTO.setId(facture.getDevis().getId());
        devisDTO.setNumero(facture.getDevis().getNumero());
        devisDTO.setDate(facture.getDevis().getDate());
        devisDTO.setEquipementE(facture.getDevis().getEquipementE());
        devisDTO.setPrestataire(facture.getDevis().getPrestataire());
        devisDTO.setMontant(facture.getDevis().getMontant());
        devisDTO.setAssurance(facture.getDevis().getAssurance());
        devisDTO.setRejected(facture.getDevis().getRejected());
        devisDTO.setTechnicien(facture.getDevis().getTechnicien());
        devisDTO.setAgence(facture.getDevis().getAgence());


        factureDTO.setDevis(devisDTO);

        return factureDTO;
    }


    public ResponseEntity<Object> createFacture(FactureRequest factureRequest) {
        User user = authenticationUserService.getCurrentUser();
        Optional<Facture> existingFacture = facture_repo.findByNumero(factureRequest.getNumero());
        if (existingFacture.isPresent()) {
            return ResponseEntity.badRequest().body("Facture with this Numero already exists");
        }
            Optional<Devis> existingDevis = devisRepo.findByNumero(factureRequest.getDevis().getNumero());
        if (!existingDevis.isPresent()) {
            return ResponseEntity.badRequest().body("Facture not found");
        }
        Devis devis = existingDevis.get();
        Facture facture = new Facture();
        facture.setNumero(factureRequest.getNumero());
        facture.setDate_traitement(new Date());
        facture.setDate_facture(factureRequest.getDate_facture());
        facture.setMontant(factureRequest.getMontant() * 1.2);
        facture.setDevis(devis);
        facture.setTraitepar(user);
        facture_repo.save(facture);
        return ResponseEntity.ok("Your Facture is created successfully");
    }

    public Facture updateFacture(Long id, FactureRequest factureRequest) {
        Facture existingFacture = facture_repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facture non trouvé avec l'ID : " + id));

        if (factureRequest.getNumero() != null) {
            existingFacture.setNumero(factureRequest.getNumero());
        }
        if(factureRequest.getDate_facture() != null) {
            existingFacture.setDate_facture(factureRequest.getDate_facture());
        }
        if(factureRequest.getMontant() != null) {
            if (existingFacture.getMontant().doubleValue() != factureRequest.getMontant().doubleValue()) {
                existingFacture.setMontant(factureRequest.getMontant() * 1.2);
            } else {
                existingFacture.setMontant(factureRequest.getMontant());
            }

        }
        if (factureRequest.getDevis() != null && factureRequest.getDevis().getNumero() != null) {
            Optional<Devis> existingDevis = devisRepo.findByNumero(factureRequest.getDevis().getNumero());
            if (existingDevis.isPresent()) {
                existingFacture.setDevis(existingDevis.get());
            } else {
                throw new EntityNotFoundException("Devis non trouvé avec le Numero : " + factureRequest.getDevis().getNumero());
            }
        }
        return facture_repo.save(existingFacture);
    }

    public void deleteFacture(Long id) {
        if (!facture_repo.existsById(id)) {
            throw new EntityNotFoundException("Facture non trouvé avec l'ID : " + id);
        }
        facture_repo.deleteById(id);
    }
    private Devis convertToDevis(DevisRequest devisRequest) {
        return devisRepo.findByNumero(devisRequest.getNumero()).orElse(null);
    }
    private final AuthenticationUserService authenticationUserService;
    private final FactureRepo facture_repo;

}
