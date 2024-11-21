package projectbp.bp_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Notification;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dao.NotificationRepo;
import projectbp.bp_backend.dao.UserRepo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private DevisRepo devisRepo;

    @Autowired
    private FactureRepo factureRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserRepo userRepo;


    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepo.findById(id);
    }


    public void deleteNotification(Long notificationId) {
        notificationRepo.deleteById(notificationId);
    }

    public void deleteAllNotifications(User user) {
        List<Notification> notifications = notificationRepo.findByUser(user);
        notificationRepo.deleteAll(notifications);
    }

    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepo.findByUser(user);
    }

    public void checkAndCreateNotifications() {
        List<Devis> devisList = devisRepo.findAllByRejectedFalseAndNotificationSentFalseAndHandledFalse();
        Date currentDate = new Date();

        for (Devis devis : devisList) {
            if (shouldCreateNotification(devis, currentDate)) {
                createNotification(devis);
                devis.setNotificationSent(true);
                devisRepo.save(devis);
            }
        }
    }

    private boolean shouldCreateNotification(Devis devis, Date currentDate) {
        long diffInSeconds = (currentDate.getTime() - devis.getDate().getTime()) / 1000;
        return diffInSeconds >= 10 && !factureRepo.existsByDevis(devis);
    }

    private void createNotification(Devis devis) {
        Devis freshDevis = devisRepo.findById(devis.getId()).orElseThrow(() -> new RuntimeException("Devis not found"));

        User user = freshDevis.getTraitepar();
        String message = "Devis " + devis.getNumero() + " requires attention. No facture associated after 30 days.";

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        notification.setDevis(devis);
        notification.setRead(false);

        notificationRepo.save(notification);
        freshDevis.setNotificationSent(true);
        devisRepo.save(freshDevis);
        System.out.println("Created notification for Devis: " + freshDevis.getNumero());
    }

    public void markNotificationAsRead(Long notificationId) {
        Optional<Notification> optionalNotification = notificationRepo.findById(notificationId);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setRead(true);
            notificationRepo.save(notification);

            Devis devis = notification.getDevis();
            devis.setHandled(true);
            devisRepo.save(devis);

            System.out.println("Marked notification as read and Devis as handled: " + devis.getNumero());
        } else {
            throw new RuntimeException("Notification not found: " + notificationId);
        }
    }

    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepo.findByUserAndReadFalse(user);
    }

    public void notifySupervisors(Demande demande) {
        List<User> supervisors = userRepo.findByRole("SUPERVISOR");
        for (User supervisor : supervisors) {
            // Implement your notification logic here (e.g., email, in-app notification)
            System.out.println("Notification sent to " + supervisor.getEmail() +
                    " about new demande: " + demande.getMessage());
        }
    }

    public void notifyUser(Demande demande) {
        // Implement your notification logic here
        System.out.println("Notification sent to " + demande.getDemandeur().getEmail() +
                " about demande status: " + demande.getStatus());
    }
}
