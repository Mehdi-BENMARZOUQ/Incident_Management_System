package projectbp.bp_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import projectbp.bp_backend.bean.*;
import projectbp.bp_backend.dao.DemandeRepo;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dao.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.dto.CRUD.DemandeRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepo demandeRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private DevisRepo devisRepository;

    @Autowired
    private FactureRepo factureRepository;




    public List<DemandeRequest> findAll() {
        List<Demande> demandes = demandeRepository.findAll();
        return demandes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DemandeRequest convertToDTO(Demande demande) {
        DemandeRequest dto = new DemandeRequest();
        //dto.setId(demande.getId());
        dto.setType(demande.getType());
        dto.setNumero(demande.getNumero());
        dto.setMessage(demande.getMessage());
        dto.setDescription(demande.getDescription());
        dto.setRead(demande.getIsRead());
        return dto;
    }

    public ResponseEntity<Object> createDemande(DemandeRequest demandeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Demande demande = new Demande();
        demande.setType(demandeRequest.getType());
        demande.setMessage(demandeRequest.getMessage());
        demande.setDescription(demandeRequest.getDescription());
        demande.setDemandeur(currentUser);
        demande.setStatus("Pending");
        demande.setCreatedDate(new Date());

        if ("Devis".equalsIgnoreCase(demandeRequest.getType())) {
            Devis devis = devisRepository.findByNumero(demandeRequest.getNumero())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Devis numero"));
            demande.setNumero(devis.getNumero());
        } else if ("Facture".equalsIgnoreCase(demandeRequest.getType())) {
            Facture facture = factureRepository.findByNumero(demandeRequest.getNumero())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Facture numero"));
            demande.setNumero(facture.getNumero());
        }

        notifySupervisors(demande);
        Demande savedDemande = demandeRepository.save(demande);

        return ResponseEntity.ok(savedDemande);
    }

     public List<DemandeResponse> getFeedbackForUser(User user) {
        List<Demande> demandes = demandeRepository.findByDemandeur(user);
        return demandes.stream()
                .filter(demande -> demande.getStatus().equals("Handled") || demande.getStatus().equals("Rejected"))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

     public List<DemandeResponse> getAllFeedbacksForUser(User user) {
        List<Demande> demandes = demandeRepository.findByDemandeur(user);
        return demandes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DemandeResponse mapToResponse(Demande demande) {
        DemandeResponse response = new DemandeResponse();
        response.setId(demande.getId());
        response.setType(demande.getType());
        response.setNumero(demande.getNumero());
        response.setMessage(demande.getMessage());
        response.setDescription(demande.getDescription());
        response.setDemandeur(demande.getDemandeur().getEmail());
        response.setHandledBy(demande.getHandledBy() != null ? demande.getHandledBy().getEmail() : null);
        response.setStatus(demande.getStatus());
        response.setResponseMessage(demande.getResponseMessage());
        response.setCreatedDate(demande.getCreatedDate());
        response.setHandledDate(demande.getHandledDate());
        response.setRead(demande.getIsRead());

        return response;
    }

    public ResponseEntity<Object> handleDemande(Long demandeId, String responseType, String responseMessage) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid demande ID"));

        if (!"Pending".equals(demande.getStatus())) {
            String message = "This demande has already been handled by " + demande.getHandledBy().getEmail();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        demande.setHandledBy(currentUser);
        demande.setHandledDate(new Date());
        demande.setStatus(responseType);
        demande.setResponseMessage(responseMessage);

        notifyUser(demande);
        mapToResponse(demande);

        return ResponseEntity.ok(demandeRepository.save(demande));
    }


    private void notifyUser(Demande demande) {
        // Implement notification logic here
        System.out.println("Notification sent to " + demande.getDemandeur().getEmail() +
                " about demande status: " + demande.getStatus());
    }

    public Demande markAsRead(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid demande ID"));

        demande.setIsRead(true);
        return demandeRepository.save(demande);
    }

    public long countPendingDemandesForSupervisor() {
        return demandeRepository.countByStatus( "Pending");
    }


    public void notifySupervisors(Demande demande) {
        List<User> supervisors = userRepository.findByRole("SUPERVISOR");
        for (User supervisor : supervisors) {
            System.out.println("Notification sent to " + supervisor.getEmail() + " about new demande: " + demande.getMessage());
        }
    }

    public void deleteDemande(Long demandeId) {
        demandeRepository.deleteById(demandeId);
    }

    public void deleteAllDemandes() {
        demandeRepository.deleteAll();
    }

    /*Filter Demands*/
    public List<Demande> getPendingDemandesForSupervisor(User user) {
        if (!"SUPERVISOR".equals(user.getRole())) {
            throw new IllegalArgumentException("You are not a supervisor");
        }
        return demandeRepository.findByStatus("Pending");
    }

    public void deleteAllFeedbacksForUser(User user) {
        //List<Demande> userFeedbacks = demandeRepository.findByDemandeur(user);

        List<Demande> demandesToDelete = demandeRepository.findByDemandeur(user).stream()
                .filter(demande -> !demande.getStatus().equals("Pending"))
                .collect(Collectors.toList());

        demandeRepository.deleteAll(demandesToDelete);
    }
}
