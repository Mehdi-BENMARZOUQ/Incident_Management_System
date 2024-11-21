package projectbp.bp_backend.dto.CRUD;


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
public class NotificationRequest {

    private String message;
    private boolean read;
    private Date createdAt;
    private User user;
    private Devis devis;

}
