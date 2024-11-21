package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.User;

import java.util.Optional;

@Repository
public interface AgenceRepo extends JpaRepository<Agence, Long> {

    Optional<Agence> findByNom(String nom);

}
