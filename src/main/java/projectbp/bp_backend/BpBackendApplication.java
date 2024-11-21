package projectbp.bp_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpBackendApplication.class, args);
    }

}