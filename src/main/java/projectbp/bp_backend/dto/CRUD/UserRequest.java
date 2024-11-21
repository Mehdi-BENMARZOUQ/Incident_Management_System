package projectbp.bp_backend.dto.CRUD;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
}
