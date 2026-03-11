package uk.ac.ncl.csc8019backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class Csc8019BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Csc8019BackendApplication.class, args);
    }

}