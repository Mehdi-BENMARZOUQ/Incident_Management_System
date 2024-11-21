package projectbp.bp_backend.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    @Column(name = "`read`")
    private Boolean read = false;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "devis_id")
    @JsonIgnoreProperties("notifications")
    private Devis devis;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("notifications")
    private User user;

    private Date createdAt;


    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }


}
