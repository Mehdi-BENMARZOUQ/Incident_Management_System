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
import projectbp.bp_backend.deserializer.AgenceDeserializer;
import projectbp.bp_backend.deserializer.TechnicienDeserializer;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String numero;
    private Date date;
    private String equipementE;
    private String prestataire;
    private Double montant;
    private Boolean assurance;

    @Column(nullable = false)
    private Boolean rejected = false;


    @Column(nullable = false)
    private Boolean notificationSent = false;

    @Column(nullable = false)
    private Boolean handled = false;


    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonDeserialize(using = TechnicienDeserializer.class)
    private Technicien technicien;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonDeserialize(using = AgenceDeserializer.class)
    private Agence agence;

    @ManyToOne
    @JsonIgnoreProperties({"devisC"})
    private User traitepar;

    public Devis(String numero) {
        this.numero = numero;
    }


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
