package projectbp.bp_backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import projectbp.bp_backend.service.NotificationService;

import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    private NotificationService notificationService;


    @Scheduled(fixedRate = 5000) //5 seconds for test only
    // will be executed every 29 days
    //@Scheduled(cron = "0 0 0 */29 * ?")
    public void checkDevisAndCreateNotifications() {
        System.out.println("Running scheduled task at " + new Date());
        notificationService.checkAndCreateNotifications();
    }
}