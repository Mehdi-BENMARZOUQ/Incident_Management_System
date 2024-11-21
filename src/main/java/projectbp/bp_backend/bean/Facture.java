package projectbp.bp_backend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import projectbp.bp_backend.deserializer.DevisDeserializer;
import projectbp.bp_backend.deserializer.TechnicienDeserializer;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String numero;
    private Date date_facture;
    private Date date_traitement;
    private Double montant;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonDeserialize(using = DevisDeserializer.class)
    private Devis devis;

    @ManyToOne
    @JsonIgnoreProperties({"FactureC"})
    private User traitepar;

    @PrePersist
    public void initTraitepar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                this.traitepar = (User) principal;
            } else {
                throw new IllegalStateException("Utilisateur non trouvé dans le contexte de sécurité.");
            }
        } else {
            throw new IllegalStateException("Aucun utilisateur authentifié trouvé.");
        }
    }



}
