package projectbp.bp_backend.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Technicien {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String matricule;
    private String nom;

    public Technicien(String nom) {
        this.nom = nom;
    }

}
