package az.ingress;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableAsync
public class BookStore {

    public static void main(String[] args) {
        run(BookStore.class, args);
    }
}