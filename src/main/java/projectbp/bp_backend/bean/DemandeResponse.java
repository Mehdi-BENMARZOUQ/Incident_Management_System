package projectbp.bp_backend.bean;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class DemandeResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    private String numero;
    private String message;
    private String description;
    private String demandeur;
    private String handledBy;
    private String status;
    private String responseMessage;
    private Date createdDate;
    private Date handledDate;
    private boolean isRead;
}
