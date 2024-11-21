package projectbp.bp_backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.dao.AgenceRepo;
import projectbp.bp_backend.dto.CRUD.AgenceRequest;
import projectbp.bp_backend.dto.CRUD.DevisRequest;
import projectbp.bp_backend.dto.auth.RegisterRequest;
import projectbp.bp_backend.service.AgenceService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AgenceController {

    private final AgenceService agenceService;

    @GetMapping("/public/agence")
    public ResponseEntity<Object> getAllAgences() {
        return ResponseEntity.ok(agenceService.findAll());
    }


    @GetMapping("/public/agence/nom/{nom}")
    public Optional<Agence> findByNom(@PathVariable  String nom) {
        return agenceService.findByNom(nom);
    }

    @PostMapping("/supervisor/saveagence")
    public ResponseEntity<Object> createAg(@RequestBody AgenceRequest agencereq) {
        return ResponseEntity.ok(agenceService.createAgence(agencereq));
    }

    @PostMapping("/supervisor/updateAgence/{id}")
    public ResponseEntity<Agence> updateAgence( @PathVariable Long id, @RequestBody AgenceRequest agencereq) {

        Agence agence = agenceService.updateAgence(id, agencereq);
        return ResponseEntity.ok(agence);
    }

    @DeleteMapping("/supervisor/deleteagence/{id}")
    public ResponseEntity<Object> deleteAgence(@PathVariable Long id) {

        return agenceService.deleteAgence(id);
    }




}
