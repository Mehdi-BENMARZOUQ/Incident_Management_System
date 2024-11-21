package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Technicien;

import java.util.List;
import java.util.Optional;
@Repository
public interface DevisRepo extends JpaRepository<Devis , Long> {

    Optional<Devis> findByNumero(String numero);
    List<Devis> findAllByRejectedFalseAndNotificationSentFalseAndHandledFalse();

    List<Devis> findByAgence(Agence agence);

    List<Devis> findByTechnicien(Technicien technicienToDelete);
}
