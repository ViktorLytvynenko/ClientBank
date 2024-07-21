package org.example.clientbank.employer.db;

import org.example.clientbank.employer.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
}
