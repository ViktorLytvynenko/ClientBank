package org.example.clientbank.employer.service;

import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.EmployerDto;
import org.example.clientbank.employer.status.EmployerStatus;

import java.util.List;
import java.util.Optional;

public interface EmployerService {

    List<Employer> findAll();

    Optional<Employer> getEmployerById(Long id);

    void createEmployer(String name, String address);

    void deleteById(Long id);

    EmployerStatus updateEmployer(Employer employer, EmployerDto employerDto);

    boolean updateEmployerFromDTO(Employer employer, EmployerDto employerDto);
}
