package projectbp.bp_backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.User;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String role;
    private User users;
    private List<Agence> agences;
    private List<Devis> devis;
}
