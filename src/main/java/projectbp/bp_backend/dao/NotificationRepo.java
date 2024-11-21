package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Notification;
import projectbp.bp_backend.bean.User;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndReadFalse(User user);
    void deleteByDevisId(Long devisId);

}
