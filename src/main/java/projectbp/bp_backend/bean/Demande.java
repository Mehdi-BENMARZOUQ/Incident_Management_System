package projectbp.bp_backend.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type; // "Facture" or "Devis"
    private String numero;  // ID of the related Facture or Devis
    private String message;
    private String description;
    private Boolean isRead = false; // New field

    @ManyToOne
    private User demandeur;

    @ManyToOne
    private User handledBy;

    private String status; // "Pending", "Rejected", "Treated"
    private String responseMessage;
    private Date createdDate;
    private Date handledDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
        this.status = "Pending";
    }

}
