package projectbp.bp_backend.dto.CRUD;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeRequest {
    //private Long id;
    private String type;
    private String numero;
    private String message;
    private String description;
    private boolean isRead;
}
