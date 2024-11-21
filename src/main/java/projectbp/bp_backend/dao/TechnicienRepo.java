package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.Technicien;

import java.util.Optional;

@Repository
public interface TechnicienRepo extends JpaRepository<Technicien, Long> {

    Optional<Technicien> findByMatricule(String matricule);
    Optional<Technicien> findByNom(String nom);
}
