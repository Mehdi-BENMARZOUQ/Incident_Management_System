package projectbp.bp_backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dto.CRUD.DevisRequest;
import projectbp.bp_backend.service.DevisService;


import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DevisController {


    @GetMapping("/public/devis/numero/{numero}")
    public Optional<Devis> findByNumero(@PathVariable String numero) {
        return devisService.findByNumero(numero);
    }
    @GetMapping("/public/devis")
    public ResponseEntity<Object> getAllDevis() {
        return ResponseEntity.ok(devisService.findAll());
    }



    @PostMapping("/public/savedevis")
    public ResponseEntity<Object> createDevis(@RequestBody DevisRequest devisRequest) {
        return ResponseEntity.ok(devisService.createDevis(devisRequest));
    }


    @PostMapping("/supervisor/updatedevis/{id}")
    public ResponseEntity<Object> updateDevis(@PathVariable Long id, @RequestBody DevisRequest devisRequest) {
        ResponseEntity<Object> updatedDevis = devisService.updateDevis(id, devisRequest);
        return ResponseEntity.ok(updatedDevis);
    }

    @DeleteMapping("/supervisor/deletedevis/{id}")
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        devisService.deleteDevis(id);
        return ResponseEntity.noContent().build();
    }

    private final DevisService devisService;
}
