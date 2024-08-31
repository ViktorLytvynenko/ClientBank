package org.example.clientbank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
@EnableJpaAuditing
public class ClientBankApplication implements ApplicationRunner {
    private final DataSource dataSource;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public ClientBankApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientBankApplication.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws SQLException {
        // check what base is connected
        System.out.println(dataSource.getConnection().getMetaData().getURL());
        System.out.println(activeProfile);
        System.out.println(datasourceUrl);

        System.out.println("http://localhost:9000");
        System.out.println("http://localhost:9000/h2-console");
        System.out.println("http://localhost:9000/login");
        System.out.println("https://springhw1-f5412575bf3e.herokuapp.com");
        System.out.println("https://client-bank-front-end.vercel.app");
    }
}
