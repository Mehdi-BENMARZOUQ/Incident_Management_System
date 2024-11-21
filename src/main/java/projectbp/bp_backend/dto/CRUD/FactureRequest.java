package projectbp.bp_backend.dto.CRUD;


import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.User;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FactureRequest {

    private Long id;
    private String numero;
    private Date date_facture;
    private Date date_traitement;
    private Double montant;
    private Devis devis;
    private UserRequest traitepar;

}
