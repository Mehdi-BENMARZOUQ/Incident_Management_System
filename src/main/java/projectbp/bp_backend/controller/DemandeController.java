package projectbp.bp_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.DemandeResponse;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dto.CRUD.DemandeRequest;
import projectbp.bp_backend.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;


   /* @GetMapping("/all")
    public ResponseEntity<Object> getAllDemandes() {
        return ResponseEntity.ok(demandeService.findAll());
    }*/


    @GetMapping("/all")
    public ResponseEntity<Object> getAllDemandes() {
        return ResponseEntity.ok(demandeService.findAll());
    }

    @PostMapping("/feedback/{id}/read")
    public ResponseEntity<Object> markAsRead(@PathVariable Long id) {
        Demande demande = demandeService.markAsRead(id);
        return ResponseEntity.ok(demande);
    }
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadDemandCount() {
        long count = demandeService.countPendingDemandesForSupervisor();
        return ResponseEntity.ok(count);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createDemande(@RequestBody DemandeRequest demandeRequest) {
        return ResponseEntity.ok(demandeService.createDemande(demandeRequest));
    }

    @PostMapping("/handle/{id}")
    public ResponseEntity<Object> handleDemande(@PathVariable Long id, @RequestParam String responseType, @RequestParam String responseMessage) {
        return ResponseEntity.ok(demandeService.handleDemande(id, responseType, responseMessage));
    }
    


    @GetMapping("/pending")
    public ResponseEntity<Object> getPendingDemandes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            List<Demande> pendingDemandes = demandeService.getPendingDemandesForSupervisor(currentUser);
            return ResponseEntity.ok(pendingDemandes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }


    @PostMapping("/{id}")
    public void deleteDemande(@PathVariable Long id) {
        demandeService.deleteDemande(id);
    }

    @PostMapping("/deleteAll")
    public void deleteAllDemandes() {
        demandeService.deleteAllDemandes();
    }

    @GetMapping("/feedback")
    public ResponseEntity<Object> getUserFeedback() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            List<DemandeResponse> feedback = demandeService.getFeedbackForUser(currentUser);
            return ResponseEntity.ok(feedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/allfeedback")
    public ResponseEntity<Object> getAllUserFeedback() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            List<DemandeResponse> feedback = demandeService.getAllFeedbacksForUser(currentUser);
            return ResponseEntity.ok(feedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @PostMapping("/deleteAllFeedbacks")
    public ResponseEntity<Void> deleteAllFeedbacks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        demandeService.deleteAllFeedbacksForUser(currentUser);
        return ResponseEntity.noContent().build();
    }
}
