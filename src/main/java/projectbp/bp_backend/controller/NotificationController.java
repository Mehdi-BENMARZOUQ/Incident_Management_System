package projectbp.bp_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Notification;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.service.NotificationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @PostMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    @PostMapping
    public void deleteAllNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        notificationService.deleteAllNotifications(user);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(user);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{id}/mark-as-read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            notificationService.markNotificationAsRead(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Optional<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }
}
