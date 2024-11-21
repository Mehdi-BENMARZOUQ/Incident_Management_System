package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Facture;

import java.util.Optional;

public interface FactureRepo extends JpaRepository<Facture, Long> {

    Optional<Facture> findByNumero(String numero);
    boolean existsByDevis(Devis devis);
}
