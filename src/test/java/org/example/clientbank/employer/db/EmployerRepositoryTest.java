package org.example.clientbank.employer.db;

import org.example.clientbank.employer.Employer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class EmployerRepositoryTest {

    @Autowired
    private EmployerRepository employerRepository;

    @BeforeEach
    void setUp() {
        employerRepository.deleteAll();
    }

    @Test
    void whenSaveEmployer_thenFindById() {
        Employer employer = new Employer();
        employer.setName("Google");
        employer.setAddress("USA");

        Employer savedEmployer = employerRepository.save(employer);

        Optional<Employer> foundEmployer = employerRepository.findById(savedEmployer.getId());
        assertThat(foundEmployer).isPresent();
        assertThat(foundEmployer.get().getName()).isEqualTo("Google");
    }
}