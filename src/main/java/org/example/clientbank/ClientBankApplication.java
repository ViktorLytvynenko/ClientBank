package org.example.clientbank;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClientBankApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientBankApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("http://localhost:9000");
        System.out.println("http://localhost:9000/h2-console");
        System.out.println("http://localhost:9000/login");
        System.out.println("https://springhw1-f5412575bf3e.herokuapp.com");
        System.out.println("https://client-bank-front-end.vercel.app");
    }
}
