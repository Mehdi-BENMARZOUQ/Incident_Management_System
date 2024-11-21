package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.dto.CRUD.TechnicienRequest;
import projectbp.bp_backend.service.TechnicienService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TechnicienController {

    @GetMapping("/public/technicien/matricule/{matricule}")
    public Optional<Technicien> findByMatricule(@PathVariable String matricule) {
        return techService.findByMatricule(matricule);
    }

    @GetMapping("/public/technicien")
    public List<Technicien> getAllTechniciens() {
        return techService.findAll();
    }

    @PostMapping("/supervisor/savetechnicien")
    public Technicien saveTechnicien(@RequestBody TechnicienRequest technicienRequest) {
        return techService.saveTechnicien(technicienRequest);
    }

    @DeleteMapping("/supervisor/deletetechnicien/{id}")
    public ResponseEntity<Object> deleteTechnicien(@PathVariable Long id) {
        return techService.deleteTechnicien(id);
    }

    @PostMapping("/supervisor/updatetechnicien/{id}")
    public Technicien updateTechnicien(@PathVariable Long id,  @RequestBody  TechnicienRequest technicienRequest) {
        return techService.updateTechnicien(id, technicienRequest);
    }

    private final TechnicienService techService;

}
