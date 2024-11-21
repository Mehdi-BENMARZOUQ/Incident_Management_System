package projectbp.bp_backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Facture;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dto.CRUD.FactureRequest;
import projectbp.bp_backend.service.FactureService;


import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class FactureController {


    @GetMapping("/public/facture/numero/{numero}")
    public Optional<Facture> findByNumero(@PathVariable String numero) {
        return factureService.findByNumero(numero);
    }


    @GetMapping("/public/facture")
    public ResponseEntity<Object> getAllFacture() {
        return ResponseEntity.ok(factureService.findAll());
    }


    @PostMapping("/public/saveFacture")
    public ResponseEntity<Object> createFacture(@RequestBody FactureRequest factureRequest) {
        return ResponseEntity.ok(factureService.createFacture(factureRequest));
    }


    @PostMapping("/supervisor/updateFacture/{id}")
    public ResponseEntity<Facture> updateFacture(@PathVariable Long id, @RequestBody FactureRequest factureRequest) {
        Facture updatedFacture = factureService.updateFacture(id, factureRequest);
        return ResponseEntity.ok(updatedFacture);
    }

    @DeleteMapping("/supervisor/deleteFacture/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    private final FactureService factureService;
}
