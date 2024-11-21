package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.User;

import java.util.List;

@Repository
public interface DemandeRepo  extends JpaRepository<Demande, Long> {
    List<Demande> findByStatus(String status);


    List<Demande> findByDemandeur(User demandeur);

    @Query("SELECT count(*) FROM Demande d WHERE d.status = ?1 OR d.status = ?2")
    long countByStatusOrByStatus( String status1,String status2);

    long countByStatus(String status);
}
